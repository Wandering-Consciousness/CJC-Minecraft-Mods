package minecraftbyexample.mbe10_item_simple;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;

/**
 * User: The Grey Ghost
 * Date: 30/12/2014
 *
 * ItemSimple is an ordinary two-dimensional item
 * For background information on item see here http://greyminecraftcoder.blogspot.com/2013/12/items.html
 *   and here http://greyminecraftcoder.blogspot.com.au/2014/12/item-rendering-18.html
 */
public class ItemSimple extends Item
{
  static private final int MAXIMUM_NUMBER_OF_FROGS = 6; // maximum stack size
  public ItemSimple()
  {
    super(new Item.Properties().maxStackSize(MAXIMUM_NUMBER_OF_FROGS).group(ItemGroup.MISC) // the item will appear on the Miscellaneous tab in creative
    );
  }
}
