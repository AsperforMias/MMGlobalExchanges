package xycm.momo.mmglobalexchanges.listener.common;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;

/**
 * 交易市场监听器抽象基类
 */
public abstract class AbstractMarketListener implements Listener {
    
    // 市场策略
    protected final MarketStrategy strategy;
    
    /**
     * 构造函数
     * @param strategy 市场策略
     */
    public AbstractMarketListener(MarketStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * 处理物品点击事件
     * @param event 事件对象
     * @param inventory 物品所在的背包
     * @param player 玩家
     * @param slot 点击的槽位
     * @return 是否已处理（如果返回true则事件会被取消）
     */
    protected abstract boolean handleItemClick(InventoryClickEvent event, Inventory inventory, Player player, int slot);
    
    /**
     * 获取界面标题
     * @return 界面标题
     */
    protected abstract String getInventoryTitle();
    
    /**
     * 背包点击事件处理器
     * @param event 事件对象
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // 检查是否是玩家点击
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        
        // 检查点击的是否是该监听器监听的背包
        if (inventory == null || !event.getView().getTitle().equals(getInventoryTitle())) {
            return;
        }
        
        // 获取点击的槽位
        int slot = event.getRawSlot();
        
        // 如果点击的是玩家背包，不处理
        if (slot >= inventory.getSize()) {
            return;
        }
        
        // 处理点击事件
        if (handleItemClick(event, inventory, player, slot)) {
            event.setCancelled(true);
        }
    }
}
