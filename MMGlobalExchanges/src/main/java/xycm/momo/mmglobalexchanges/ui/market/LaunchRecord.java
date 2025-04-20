package xycm.momo.mmglobalexchanges.ui.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.file.MarketFile;
import xycm.momo.mmglobalexchanges.ui.common.LaunchRecordUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 普通市场上架记录界面
 */
public class LaunchRecord extends LaunchRecordUI {
    // 单例实例
    private static LaunchRecord instance;
    
    /**
     * 私有构造函数
     */
    private LaunchRecord() {
        super("上架记录", 54, new NormalMarketStrategy());
    }
    
    /**
     * 获取实例
     * @return LaunchRecord实例
     */
    public static LaunchRecord getInstance() {
        if (instance == null) {
            instance = new LaunchRecord();
        }
        return instance;
    }
    
    @Override
    protected void refreshItems(Player player, int page) {
        // 获取玩家的上架记录
        MarketFile marketFile = MMGlobalExchanges.instance.getMarketFile();
        Map<String, Object> marketData = marketFile.getData();
        
        // 清除展示区域
        for (int i = 0; i < 45; i++) {
            clearItem(i);
        }
        
        // 获取该玩家的上架物品
        List<String> playerItems = new ArrayList<>();
        for (String id : marketData.keySet()) {
            Map<String, Object> item = (Map<String, Object>) marketData.get(id);
            if (player.getName().equals(item.get("seller"))) {
                playerItems.add(id);
            }
        }
        
        // 计算分页
        int itemsPerPage = getItemsPerPage();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, playerItems.size());
        
        // 显示物品
        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String id = playerItems.get(i);
            Map<String, Object> itemData = (Map<String, Object>) marketData.get(id);
            ItemStack item = marketFile.getItemStack(id);
            
            if (item != null) {
                // 添加物品信息
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("");
                lore.add("§f价格: " + itemData.get("price") + " 时息");
                lore.add("§f物品ID: " + id);
                meta.setLore(lore);
                item.setItemMeta(meta);
                
                // 添加到界面
                addItem(slot, item);
                
                // 添加下架按钮
                addItem(slot + 9, createDelistButton(id));
                
                slot++;
                if (slot >= 9) break; // 最多显示9个物品
            }
        }
    }
    
    @Override
    protected int getTotalItemCount(Player player) {
        // 获取玩家上架的物品总数
        MarketFile marketFile = MMGlobalExchanges.instance.getMarketFile();
        Map<String, Object> marketData = marketFile.getData();
        
        int count = 0;
        for (String id : marketData.keySet()) {
            Map<String, Object> item = (Map<String, Object>) marketData.get(id);
            if (player.getName().equals(item.get("seller"))) {
                count++;
            }
        }
        
        return count;
    }
    
    @Override
    protected int getItemsPerPage() {
        return 9; // 每页显示9个物品
    }
}
