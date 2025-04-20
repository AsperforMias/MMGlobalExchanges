package xycm.momo.mmglobalexchanges.core.blackmarket;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.file.BlackMarketFile;

import java.util.*;

/**
 * 黑市策略实现
 */
public class BlackMarketStrategy implements MarketStrategy {
    private final BlackMarketFile blackMarketFile;
    private final String hiddenChars;
    
    public BlackMarketStrategy() {
        this.blackMarketFile = MMGlobalExchanges.instance.getBlackMarketFile();
        this.hiddenChars = MMGlobalExchanges.instance.getConfig().getString("black_market_hidden_char", "？");
    }
    
    @Override
    public String getMarketTypeName() {
        return "黑市";
    }
    
    @Override
    public ItemStack formatItemDisplay(ItemStack item, String sellerName, String itemId, double price) {
        ItemStack displayItem = item.clone();
        ItemMeta meta = displayItem.getItemMeta();
        
        if (meta != null) {
            // 黑市特殊处理 - 隐藏物品名称中的一部分信息
            String itemName = meta.getDisplayName();
            if (itemName != null && !itemName.isEmpty()) {
                meta.setDisplayName(hideInformation(itemName));
            }
            
            List<String> lore = getItemLore(item, sellerName, itemId, price);
            meta.setLore(lore);
            displayItem.setItemMeta(meta);
        }
        
        return displayItem;
    }
    
    @Override
    public List<String> getItemLore(ItemStack item, String sellerName, String itemId, double price) {
        List<String> originalLore = new ArrayList<>();
        List<String> processedLore = new ArrayList<>();
        
        // 获取原始lore并处理
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore()) {
            originalLore = meta.getLore();
            
            // 黑市特殊处理 - 隐藏lore中的部分信息
            for (String line : originalLore) {
                processedLore.add(hideInformation(line));
            }
        }
        
        // 添加市场信息
        processedLore.add("");
        processedLore.add("§f卖家: " + hideSellerName(sellerName));
        processedLore.add("§f价格: " + formatPriceDisplay(price));
        processedLore.add("§f商品唯一ID: " + itemId);
        processedLore.add("");
        processedLore.add("§a点击购买");
        
        return processedLore;
    }
    
    /**
     * 隐藏信息，将部分文字替换为随机字符
     */
    private String hideInformation(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 将文本按照颜色代码进行分割
        String[] split = text.split("§");
        Random r = new Random();
        
        // 对每个部分进行处理
        for (int split_i = 1; split_i < split.length; split_i += 2) {
            // 如果分隔后的索引不存在则跳过
            if (split_i < split.length) {
                StringBuilder sb = new StringBuilder();
                for (int r_i = 0; r_i < split[split_i].length(); r_i++) {
                    sb.append(hiddenChars.charAt(r.nextInt(hiddenChars.length())));
                }
                split[split_i] = sb.toString();
            }
        }
        
        // 重新拼接文本
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i > 0) {
                result.append("§");
            }
            result.append(split[i]);
        }
        
        return result.toString();
    }
    
    /**
     * 隐藏卖家名称
     */
    private String hideSellerName(String sellerName) {
        if (sellerName == null || sellerName.isEmpty()) {
            return "Unknown";
        }
        
        // 替换为固定数量的*号
        return "***";
    }
    
    @Override
    public String formatPriceDisplay(double price) {
        return price + " 时息";
    }
    
    @Override
    public Map<String, Object> getMarketData() {
        return blackMarketFile.getData();
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
        return blackMarketFile.purchase(player, itemId);
    }
    
    @Override
    public boolean handleLaunch(Player player, ItemStack item, double price) {
        return blackMarketFile.launch(player, item, price);
    }
    
    @Override
    public boolean handleDelist(Player player, String itemId) {
        return blackMarketFile.delist(player, itemId);
    }
    
    @Override
    public int getItemsPerPage() {
        return MMGlobalExchanges.instance.getConfig().getInt("black_market_max", 45);
    }
}
