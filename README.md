# TightInventories
TightInventories is a shortcut for defining inventory click listeners inside your Bukkit plugins. Here's how you use it:

```java
InventoryFactory inventoryFactory = new InventoryFactory(myPlugin);
TightInventory tightInventory = inventoryFactory.createInventory();

tightInventory.setItem(0, new ItemStack(Material.DIAMOND), new ClickHandler() {
  @Override
  public boolean itemClicked(InventoryClickEvent event) {
    Bukkit.broadcastMessage("Hi there");
    
    return true; // Returning true cancels the event. Returning false doesn't modify the existing outcome.
  }
});

inventory.openInventory(player);
```

**WARNING:** You must the ```inventory.openInventory(player)``` method as opposed to ```player.openInventory(inventory)``` or the handlers will not work.

TightInventories also provides a way for you to set handlers for specific slots without having to set an item first:
```java
tightInventory.setHandler(index, handler);
```

### Things that are broken
TightInventories does not yet support certain inventory methods:
- ```removeItem(ItemStack... items)```
- ```remove(int materialId)```
- ```remove(Material material)```
- ```remove(ItemStack item)```
