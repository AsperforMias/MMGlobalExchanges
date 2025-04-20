package xycm.momo.mmglobalexchanges.ui.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.ui.components.UIComponentFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用市场界面实现 - 可同时用于普通市场和黑市
 */
public class MarketUI extends AbstractMarketUI {
    
    // 单例模式
    protected static final Map<Class<? extends MarketUI>, MarketUI> instances = new HashMap<>();
    
    // 存储玩家打开过的市场界面
    protected final Map<String, MarketUI> playerInventory = new HashMap<>();
    
    /**
     * 构造函数
     * 
     * @param strategy 市场策略
     */
    protected MarketUI(MarketStrategy strategy) {
        super(strategy.getMarketTypeName(), 54, strategy);
        instances.put(this.getClass(), this);
    }
    
    /**
     * 获取物品堆栈
     */
    @Override
    protected ItemStack getItemStackFromId(String id) {
        // 根据策略类型，获取对应的物品
        if (strategy.getMarketTypeName().equals("市场")) {
            return MMGlobalExchanges.instance.getMarketFile().getItemStack(id);
        } else {
            return MMGlobalExchanges.instance.getBlackMarketFile().getItemStack(id);
        }
    }
    
    /**
     * 获取总物品数量
     */
    @Override
    protected int getTotalItemCount(Player player) {
        if (strategy.getMarketTypeName().equals("市场")) {
            return MMGlobalExchanges.instance.getMarketFile().getSize(player);
        } else {
            return MMGlobalExchanges.instance.getBlackMarketFile().getSize(player);
        }
    }
    
    /**
     * 打开市场界面
     * 
     * @param player 玩家
     * @param page 页码
     */
    public void open(Player player, int page) {
        // 清空界面
        clearAllItems();
        
        // 初始化界面元素
        addLaunch();
        addSearch();
        addPersonalInfo(player);
        addReturn(player);
        
        // 添加翻页控件
        addPageControls(player, page);
        
        // 添加关闭按钮
        addCloseButton();
        
        // 刷新物品显示
        refreshItems(player, page);
        
        // 打开界面
        player.openInventory(getInventory());
        
        // 记录打开的界面
        playerInventory.put(player.getName(), this);
    }
    
    /**
     * 添加上架按钮
     */
    @Override
    protected void addLaunch() {
        addItem(SLOT_LAUNCH, UIComponentFactory.createLaunchButton(strategy.getMarketTypeName()));
    }
    
    /**
     * 添加搜索按钮
     */
    @Override
    protected void addSearch() {
        addItem(SLOT_SEARCH, UIComponentFactory.createSearchButton(strategy.getMarketTypeName()));
    }
    
    /**
     * 添加个人信息按钮
     */
    protected void addPersonalInfo(Player player) {
        addItem(53, UIComponentFactory.createPersonalInfoButton());
    }
    
    /**
     * 添加关闭按钮
     */
    @Override
    protected void addCloseButton() {
        addItem(SLOT_CLOSE, UIComponentFactory.createCloseButton());
    }
    
    /**
     * 根据玩家名称获取市场界面实例
     * 
     * @param player 玩家
     * @return 市场界面实例
     */
    public static <T extends MarketUI> T getPlayerInventory(Player player, Class<T> clazz) {
        MarketUI ui = instances.get(clazz);
        if (ui != null) {
            return (T) ui.playerInventory.get(player.getName());
        }
        return null;
    }
}
