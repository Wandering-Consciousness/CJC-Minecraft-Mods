# MBE32_INVENTORY_ITEM

This example is a Flower Bag- an item that can be used to contain other items (in this case flowers).

It illustrates three concepts:
1) How to add an inventory to an item
2) How to use Forge Capability 
3) How to synchronise capability information to the client for use in rendering.

The code is heavily based on the Mystical Flower Bag from the Botania mod, created by Vazkii (thanks dude!):
[https://botaniamod.net/](https://botaniamod.net/)

Adding an inventory to an item is very similar to TileEntities, i.e. you have a `ContainerType`, `Container`, and `ContainerScreen`, which are used to add/remove items from the inventory.  The inventory is stored in a Capability which is attached to the FlowerBag ItemStack.

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClientOnly` - used for registering the various components
* `ItemFlowerBag` - used to define the flower bag's behaviour when clicking
* `ItemStackHandlerFlowerBag` - used to store the flowers
* `CapabilityProviderFlowerBag` - used to define which Capabilities the FlowerBag possesses
* `ContainerFlowerBag`  - used to collate items together into one place (eg from player inventory and flowerbag inventory)
* `ContainerScreenFlowerBag` - used to draw the GUI for the container
* `resources\assets\minecraftbyexample\textures\gui\mbe32_flower_back_bg.png`

See ReadMeFlowerBagDataFlow.png for a diagram of the dataflow between the classes.

## Synchronisation 
The key to understanding how this example works is to keep in mind the client-server synchronisation that is required.
 On the server, the data (the flowers) are all stored in a capability attached to the server ItemStack.  There is also a client ItemStack and this is synchronised automatically by vanilla using NBT data, when vanilla detects that the ItemStack has changed. This normally happens in ServerPlayerEntity.tick(), which calls `this.openContainer.detectAndSendChanges()`;<br>
 But beware!  The Capability information for the ItemStack is stored as a field that the vanilla server does not use when checking for changes that it needs to send to the client.  This field is only accessed when serialising / deserialising the ItemStack for loading/saving to disk.<br>
 In addition, when your ItemStack is sent from server to client, it happens differently depending on whether the server is local or is on a different machine:
 * If the server and client are on the same machine, the synchronisation packet (eg SSetSlotPacket) is transmitted from the server to the client using a Netty LocalChannel.  This method does not serialise & deserialise the ItemStack, i.e. the client just receives a direct copy of the ItemStack (produced using ItemStack.copy();) which also copies the Capability field. 
 * If the server and client are on different machines, the synchronisation packet is transmitted using an actual network channel, which uses IForgeItemStack.getShareTag() and IForgeItemStack.readShareTag() to serialise and deserialise the ItemStack.  By default, these methods do not copy the capability information.
 
 This means that if you update server Capability information, and you want the client to know about the change (eg because the capability affects rendering), you must do two things:
 1) manually trigger the server to resynchronise the ItemStack to the client, and
 2) override your Item.getShareTag() and readShareTag() to transmit your relevant capability information   

## Synchronisation in Graphical User Interface    
The synchronisation for the Graphical User Interface (GUI) is different.  It takes place through two Containers, one on the server and one on the client:<br>   
When the flower bag GUI is opened, two Containers are created.  The Container on the server is linked to the server's ItemStack.  But the Container on the client is a dummy for temporary storage only, it is NOT linked to the client's ItemStack: it is synchronised with the server container instead.  The manipulation of flowers in slots is handled automatically between the two containers, and does not involve the client ItemStack at all.

See also
* Background information on [Containers](http://greyminecraftcoder.blogspot.com/2020/04/containers-1144.html)

## Common errors

* "Array index out of bounds"--you have messed up the mapping of slot numbers to inventories using addSlotToContainer
* Items appear in strange spots, or can't click on slots--you have messed up the [x,y] positions of the slots; you may have forgotten to offset by guiLeft and guiTop
* Items disappear from the flower bag after loading and reloading--your NBT read/write code is wrong / mismatched

## Futher notes
ItemHandlerHelper has a number of useful methods to help you work with ItemStackHandlers and other inventories

### Capabilities
The terminology that Forge uses for capabilities is a bit confusing, because they use the same word (Capability) to refer to both the type of Capability (eg Capability<IItemHandler> such as CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) and the "storage" class ('interface instance') that the Capability supplies (e.g. IItemHandler) 

The key concepts you need to understand:
1) A Capability represents some extra functionality that is added to your object (ItemStack, Entity, etc)
2) Your object needs to have a CapabilityProvider.  This is used for two main purposes
  1) When Forge wants to know if your object supports a given Capability<Type>, it asks the CapabilityProvider using the getCapability() method.  If the type is supported, the CapabilityProvider returns the corresponding interface instance, which Forge then uses to interact with the Capability.
  2) To read/write the Capability information to permanent storage, i.e. NBT tags. 
3) A CapabilityProvider can be attached to objects in one of two ways:
  1) By overriding the appropriate method for the object (eg IForgeItem::initCapabilities()), which is useful for classes that you have added to the game yourself
  2) Using AttachCapabilitiesEvent, which allows you to attach your own Capability to vanilla objects
4) Each instance of your object gets its own instance of CapabilityProvider
5) The CapabilityProvider uses "Lazy initialisation", i.e. instead of fully initialising your interface instance when the CapabilityProvider is created, it waits until the first time that someone requests the interface instance.

A concrete example:
We want to give our ItemFlowerBag the ability to store Flowers.
1) The Capability that lets us do this is CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.  This Capability has a corresponding interface instance of ItemStackhandlerFlowerBag, which can store up to 16 ItemStacks.
2) Our CapabilityProvider is CapabilityProviderFlowerBag.  
  1) When CapabilityProviderFlowerBag::getCapability is called with the CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, it returns an ItemStackhandlerFlowerBag, which the caller can use to look in the bag or add more flowers.
  2) When an ItemFlowerBag is saved to disk, CapabilityProviderFlowerBag writes the ItemStackhandlerFlowerBag to NBT.  Likewise, when loading an ItemFlowerBag, CapabilityProviderFlowerBag reads the ItemStackhandlerFlowerBag out of NBT.
3) We attach the CapabilityProviderFlowerBag using ItemFlowerBag::initCapabilities
4) This creates a new instance of CapabilityProviderFlowerBag for each time that an ItemFlowerBag ItemStack is created 
5) Our CapabilityProviderFlowerBag does not actually create an ItemStackhandlerFlowerBag until the first time that someone calls CapabilityProviderFlowerBag::getCapability

In this example, we use a GUI to interact with the Capability via a Container:
The GUI talks to the Container, which manipulates the ItemStackHandlerFlowerBag that it obtained from the ItemFlowerBag.

You might wonder what the point of using a Capability is- you could instead just use a method in your ItemFlowerBag class to keep its own ItemStackHandler as an NBT tag attached to the item, and handle the loading/saving to NBT directly.
The advantage of using the Capability is that other mods will be able to interact with the ItemStack to retrieve items from it.  For an ItemFlowerBag that might not be terribly useful (since there are no vanilla items which will ask other items if they can store objects), but if you are making a new block container of some sort, then it might allow vanilla objects to interact with your container.

### For more information about capabilities:
https://gist.github.com/williewillus/c8dc2a1e7963b57ef436c699f25a710d
https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/


