package pw.teg.tightinventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TightInventory extends BaseInventory implements Listener {

    private Map<Integer, ClickHandler> handlerMap = new HashMap<>();
    private Set<UUID> openedInv = new HashSet<>();
    private final UUID inventoryId = UUID.randomUUID();

    TightInventory(InventoryHolder owner, InventoryType type) {
        super(owner, type);
    }

    TightInventory(InventoryHolder owner, InventoryType type, String title) {
        super(owner, type, title);
    }

    TightInventory(InventoryHolder owner, int size) {
        super(owner, size);
    }

    TightInventory(InventoryHolder inventoryHolder, int size, String title) {
        super(inventoryHolder, size, title);
    }

    public boolean hasHandler(int index) throws IllegalArgumentException {
        if (index >= inventory.getSize()) {
            throw new IllegalArgumentException("Inventory does not contain index " + index);
        }

        return handlerMap.containsKey(index);
    }

    public ClickHandler getHandler(int index) throws IllegalArgumentException {
        if (index >= inventory.getSize()) {
            throw new IllegalArgumentException("Inventory does not contain index " + index);
        }

        if (!handlerMap.containsKey(index)) {
            throw new IllegalArgumentException("There is no handler for item at index " + index);
        }

        return handlerMap.get(index);
    }

    public void setHandler(int index, ClickHandler handler) throws IllegalArgumentException {
        if (index >= inventory.getSize()) {
            throw new IllegalArgumentException("Inventory does not contain index " + index);
        }

        if (handler == null) {
            handlerMap.remove(index);
        } else {
            handlerMap.put(index, handler);
        }
    }

    public void openInventory(Player player) {
        player.openInventory(this);
        openedInv.add(player.getUniqueId());
    }

    Map<Integer, ClickHandler> getHandlerMap() {
        return handlerMap;
    }

    Set<UUID> getOpenedInv() {
        return openedInv;
    }

    @Override
    public void setItem(int index, ItemStack item) {
        handlerMap.remove(index);

        super.setItem(index, item);
    }

    public void setItem(int index, ItemStack item, ClickHandler clickHandler) {
        handlerMap.put(index, clickHandler);

        super.setItem(index, item);
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        throw new UnsupportedOperationException("Remove array of items not supported for TightInventory");
    }

    @Override
    public void remove(int materialId) {
        throw new UnsupportedOperationException("Remove by material id not supported for TightInventory");
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Remove by material not supported for TightInventory");
    }

    @Override
    public void remove(ItemStack item) {
        throw new UnsupportedOperationException("Remove by itemstack not supported for TightInventory");
    }

    @Override
    public void clear(int index) {
        handlerMap.remove(index);

        super.clear(index);
    }

    @Override
    public void clear() {
        handlerMap.clear();

        super.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TightInventory inventory = (TightInventory) o;

        return inventoryId.equals(inventory.inventoryId);
    }

    @Override
    public int hashCode() {
        return inventoryId.hashCode();
    }
}
