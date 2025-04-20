package xycm.momo.mmglobalexchanges.ui.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.ui.Chest;
import xycm.momo.mmglobalexchanges.ui.components.UIComponentFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用出售记录界面
 */
public abstract class SellRecordUI extends Chest {

    // 常量定义
    protected static final int SLOT_RETURN = 46;
    protected static final int SLOT_FIRST_PAGE = 47;
    protected static final int SLOT_PREV_PAGE = 48;
    protected static final int SLOT_NEXT_PAGE = 50;
    protected static final int SLOT_LAST_PAGE = 51;
    protected static final int SLOT_INFO = 52;
    
    // 存储玩家上一次的翻页记录
    protected final Map<String, Integer> playerPages = new HashMap<>();
    // 存储玩家返回界面的提示信息
    protected final Map<String, String> playerInfo = new HashMap<>();
    
    // 市场策略
    protected final MarketStrategy strategy;

    /**
     * 构造函数
     * @param title 界面标题
     * @param size 界面大小
     * @param strategy 市场策略
     */
    public SellRecordUI(String title, int size, MarketStrategy strategy) {
        super(title, size);
        this.strategy = strategy;
    }

    /**
     * 打开界面
     * @param player 玩家
     * @param page 页码
     */
    public void open(Player player, int page) {
        clearAllItems();
        
        // 初始化界面
        init(player, page);
        refreshItems(player, page);
        
        // 打开界面
        player.openInventory(getInventory());
    }
    
    /**
     * 初始化界面
     * @param player 玩家
     * @param page 页码
     */
    protected void init(Player player, int page) {
        addReturn();
        addPageControls(player, page);
    }
    
    /**
     * 添加返回按钮
     */
    protected void addReturn() {
        addItem(SLOT_RETURN, UIComponentFactory.createReturnButton());
    }
    
    /**
     * 刷新物品显示
     * @param player 玩家
     * @param page 页码
     */
    protected abstract void refreshItems(Player player, int page);
    
    /**
     * 添加翻页控件
     * @param player 玩家
     * @param page 当前页码
     */
    protected void addPageControls(Player player, int page) {
        // 计算最大页数
        int totalItems = getTotalItemCount(player);
        int itemsPerPage = getItemsPerPage();
        int maxPage = (totalItems <= 0) ? 1 : ((totalItems - 1) / itemsPerPage + 1);
        
        // 记录页码
        playerPages.put(player.getName(), page);
        
        // 清理按钮
        clearItem(SLOT_FIRST_PAGE);
        clearItem(SLOT_PREV_PAGE);
        clearItem(SLOT_NEXT_PAGE);
        clearItem(SLOT_LAST_PAGE);
        clearItem(SLOT_INFO);
        
        // 添加首页按钮
        if (page > 1) {
            addItem(SLOT_FIRST_PAGE, UIComponentFactory.createFirstPageButton());
        }
        
        // 添加上一页按钮
        if (page > 1) {
            addItem(SLOT_PREV_PAGE, UIComponentFactory.createPreviousPageButton());
        }
        
        // 添加下一页按钮
        if (page < maxPage) {
            addItem(SLOT_NEXT_PAGE, UIComponentFactory.createNextPageButton());
        }
        
        // 添加末页按钮
        if (page < maxPage) {
            addItem(SLOT_LAST_PAGE, UIComponentFactory.createLastPageButton());
        }
        
        // 添加信息按钮
        String info = playerInfo.get(player.getName());
        addItem(SLOT_INFO, UIComponentFactory.createPageInfoButton(page, maxPage, info));
        if (info != null) {
            playerInfo.remove(player.getName());
        }
    }
    
    /**
     * 获取当前页码
     * @param player 玩家
     * @return 页码
     */
    public int getPage(Player player) {
        return playerPages.getOrDefault(player.getName(), 1);
    }
    
    /**
     * 设置提示信息
     * @param player 玩家
     * @param info 提示信息
     */
    public void addInfo(Player player, String info) {
        playerInfo.put(player.getName(), info);
    }
    
    /**
     * 获取总物品数量
     * @param player 玩家
     * @return 物品总数
     */
    protected abstract int getTotalItemCount(Player player);
    
    /**
     * 获取每页显示的物品数量
     * @return 每页物品数
     */
    protected abstract int getItemsPerPage();
}
