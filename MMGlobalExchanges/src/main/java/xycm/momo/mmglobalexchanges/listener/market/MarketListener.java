package xycm.momo.mmglobalexchanges.listener.market;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.listener.common.AbstractMarketListener;
import xycm.momo.mmglobalexchanges.ui.common.MarketUI;
import xycm.momo.mmglobalexchanges.ui.market.LaunchRecord;
import xycm.momo.mmglobalexchanges.ui.market.Mail;
import xycm.momo.mmglobalexchanges.ui.market.Market;
import xycm.momo.mmglobalexchanges.ui.market.PersonalInfo;

import java.util.Map;

/**
 * 市场监听器
 */
public class MarketListener extends AbstractMarketListener {
    
    public MarketListener() {
        super(new NormalMarketStrategy());
    }
    
    @Override
    protected String getInventoryTitle() {
        return "市场";
    }
    
    @Override
    protected boolean handleItemClick(InventoryClickEvent event, Inventory inventory, Player player, int slot) {
        // 获取当前的市场界面
        Market market = Market.getPlayerInventory(player);
        if (market == null) {
            return false;
        }
        
        // 根据点击的槽位处理不同的功能
        switch (slot) {
            case MarketUI.SLOT_LAUNCH:
                // 上架物品
                handleLaunchItem(player);
                break;
                
            case MarketUI.SLOT_RETURN:
                // 返回按钮，此处不做处理
                break;
                
            case MarketUI.SLOT_FIRST_PAGE:
                // 首页
                market.open(player, 1);
                break;
                
            case MarketUI.SLOT_PREV_PAGE:
                // 上一页
                market.open(player, market.getPage(player) - 1);
                break;
                
            case MarketUI.SLOT_SEARCH:
                // 搜索
                handleSearch(player);
                break;
                
            case MarketUI.SLOT_NEXT_PAGE:
                // 下一页
                market.open(player, market.getPage(player) + 1);
                break;
                
            case MarketUI.SLOT_LAST_PAGE: {
                // 末页
                int totalItems = MMGlobalExchanges.marketFile.getSize(player);
                int itemsPerPage = strategy.getItemsPerPage();
                int maxPage = (totalItems <= 0) ? 1 : ((totalItems - 1) / itemsPerPage + 1);
                market.open(player, maxPage);
                break;
            }
                
            case 53:
                // 个人信息
                handlePersonalInfo(player);
                break;
                
            default:
                // 检查是否点击了商品
                if (slot >= 0 && slot < 45) {
                    handleItemPurchase(player, slot);
                }
                break;
        }
        
        return true;
    }
    
    /**
     * 处理上架物品
     */
    private void handleLaunchItem(Player player) {
        // 这里只是打开选择物品界面，之后会有专门的监听器处理上架流程
        player.closeInventory();
        player.sendMessage("§a请手持要上架的物品，然后输入价格");
    }
    
    /**
     * 处理搜索
     */
    private void handleSearch(Player player) {
        // 根据配置决定使用DragonCore或原生搜索
        if (MMGlobalExchanges.instance.getConfig().getBoolean("market_use_dragoncore")) {
            MMGlobalExchanges.instance.getSearch().open(player);
        } else {
            player.sendMessage("§a请输入要搜索的物品名称");
            player.closeInventory();
        }
    }
    
    /**
     * 处理个人信息
     */
    private void handlePersonalInfo(Player player) {
        PersonalInfo.getInstance().open(player);
    }
    
    /**
     * 处理物品购买
     */
    private void handleItemPurchase(Player player, int slot) {
        Market market = Market.getPlayerInventory(player);
        if (market == null) {
            return;
        }
        
        // 获取点击的物品
        String itemId = getItemIdFromSlot(player, slot);
        if (itemId == null) {
            return;
        }
        
        // 处理购买操作
        boolean success = strategy.handlePurchase(player, itemId);
        if (success) {
            player.sendMessage("§a购买成功，物品已发送到您的邮箱");
        }
    }
    
    /**
     * 从槽位获取物品ID
     */
    private String getItemIdFromSlot(Player player, int slot) {
        // 这里需要根据实际情况实现获取物品ID的逻辑
        // 暂时返回null作为占位
        return null;
    }
}
