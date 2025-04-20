package xycm.momo.mmglobalexchanges.ui.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.ui.common.MailUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 市场邮件界面
 */
public class Mail extends MailUI {
    // 单例实例
    private static Mail instance;
    
    /**
     * 私有构造函数
     */
    private Mail() {
        super("市场邮件", 54, new NormalMarketStrategy());
    }
    
    /**
     * 获取实例
     * @return Mail实例
     */
    public static Mail getInstance() {
        if (instance == null) {
            instance = new Mail();
        }
        return instance;
    }
    
    @Override
    protected void refreshItems(Player player, int page) {
        // 获取玩家数据
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        Map<String, Object> mailMap = playerData.getMarketMail(player.getName());
        
        // 清除展示区域
        for (int i = 0; i < 45; i++) {
            clearItem(i);
        }
        
        if (mailMap == null || mailMap.isEmpty()) {
            return;
        }
        
        // 获取邮件ID列表
        List<String> mailIds = new ArrayList<>(mailMap.keySet());
        
        // 计算分页
        int itemsPerPage = getItemsPerPage();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, mailIds.size());
        
        // 显示邮件
        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            String id = mailIds.get(i);
            Map<String, Object> mailData = (Map<String, Object>) mailMap.get(id);
            
            // 创建邮件物品
            ItemStack mailItem;
            if (mailData.containsKey("item")) {
                // 如果是物品邮件
                mailItem = (ItemStack) mailData.get("item");
                ItemMeta meta = mailItem.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("");
                lore.add("§f邮件ID: " + id);
                lore.add("§f来源: " + mailData.get("source"));
                lore.add("§f时间: " + mailData.get("time"));
                meta.setLore(lore);
                mailItem.setItemMeta(meta);
            } else if (mailData.containsKey("money")) {
                // 如果是金钱邮件
                mailItem = new ItemStack(org.bukkit.Material.GOLD_INGOT);
                ItemMeta meta = mailItem.getItemMeta();
                meta.setDisplayName("§6" + mailData.get("money") + " 时息");
                List<String> lore = new ArrayList<>();
                lore.add("§f邮件ID: " + id);
                lore.add("§f来源: " + mailData.get("source"));
                lore.add("§f时间: " + mailData.get("time"));
                meta.setLore(lore);
                mailItem.setItemMeta(meta);
            } else {
                continue;
            }
            
            // 添加到界面
            addItem(slot, mailItem);
            
            // 添加领取按钮
            addItem(slot + 9, createPickButton(id));
            
            slot++;
            if (slot >= 9) break; // 最多显示9个邮件
        }
    }
    
    @Override
    protected int getTotalItemCount(Player player) {
        // 获取玩家邮件总数
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        Map<String, Object> mailMap = playerData.getMarketMail(player.getName());
        
        return mailMap != null ? mailMap.size() : 0;
    }
    
    @Override
    protected int getItemsPerPage() {
        return 9; // 每页显示9个邮件
    }
}
