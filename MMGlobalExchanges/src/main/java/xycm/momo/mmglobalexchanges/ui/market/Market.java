package xycm.momo.mmglobalexchanges.ui.market;

import org.bukkit.entity.Player;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.ui.common.MarketUI;

/**
 * 普通市场界面实现
 */
public class Market extends MarketUI {
    
    // 单例实例
    private static Market instance;
    
    /**
     * 私有构造函数 - 使用单例模式
     */
    private Market() {
        super(new NormalMarketStrategy());
    }
    
    /**
     * 获取市场实例
     * @return 市场单例
     */
    public static Market getInstance() {
        if (instance == null) {
            instance = new Market();
        }
        return instance;
    }
    
    /**
     * 获取玩家打开的市场界面
     * @param player 玩家
     * @return 市场界面实例
     */
    public static Market getPlayerInventory(Player player) {
        return MarketUI.getPlayerInventory(player, Market.class);
    }
}
