package xycm.momo.mmglobalexchanges.ui.blackmarket;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.blackmarket.BlackMarketStrategy;
import xycm.momo.mmglobalexchanges.file.History;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.ui.common.SellRecordUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 黑市出售记录界面
 */
public class BlackSellRecord extends SellRecordUI {
    // 单例实例
    private static BlackSellRecord instance;
    
    /**
     * 私有构造函数
     */
    private BlackSellRecord() {
        super("黑市出售记录", 54, new BlackMarketStrategy());
    }
    
    /**
     * 获取实例
     * @return BlackSellRecord实例
     */
    public static BlackSellRecord getInstance() {
        if (instance == null) {
            instance = new BlackSellRecord();
        }
        return instance;
    }
    
    @Override
    protected void refreshItems(Player player, int page) {
        // 获取玩家数据
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        History history = playerData.getBlackMarketHistory(player.getName());
        
        // 清除展示区域
        for (int i = 0; i < 45; i++) {
            clearItem(i);
        }
        
        if (history == null) {
            return;
        }
        
        // 获取出售历史
        Map<String, Object> sellHistory = history.getSell();
        
        if (sellHistory == null || sellHistory.isEmpty()) {
            return;
        }
        
        // 获取ID列表
        List<String> itemIds = new ArrayList<>(sellHistory.keySet());
        
        // 计算分页
        int itemsPerPage = getItemsPerPage();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, itemIds.size());
        
        // 显示记录，黑市特殊处理：隐藏买家信息
        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String id = itemIds.get(i);
            Map<String, Object> recordData = (Map<String, Object>) sellHistory.get(id);
            
            // 获取物品
            ItemStack item = MMGlobalExchanges.blackMarketFile.getHistoryItemStack(id);
            if (item != null) {
                String brief = (String) recordData.get("brief");
                String detail = (String) recordData.get("detail");
                
                // 更新物品信息
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("");
                lore.add("§f出售: " + detail);
                lore.add("§f时间: " + brief);
                lore.add("§f购买者: ***"); // 黑市特性：隐藏购买者信息
                lore.add("§f商品唯一ID: " + id);
                lore.add("§f价格: " + history.getPrice(id) + " 时息");
                meta.setLore(lore);
                item.setItemMeta(meta);
                
                // 添加到界面
                addItem(slot++, item);
                
                if (slot >= getItemsPerPage()) break;
            }
        }
    }
    
    @Override
    protected int getTotalItemCount(Player player) {
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        History history = playerData.getBlackMarketHistory(player.getName());
        
        if (history == null || history.getSell() == null) {
            return 0;
        }
        
        return history.getSell().size();
    }
    
    @Override
    protected int getItemsPerPage() {
        return 45; // 每页显示45个记录
    }
}
