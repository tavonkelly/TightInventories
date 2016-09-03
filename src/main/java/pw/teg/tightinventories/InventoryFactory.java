package pw.teg.tightinventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class InventoryFactory implements Listener {

    private Set<TightInventory> inventorySet = Collections.newSetFromMap(new WeakHashMap<TightInventory, Boolean>());

    public InventoryFactory(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public TightInventory createInventory(InventoryHolder owner, InventoryType type) {
        TightInventory inventory = new TightInventory(owner, type);

        inventorySet.add(inventory);

        return inventory;
    }

    public TightInventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        TightInventory inventory = new TightInventory(owner, type, title);

        inventorySet.add(inventory);

        return inventory;
    }

    public TightInventory createInventory(InventoryHolder owner, int size) {
        TightInventory inventory = new TightInventory(owner, size);

        inventorySet.add(inventory);

        return inventory;
    }

    public TightInventory createInventory(InventoryHolder inventoryHolder, int size, String title) {
        TightInventory inventory = new TightInventory(inventoryHolder, size, title);

        inventorySet.add(inventory);

        return inventory;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }

        if (isInsideInventory(e.getPlayer())) {
            return;
        }

        checkInventory(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        checkInventory(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        checkInventory(e.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent e) {
        checkInventory(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        EntityType type = e.getRightClicked().getType();
        if ((type == EntityType.VILLAGER || type == EntityType.MINECART) && isInsideInventory(player)) {
            e.setCancelled(true);
        }
    }

    private void checkInventory(HumanEntity entity) {
        TightInventory inventory = null;

        for (TightInventory inv : inventorySet) {
            if (inv.getOpenedInv().contains(entity.getUniqueId())) {
                inventory = inv;
                break;
            }
        }

        if (inventory == null) {
            return;
        }

        inventory.getOpenedInv().remove(entity.getUniqueId());
    }

    private TightInventory getInventory(HumanEntity entity) {
        for (TightInventory inv : inventorySet) {
            if (inv.getOpenedInv().contains(entity.getUniqueId())) {
                return inv;
            }
        }

        return null;
    }

    private boolean isInsideInventory(HumanEntity entity) {
        return getInventory(entity) != null;
    }

    @EventHandler
    public void tightInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTopInventory() == null || !e.getClickedInventory().equals(e.getView().getTopInventory())) {
            return;
        }

        TightInventory inv = getInventory(e.getWhoClicked());

        if (inv == null) {
            return;
        }

        if (!inv.getTitle().equals(e.getView().getTopInventory().getTitle())) {
            return;
        }

        if (e.getRawSlot() < 0) {
            return;
        }

        ClickHandler handler = inv.getHandlerMap().get(e.getRawSlot());

        if (handler != null) {
            boolean cancelled = handler.itemClicked(e);

            if (!e.isCancelled()) {
                e.setCancelled(cancelled);
            }
        }
    }


}
