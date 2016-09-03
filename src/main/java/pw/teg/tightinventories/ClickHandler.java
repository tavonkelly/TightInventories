package pw.teg.tightinventories;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickHandler {

    boolean itemClicked(InventoryClickEvent event);

}
