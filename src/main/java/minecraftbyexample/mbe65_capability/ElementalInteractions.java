package minecraftbyexample.mbe65_capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Created by TGG on 21/06/2020.
 *
 * This class models what happens when elemental Fire or Air interact with Entities:
 *
 * 1) The arrow disappears immediately without causing damage or impact
 * 2) If the arrow strikes a block, it produces a harmless particle effect only
 * 3) If the arrow strikes a living entity:
 *   a) Elemental Air is transferred to the entity.  The entity levitates into the air by a variable amount depending
 *      on how much Elemental Air it has accumulated
 *   b) Elemental Fire is transferred to the entity.  The entity receives a variable speed boost depending on how
 *      much Elemental Fire is has accumulated
 *   c) If the entity receives both Elemental Air and Elemental Fire, it explodes.
 *
 * It is triggered by subscribing to the ProjectileImpactEvent.
 *
 */
public class ElementalInteractions {

  public static final int MAX_FIRE_CHARGE_LEVEL_ARROW = 400;    // an arrow can't hold more than this amount of fire
  public static final int MAX_FIRE_CHARGE_LEVEL_ENTITY = 1000;  // a living entity can't hold more than this amount of fire

  public static final int MAX_AIR_CHARGE_LEVEL_ARROW = 100;    // an arrow can't hold more than this amount of air
  public static final int MAX_AIR_CHARGE_LEVEL_ENTITY = 1000;  // a living entity can't hold more than this amount of air

  // When a fire or air arrow strikes an entity or block...
  @SubscribeEvent
  public static void onProjectileImpact(ProjectileImpactEvent.Arrow event) {
    AbstractArrowEntity arrowEntity = event.getArrow();
    ElementalFire arrowFire = arrowEntity.getCapability(CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE).orElse(null);
    ElementalAir arrowAir = arrowEntity.getCapability(CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR).orElse(null);

    // If this arrow wasn't fired by an elemental bow, return immediately and treat it like a vanilla arrow
    if (    (arrowFire == null || arrowFire.getChargeLevel() == 0)
            && (arrowAir == null || arrowAir.getChargeLevel() == 0)) {
      return;
    }

    World world = arrowEntity.getEntityWorld();
    if (world.isRemote()) return;
    if (!(world instanceof ServerWorld)) throw new AssertionError("ServerWorld expected");
    ServerWorld serverWorld = (ServerWorld)world;

    RayTraceResult rayTraceResult = event.getRayTraceResult();
    switch (rayTraceResult.getType()) {
      case BLOCK:
        if (arrowFire != null) blockHitFire(serverWorld, rayTraceResult, arrowFire);
        if (arrowAir != null) blockHitAir(serverWorld, rayTraceResult, arrowAir);
        break;
      case ENTITY:
        Optional<LivingEntity> livingEntity = getLivingEntityFromRayTraceResult(rayTraceResult);
        if (livingEntity.isPresent()) {
          if (arrowAir != null) entityHitAir(serverWorld, livingEntity.get(), arrowAir);
          if (arrowFire != null) entityHitFire(serverWorld, livingEntity.get(), arrowFire);
          checkForFireAirMixture(serverWorld, livingEntity.get());
        }
        break;

      case MISS:
        LOGGER.warn("Unexpected RayTraceResult:" + rayTraceResult.getType());
        break;

      default:
        LOGGER.warn("Unexpected RayTraceResult:" + rayTraceResult.getType());
        return;
    }

    event.setCanceled(true);
    arrowEntity.remove();
  }

  /**
   * When the block is hit by a fire arrow, spawn particles at the point of impact
   * @param serverWorld
   * @param rayTraceResult
   * @param fireInterfaceInstance
   */
  private static void blockHitFire(ServerWorld serverWorld, RayTraceResult rayTraceResult,
                                   ElementalFire fireInterfaceInstance) {
    int arrowFireChargeLevel = fireInterfaceInstance.getChargeLevel();
    if (arrowFireChargeLevel == 0) return;

    if (!(rayTraceResult instanceof BlockRayTraceResult)) throw new AssertionError("BlockRayTraceResult expected");
    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTraceResult;
    Vector3d hitPosition = blockRayTraceResult.getHitVec();

    final int MAX_SMOKE_PARTICLES = 20;
    int smokeParticleCount = 1 + ((MAX_SMOKE_PARTICLES - 1) * arrowFireChargeLevel / MAX_FIRE_CHARGE_LEVEL_ARROW);
    final Vector3d OFFSET_VARIATION = new Vector3d(0.5, 0.25, 0.5);
    final int SPEED = 0;

    serverWorld.spawnParticle(ParticleTypes.LARGE_SMOKE, hitPosition.getX(), hitPosition.getY(), hitPosition.getZ(),
            smokeParticleCount, OFFSET_VARIATION.getX(), OFFSET_VARIATION.getY(), OFFSET_VARIATION.getZ(), SPEED);

    serverWorld.spawnParticle(ParticleTypes.FLAME, hitPosition.getX(), hitPosition.getY(), hitPosition.getZ(),
            1, 0, 0, 0, SPEED);
  }

  /**
   * When the block is hit by an air arrow, spawn particles at the point of impact
   * @param serverWorld
   * @param rayTraceResult
   * @param airInterfaceInstance
   */
  private static void blockHitAir(ServerWorld serverWorld, RayTraceResult rayTraceResult,
                                   ElementalAir airInterfaceInstance) {
    int arrowAirChargeLevel = airInterfaceInstance.getChargeLevel();
    if (arrowAirChargeLevel == 0) return;

    if (!(rayTraceResult instanceof BlockRayTraceResult)) throw new AssertionError("BlockRayTraceResult expected");
    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTraceResult;
    Vector3d hitPosition = blockRayTraceResult.getHitVec();

    final Vector3d OFFSET_VARIATION = new Vector3d(0.0, 0.0, 0.0);
    final int SPEED = 0;
    final int PARTICLE_COUNT = 1;
    serverWorld.spawnParticle(ParticleTypes.CLOUD, hitPosition.getX(), hitPosition.getY(), hitPosition.getZ(),
            PARTICLE_COUNT, OFFSET_VARIATION.getX(), OFFSET_VARIATION.getY(), OFFSET_VARIATION.getZ(), SPEED);

    serverWorld.spawnParticle(ParticleTypes.HAPPY_VILLAGER, hitPosition.getX(), hitPosition.getY(), hitPosition.getZ(),
            1, 0, 0, 0, SPEED);
  }

  /**
   * When the entity is hit by a fire arrow, add to the entity's elemental fire level and grant it a speed boost
   * @param serverWorld
   * @param livingEntity
   * @param arrowFire
   */
  private static void entityHitFire(ServerWorld serverWorld, LivingEntity livingEntity,
                                    ElementalFire arrowFire) {
    ElementalFire entityFire = livingEntity.getCapability(CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE).orElse(null);
    if (arrowFire.getChargeLevel() == 0) return;
    if (entityFire == null) return;

    entityFire.addCharge(arrowFire.getChargeLevel());

    final int DURATION_SECONDS = 600;
    final int TICKS_PER_SECOND = 20;
    final int DURATION_TICKS = DURATION_SECONDS * TICKS_PER_SECOND;
    final int MAXIMUM_AMPLIFICATION = 10;
    int amplification = 0 + MAXIMUM_AMPLIFICATION * (entityFire.getChargeLevel() / MAX_FIRE_CHARGE_LEVEL_ENTITY);
    EffectInstance speedEffect = new EffectInstance(Effects.SPEED, DURATION_TICKS, amplification);
    livingEntity.addPotionEffect(speedEffect);
  }

  /**
   * When the entity is hit by an air arrow, add to the entity's elemental air level and levitate it
   * @param serverWorld
   * @param livingEntity
   * @param arrowAir
   */
  private static void entityHitAir(ServerWorld serverWorld, LivingEntity livingEntity,
                                    ElementalAir arrowAir) {
    ElementalAir entityAir = livingEntity.getCapability(CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR).orElse(null);
    if (entityAir == null) return;
    if (arrowAir.getChargeLevel() == 0) return;

    entityAir.addCharge(arrowAir.getChargeLevel());

    final int DURATION_SECONDS = 2;
    final int TICKS_PER_SECOND = 20;
    final int DURATION_TICKS = DURATION_SECONDS * TICKS_PER_SECOND;
    final int MAXIMUM_AMPLIFICATION = 10;
    float amplification = 0 + MAXIMUM_AMPLIFICATION * (entityAir.getChargeLevel() / (float)MAX_AIR_CHARGE_LEVEL_ENTITY);
    EffectInstance speedEffect = new EffectInstance(Effects.LEVITATION, DURATION_TICKS, (int)amplification);
    livingEntity.addPotionEffect(speedEffect);
  }

  /**
   * If the entity has both fire and air, create an explosion (the greater the stored fire and air, the greater the explosion)
   * @param serverWorld
   * @param livingEntity
   */
  private static void checkForFireAirMixture(ServerWorld serverWorld, LivingEntity livingEntity) {
    ElementalFire entityFire = livingEntity.getCapability(CapabilityElementalFire.CAPABILITY_ELEMENTAL_FIRE).orElse(null);
    if (entityFire == null) return;
    ElementalAir entityAir = livingEntity.getCapability(CapabilityElementalAir.CAPABILITY_ELEMENTAL_AIR).orElse(null);
    if (entityAir == null) return;
    if (entityAir.getChargeLevel() > 0 && entityFire.getChargeLevel() > 0) {
      Vector3d entityPos = livingEntity.getPositionVec();

      final float MINIMUM_RADIUS = 0.5F;
      final float MAXIMUM_RADIUS = 10F;
      float airChargeFraction = entityAir.getChargeLevel() / (float)MAX_AIR_CHARGE_LEVEL_ENTITY;
      float fireChargeFraction = entityFire.getChargeLevel() / (float)MAX_FIRE_CHARGE_LEVEL_ENTITY;
      float combinedChargeFraction = Math.max(airChargeFraction, fireChargeFraction);

      float explosionRadius = MINIMUM_RADIUS + (MAXIMUM_RADIUS - MINIMUM_RADIUS) * combinedChargeFraction;

      final Entity NO_ENTITY_CAUSED_DAMAGE = null;
      final boolean CAUSES_FIRE = true;
      serverWorld.createExplosion(NO_ENTITY_CAUSED_DAMAGE, DamageSource.MAGIC, (ExplosionContext)null,
                                  entityPos.getX(), entityPos.getY(), entityPos.getZ(),
                                  explosionRadius, CAUSES_FIRE, Explosion.Mode.BREAK);
    }
  }

  /**
   * Retrieves a LivingEntity from the rayTraceResult, if possible
   * @param rayTraceResult
   * @return the livingEntity, or absent if not possible (not an Entity hit, or isn't a LivingEntity)
   */
  private static Optional<LivingEntity> getLivingEntityFromRayTraceResult(RayTraceResult rayTraceResult) {
    if (!(rayTraceResult instanceof EntityRayTraceResult)) return Optional.empty();
    EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;

    Entity entity = entityRayTraceResult.getEntity();
    if (!(entity instanceof LivingEntity)) return Optional.empty();
    LivingEntity livingEntity = (LivingEntity) entity;
    return Optional.of(livingEntity);
  }

    private static final Logger LOGGER = LogManager.getLogger();
}
