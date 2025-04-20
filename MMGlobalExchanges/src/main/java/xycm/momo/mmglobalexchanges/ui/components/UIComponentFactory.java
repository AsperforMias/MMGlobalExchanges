package xycm.momo.mmglobalexchanges.ui.components;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * UI组件工厂 - 提供各种通用UI元素的创建方法
 */
public class UIComponentFactory {
    
    /**
     * 创建返回按钮
     * @return 返回按钮物品
     */
    public static ItemStack createReturnButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c返回上一级");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击返回上一级");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建上架按钮
     * @param marketName 市场名称
     * @return 上架按钮物品
     */
    public static ItemStack createLaunchButton(String marketName) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a上架物品");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击上架物品到" + marketName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建搜索按钮
     * @param marketName 市场名称
     * @return 搜索按钮物品
     */
    public static ItemStack createSearchButton(String marketName) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e搜索");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击搜索" + marketName + "物品");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建个人信息按钮
     * @return 个人信息按钮物品
     */
    public static ItemStack createPersonalInfoButton() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f个人信息");
        List<String> lore = new ArrayList<>();
        lore.add("§7点击查看个人信息");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建首页按钮
     * @return 首页按钮物品
     */
    public static ItemStack createFirstPageButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e首页");
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建上一页按钮
     * @return 上一页按钮物品
     */
    public static ItemStack createPreviousPageButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e上一页");
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建下一页按钮
     * @return 下一页按钮物品
     */
    public static ItemStack createNextPageButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e下一页");
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建末页按钮
     * @return 末页按钮物品
     */
    public static ItemStack createLastPageButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e末页");
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建信息页面按钮
     * @param currentPage 当前页码
     * @param maxPage 最大页数
     * @param extraInfo 额外信息
     * @return 信息页面按钮物品
     */
    public static ItemStack createPageInfoButton(int currentPage, int maxPage, String extraInfo) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f当前页数: " + currentPage + "/" + maxPage);
        
        if (extraInfo != null && !extraInfo.isEmpty()) {
            List<String> lore = new ArrayList<>();
            lore.add(extraInfo);
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 创建关闭按钮
     * @return 关闭按钮物品
     */
    public static ItemStack createCloseButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c关闭");
        item.setItemMeta(meta);
        return item;
    }
}
