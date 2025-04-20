package xycm.momo.mmglobalexchanges.core.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * 交易市场策略接口 - 定义市场和黑市的共同行为和差异化处理点
 */
public interface MarketStrategy {
    /**
     * 获取市场类型名称
     */
    String getMarketTypeName();
    
    /**
     * 格式化物品显示，处理物品名称、描述等
     * 
     * @param item 原始物品
     * @param sellerName 卖家名称
     * @param itemId 物品ID
     * @param price 价格
     * @return 格式化后的物品
     */
    ItemStack formatItemDisplay(ItemStack item, String sellerName, String itemId, double price);
    
    /**
     * 获取物品展示的Lore描述
     * 
     * @param item 物品
     * @param sellerName 卖家名称
     * @param itemId 物品ID
     * @param price 价格
     * @return 物品的描述列表
     */
    List<String> getItemLore(ItemStack item, String sellerName, String itemId, double price);
    
    /**
     * 格式化价格显示
     * 
     * @param price 价格
     * @return 格式化后的价格字符串
     */
    String formatPriceDisplay(double price);
    
    /**
     * 获取市场或黑市的数据
     * 
     * @return 市场数据
     */
    Map<String, Object> getMarketData();
    
    /**
     * 获取物品列表
     * 
     * @param player 玩家
     * @param page 页码
     * @param filter 过滤词
     * @return 物品ID列表
     */
    List<String> getItemList(Player player, int page, String filter);
    
    /**
     * 处理物品购买
     * 
     * @param player 玩家
     * @param itemId 物品ID
     * @return 是否购买成功
     */
    boolean handlePurchase(Player player, String itemId);
    
    /**
     * 处理物品上架
     * 
     * @param player 玩家
     * @param item 物品
     * @param price 价格
     * @return 是否上架成功
     */
    boolean handleLaunch(Player player, ItemStack item, double price);
    
    /**
     * 处理物品下架
     * 
     * @param player 玩家
     * @param itemId 物品ID
     * @return 是否下架成功
     */
    boolean handleDelist(Player player, String itemId);
    
    /**
     * 获取每页显示的最大物品数量
     * 
     * @return 每页最大物品数
     */
    int getItemsPerPage();
}
