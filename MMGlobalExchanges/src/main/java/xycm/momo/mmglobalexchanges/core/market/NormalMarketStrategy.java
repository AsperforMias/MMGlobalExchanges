package xycm.momo.mmglobalexchanges.core.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.file.MarketFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 普通市场策略实现
 */
public class NormalMarketStrategy implements MarketStrategy {
    private final MarketFile marketFile;
    
    public NormalMarketStrategy() {
        this.marketFile = MMGlobalExchanges.instance.getMarketFile();
    }
    
    @Override
    public String getMarketTypeName() {
        return "市场";
    }
    
    @Override
    public ItemStack formatItemDisplay(ItemStack item, String sellerName, String itemId, double price) {
        ItemStack displayItem = item.clone();
        ItemMeta meta = displayItem.getItemMeta();
        
        if (meta != null) {
            List<String> lore = getItemLore(item, sellerName, itemId, price);
            meta.setLore(lore);
            displayItem.setItemMeta(meta);
        }
        
        return displayItem;
    }

    @Override
    public List<String> getItemLore(ItemStack item, String sellerName, String itemId, double price) {
        List<String> lore = new ArrayList<>();
        
        // 添加原有的lore
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore()) {
            lore.addAll(meta.getLore());
        }
        
        // 添加市场信息
        lore.add("");
        lore.add("§f卖家: " + sellerName);
        lore.add("§f价格: " + formatPriceDisplay(price));
        lore.add("§f商品唯一ID: " + itemId);
        lore.add("");
        lore.add("§a点击购买");
        
        return lore;
    }
    
    @Override
    public String formatPriceDisplay(double price) {
        return price + " 时息";
    }
    
    @Override
    public Map<String, Object> getMarketData() {
        return marketFile.getData();
    }
    
    @Override
    public List<String> getItemList(Player player, int page, String filter) {
        Map<String, Object> market = getMarketData();
        List<String> result = new ArrayList<>();
        
        int perPage = getItemsPerPage();
        int startIndex = (page - 1) * perPage;
        int endIndex = Math.min(startIndex + perPage, market.size());
        int currentIndex = 0;
        
        // 获取并过滤商品列表
        for (String id : market.keySet()) {
            Map<String, Object> itemData = (Map<String, Object>) market.get(id);
            String itemName = (String) itemData.get("name");
            
            // 如果有过滤词，且物品名不包含过滤词，则跳过
            if (filter != null && !filter.isEmpty() && 
                (itemName == null || !itemName.toLowerCase().contains(filter.toLowerCase()))) {
                continue;
            }
            
            if (currentIndex >= startIndex && currentIndex < endIndex) {
                result.add(id);
            }
            
            currentIndex++;
            
            // 如果已经达到了页面上限，就不再继续遍历
            if (currentIndex >= endIndex && result.size() >= perPage) {
                break;
            }
        }
        
        return result;
    }
    
    @Override
    public boolean handlePurchase(Player player, String itemId) {
        return marketFile.purchase(player, itemId);
    }
    
    @Override
    public boolean handleLaunch(Player player, ItemStack item, double price) {
        return marketFile.launch(player, item, price);
    }
    
    @Override
    public boolean handleDelist(Player player, String itemId) {
        return marketFile.delist(player, itemId);
    }
    
    @Override
    public int getItemsPerPage() {
        return MMGlobalExchanges.instance.getConfig().getInt("market_max", 45);
    }
}
