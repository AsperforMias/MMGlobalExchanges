package xycm.momo.mmglobalexchanges;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xycm.momo.mmglobalexchanges.cmd.CommandTab;
import xycm.momo.mmglobalexchanges.core.blackmarket.BlackMarketStrategy;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.dragoncore.BlackSearch;
import xycm.momo.mmglobalexchanges.dragoncore.Search;
import xycm.momo.mmglobalexchanges.file.BlackMarketFile;
import xycm.momo.mmglobalexchanges.file.MarketFile;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.listener.blackmarket.*;
import xycm.momo.mmglobalexchanges.listener.market.*;
import xycm.momo.mmglobalexchanges.ui.blackmarket.*;
import xycm.momo.mmglobalexchanges.ui.market.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class MMGlobalExchanges extends JavaPlugin {

    public static MMGlobalExchanges instance;
    
    // 文件数据相关
    public static MarketFile marketFile;
    public static BlackMarketFile blackMarketFile;
    private static PlayerData playerData;
    
    // 交易相关变量
    public static Map<String, LaunchItem> launchItems;
    public static Map<String, Map<Integer, Integer>> purchase;
    public static Map<String, Integer> delist;
    public static Map<String, BlackLaunchItem> blackLaunchItems;
    public static Map<String, Map<Integer, Integer>> blackPurchase;
    public static Map<String, Integer> blackDelist;
    
    // DragonCore搜索界面
    public static xycm.momo.mmglobalexchanges.dragoncore.Search search;
    public static xycm.momo.mmglobalexchanges.dragoncore.BlackSearch blackSearch;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        // 初始化命令
        Objects.requireNonNull(Bukkit.getPluginCommand("mmge")).setExecutor(new CommandTab());
        Objects.requireNonNull(Bukkit.getPluginCommand("mmge")).setTabCompleter(new CommandTab());
        
        // 初始化数据文件
        marketFile = new MarketFile();
        blackMarketFile = new BlackMarketFile();
        playerData = new PlayerData();
        
        // 初始化交易相关变量
        launchItems = new HashMap<>();
        purchase = new HashMap<>();
        delist = new HashMap<>();
        blackLaunchItems = new HashMap<>();
        blackPurchase = new HashMap<>();
        blackDelist = new HashMap<>();
        
        // 初始化市场UI (现在使用单例模式，不需要直接创建实例)
        // 调用getInstance()方法会触发懒加载创建实例
        Market.getInstance(); 
        Mail.getInstance();
        PurchaseRecord.getInstance();
        SellRecord.getInstance();
        LaunchRecord.getInstance();
        PersonalInfo.getInstance();
        
        // 初始化黑市UI
        BlackMarket.getInstance();
        BlackMail.getInstance();
        BlackPurchaseRecord.getInstance();
        BlackSellRecord.getInstance();
        BlackLaunchRecord.getInstance();
        BlackPersonalInfo.getInstance();
        
        // 初始化DragonCore搜索界面
        search = new Search(getConfig().getString("market.search"));
        blackSearch = new BlackSearch(getConfig().getString("black_market.search"));
        
        // 注册市场事件监听器
        getServer().getPluginManager().registerEvents(new MarketListener(), this);
        getServer().getPluginManager().registerEvents(new SelectItemListener(), this);
        getServer().getPluginManager().registerEvents(new LaunchItemListener(), this);
        getServer().getPluginManager().registerEvents(new PurchaseConfirmListener(), this);
        getServer().getPluginManager().registerEvents(new PersonalInfoListener(), this);
        getServer().getPluginManager().registerEvents(new PurchaseRecordListener(), this);
        getServer().getPluginManager().registerEvents(new SellRecordListener(), this);
        getServer().getPluginManager().registerEvents(new LaunchRecordListener(), this);
        getServer().getPluginManager().registerEvents(new DelistConfirmListener(), this);
        getServer().getPluginManager().registerEvents(new SearchListener(), this);
        getServer().getPluginManager().registerEvents(new MailListener(), this);
        getServer().getPluginManager().registerEvents(new Search(), this);
        
        // 注册黑市事件监听器
        getServer().getPluginManager().registerEvents(new BlackMarketListener(), this);
        getServer().getPluginManager().registerEvents(new BlackSelectItemListener(), this);
        getServer().getPluginManager().registerEvents(new BlackLaunchItemListener(), this);
        getServer().getPluginManager().registerEvents(new BlackPurchaseConfirmListener(), this);
        getServer().getPluginManager().registerEvents(new BlackPersonalInfoListener(), this);
        getServer().getPluginManager().registerEvents(new BlackPurchaseRecordListener(), this);
        getServer().getPluginManager().registerEvents(new BlackSellRecordListener(), this);
        getServer().getPluginManager().registerEvents(new BlackLaunchRecordListener(), this);
        getServer().getPluginManager().registerEvents(new BlackDelistConfirmListener(), this);
        getServer().getPluginManager().registerEvents(new BlackSearchListener(), this);
        getServer().getPluginManager().registerEvents(new BlackMailListener(), this);
        getServer().getPluginManager().registerEvents(new BlackSearch(), this);
        
        getLogger().info("默全球交易加载成功 QQ:1756784800");
    }

    /**
     * 重载插件配置
     */
    public void reload() {
        reloadConfig();
        
        // 更新搜索UI标题
        search.setTitle(getConfig().getString("market.search"));
        blackSearch.setTitle(getConfig().getString("black_market.search"));
        
        // 由于使用了单例模式，不需要重新设置每个UI的标题
        // 如果有需要在运行时动态修改UI标题的需求，可以在UI类中添加setter方法
    }

    @Override
    public void onDisable() {
        // 检查玩家是否正在上架物品，如果是则归还物品
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTitle().equals(getConfig().getString("market.launch"))) {
                LaunchItem launchItem = launchItems.get(player.getName());
                if (launchItem != null) {
                    launchItem.returnItem(player);
                }
            }
            
            if (player.getOpenInventory().getTitle().equals(getConfig().getString("black_market.launch"))) {
                BlackLaunchItem launchItem = blackLaunchItems.get(player.getName());
                if (launchItem != null) {
                    launchItem.returnItem(player);
                }
            }
        }
    }
    
    /**
     * 获取玩家数据管理器
     * @return PlayerData实例
     */
    public PlayerData getPlayerData() {
        return playerData;
    }
    
    /**
     * 获取市场文件
     * @return MarketFile实例
     */
    public MarketFile getMarketFile() {
        return marketFile;
    }
    
    /**
     * 获取黑市文件
     * @return BlackMarketFile实例
     */
    public BlackMarketFile getBlackMarketFile() {
        return blackMarketFile;
    }
    
    /**
     * 获取市场搜索界面
     * @return Search实例
     */
    public Search getSearch() {
        return search;
    }
    
    /**
     * 获取黑市搜索界面
     * @return BlackSearch实例
     */
    public BlackSearch getBlackSearch() {
        return blackSearch;
    }
}
