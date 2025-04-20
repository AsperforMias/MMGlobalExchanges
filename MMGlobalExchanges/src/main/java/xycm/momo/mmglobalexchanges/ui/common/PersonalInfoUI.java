package xycm.momo.mmglobalexchanges.ui.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.ui.Chest;
import xycm.momo.mmglobalexchanges.ui.components.UIComponentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用个人信息界面
 */
public abstract class PersonalInfoUI extends Chest {
    
    // 常量定义
    protected static final int SLOT_RETURN = 46;
    protected static final int SLOT_LAUNCH_RECORD = 11;
    protected static final int SLOT_MAIL = 13;
    protected static final int SLOT_PURCHASE_RECORD = 15;
    protected static final int SLOT_SELL_RECORD = 31;
    
    // 市场策略
    protected final MarketStrategy strategy;
    
    /**
     * 构造函数
     * @param title 界面标题
     * @param size 界面大小
     * @param strategy 市场策略
     */
    public PersonalInfoUI(String title, int size, MarketStrategy strategy) {
        super(title, size);
        this.strategy = strategy;
    }
    
    /**
     * 打开界面
     * @param player 玩家
     */
    public void open(Player player) {
        clearAllItems();
        
        // 初始化界面
        init(player);
        
        // 打开界面
        player.openInventory(getInventory());
    }
    
    /**
     * 初始化界面
     * @param player 玩家
     */
    protected void init(Player player) {
        // 添加返回按钮
        addItem(SLOT_RETURN, UIComponentFactory.createReturnButton());
        
        // 添加上架记录按钮
        addLaunchRecordButton();
        
        // 添加邮件按钮
        addMailButton();
        
        // 添加购买记录按钮
        addPurchaseRecordButton();
        
        // 添加出售记录按钮
        addSellRecordButton();
        
        // 添加玩家信息
        addPlayerInfo(player);
    }
    
    /**
     * 添加上架记录按钮
     */
    protected void addLaunchRecordButton() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a上架记录");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击查看上架记录");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_LAUNCH_RECORD, item);
    }
    
    /**
     * 添加邮件按钮
     */
    protected void addMailButton() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a邮箱");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击查看邮箱");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_MAIL, item);
    }
    
    /**
     * 添加购买记录按钮
     */
    protected void addPurchaseRecordButton() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a购买记录");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击查看购买记录");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_PURCHASE_RECORD, item);
    }
    
    /**
     * 添加出售记录按钮
     */
    protected void addSellRecordButton() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a出售记录");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击查看出售记录");
        meta.setLore(lore);
        item.setItemMeta(meta);
        addItem(SLOT_SELL_RECORD, item);
    }
    
    /**
     * 添加玩家信息
     * @param player 玩家
     */
    protected abstract void addPlayerInfo(Player player);
    
    /**
     * 打开上架记录界面
     * @param player 玩家
     */
    public abstract void openLaunchRecord(Player player);
    
    /**
     * 打开邮件界面
     * @param player 玩家
     */
    public abstract void openMail(Player player);
    
    /**
     * 打开购买记录界面
     * @param player 玩家
     */
    public abstract void openPurchaseRecord(Player player);
    
    /**
     * 打开出售记录界面
     * @param player 玩家
     */
    public abstract void openSellRecord(Player player);
}
