package xycm.momo.mmglobalexchanges.ui.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.ui.Chest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易市场抽象基类 - 封装市场和黑市共有的UI行为
 */
public abstract class AbstractMarketUI extends Chest {
    // 常量定义，避免硬编码
    public static final int SLOT_LAUNCH = 45;
    public static final int SLOT_RETURN = 46;
    public static final int SLOT_FIRST_PAGE = 47;
    public static final int SLOT_PREV_PAGE = 48;
    public static final int SLOT_SEARCH = 49;
    public static final int SLOT_NEXT_PAGE = 50;
    public static final int SLOT_LAST_PAGE = 51;
    public static final int SLOT_INFO = 52;
    public static final int SLOT_CLOSE = 53;
    
    // 存储玩家上一次的翻页记录
    protected final Map<String, Integer> playerPages = new HashMap<>();
    // 存储玩家返回界面的提示信息
    protected final Map<String, String> playerInfo = new HashMap<>();
    // 存储玩家搜索的过滤词
    protected final Map<String, String> playerFilter = new HashMap<>();
    
    // 市场策略对象
    protected final MarketStrategy strategy;
    
    /**
     * 构造函数
     * @param title 界面标题
     * @param size 界面大小
     * @param strategy 市场策略
     */
    public AbstractMarketUI(String title, int size, MarketStrategy strategy) {
        super(title, size);
        this.strategy = strategy;
    }
    
    /**
     * 刷新物品显示
     * @param player 玩家对象
     * @param page 页码
     */
    public void refreshItems(Player player, int page) {
        // 获取过滤词
        String filter = getFilter(player);
        
        // 获取此页显示的物品ID列表
        List<String> itemIds = strategy.getItemList(player, page, filter);
        
        // 清除之前的物品显示区
        for (int i = 0; i < 45; i++) {
            clearItem(i);
        }
        
        // 显示物品
        Map<String, Object> marketData = strategy.getMarketData();
        int index = 0;
        
        for (String id : itemIds) {
            if (index >= 45) break; // 确保不超过界面容量
            
            Map<String, Object> itemData = (Map<String, Object>) marketData.get(id);
            if (itemData != null) {
                String sellerName = (String) itemData.get("seller");
                double price = (double) itemData.get("price");
                ItemStack item = getItemStackFromId(id);
                
                if (item != null) {
                    ItemStack displayItem = strategy.formatItemDisplay(item, sellerName, id, price);
                    addItem(index++, displayItem);
                }
            }
        }
    }
    
    /**
     * 根据ID获取物品
     * @param id 物品ID
     * @return 物品堆
     */
    protected abstract ItemStack getItemStackFromId(String id);
    
    /**
     * 添加翻页按钮和信息
     * @param player 玩家对象
     * @param page 当前页码
     */
    protected void addPageControls(Player player, int page) {
        // 计算最大页数
        int totalItems = getTotalItemCount(player);
        int itemsPerPage = strategy.getItemsPerPage();
        int maxPage = (totalItems <= 0) ? 1 : ((totalItems - 1) / itemsPerPage + 1);
        
        // 当玩家翻页时，记录翻页
        playerPages.put(player.getName(), page);
        
        // 清理这些位置上的物品
        clearItem(SLOT_FIRST_PAGE);
        clearItem(SLOT_PREV_PAGE);
        clearItem(SLOT_NEXT_PAGE);
        clearItem(SLOT_LAST_PAGE);
        clearItem(SLOT_INFO);
        
        // 首页
        if (page > 1) {
            ItemStack firstItem = new ItemStack(Material.ARROW);
            ItemMeta firstMeta = firstItem.getItemMeta();
            firstMeta.setDisplayName("§e首页");
            firstItem.setItemMeta(firstMeta);
            addItem(SLOT_FIRST_PAGE, firstItem);
        }
        
        // 上一页
        if (page > 1) {
            ItemStack prevItem = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevItem.getItemMeta();
            prevMeta.setDisplayName("§e上一页");
            prevItem.setItemMeta(prevMeta);
            addItem(SLOT_PREV_PAGE, prevItem);
        }
        
        // 下一页
        if (page < maxPage) {
            ItemStack nextItem = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextItem.getItemMeta();
            nextMeta.setDisplayName("§e下一页");
            nextItem.setItemMeta(nextMeta);
            addItem(SLOT_NEXT_PAGE, nextItem);
        }
        
        // 末页
        if (page < maxPage) {
            ItemStack lastItem = new ItemStack(Material.ARROW);
            ItemMeta lastMeta = lastItem.getItemMeta();
            lastMeta.setDisplayName("§e末页");
            lastItem.setItemMeta(lastMeta);
            addItem(SLOT_LAST_PAGE, lastItem);
        }
        
        // 显示当前页码信息
        ItemStack infoItem = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§f当前页数: " + page + "/" + maxPage);
        List<String> infoLore = new ArrayList<>();
        
        // 如果有额外提示信息，添加到lore中
        String info = playerInfo.get(player.getName());
        if (info != null && !info.isEmpty()) {
            infoLore.add(info);
            playerInfo.remove(player.getName());
        }
        
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        addItem(SLOT_INFO, infoItem);
    }
    
    /**
     * 获取总物品数量
     * @param player 玩家对象
     * @return 总物品数
     */
    protected abstract int getTotalItemCount(Player player);
    
    /**
     * 添加返回按钮
     * @param player 玩家对象
     */
    protected void addReturn(Player player) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c返回上一级");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击返回上一级");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_RETURN, item);
    }
    
    /**
     * 添加搜索按钮
     */
    protected void addSearch() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e搜索");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击搜索" + strategy.getMarketTypeName() + "物品");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_SEARCH, item);
    }
    
    /**
     * 添加上架按钮
     */
    protected void addLaunch() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a上架物品");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击上架物品到" + strategy.getMarketTypeName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_LAUNCH, item);
    }
    
    /**
     * 添加提示信息
     * @param player 玩家对象
     * @param info 提示信息
     */
    public void addInfo(Player player, String info) {
        playerInfo.put(player.getName(), info);
    }
    
    /**
     * 添加关闭按钮
     */
    protected void addCloseButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c关闭");
        item.setItemMeta(meta);
        addItem(SLOT_CLOSE, item);
    }
    
    /**
     * 设置过滤词
     * @param player 玩家对象
     * @param filter 过滤词
     */
    public void setFilter(Player player, String filter) {
        if (filter == null || filter.isEmpty()) {
            playerFilter.remove(player.getName());
        } else {
            playerFilter.put(player.getName(), filter);
        }
    }
    
    /**
     * 获取过滤词
     * @param player 玩家对象
     * @return 过滤词
     */
    public String getFilter(Player player) {
        return playerFilter.getOrDefault(player.getName(), "");
    }
    
    /**
     * 获取页码
     * @param player 玩家对象
     * @return 页码
     */
    public int getPage(Player player) {
        return playerPages.getOrDefault(player.getName(), 1);
    }
    
    /**
     * 设置页码
     * @param player 玩家对象
     * @param page 页码
     */
    public void setPage(Player player, int page) {
        playerPages.put(player.getName(), page);
    }
    
    /**
     * 获取策略
     * @return 市场策略
     */
    public MarketStrategy getStrategy() {
        return strategy;
    }
}
