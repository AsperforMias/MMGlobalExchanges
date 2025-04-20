package xycm.momo.mmglobalexchanges.ui.blackmarket;

import org.bukkit.entity.Player;
import xycm.momo.mmglobalexchanges.core.blackmarket.BlackMarketStrategy;
import xycm.momo.mmglobalexchanges.ui.common.MarketUI;

/**
 * 黑市界面实现
 */
public class BlackMarket extends MarketUI {
    
    // 单例实例
    private static BlackMarket instance;
    
    /**
     * 私有构造函数 - 使用单例模式
     */
    private BlackMarket() {
        super(new BlackMarketStrategy());
    }
    
    /**
     * 获取黑市实例
     * @return 黑市单例
     */
    public static BlackMarket getInstance() {
        if (instance == null) {
            instance = new BlackMarket();
        }
        return instance;
    }
    
    /**
     * 获取玩家打开的黑市界面
     * @param player 玩家
     * @return 黑市界面实例
     */
    public static BlackMarket getPlayerInventory(Player player) {
        return MarketUI.getPlayerInventory(player, BlackMarket.class);
    }
}
