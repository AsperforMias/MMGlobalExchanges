package xycm.momo.mmglobalexchanges.listener.common;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import xycm.momo.mmglobalexchanges.core.common.MarketStrategy;
import xycm.momo.mmglobalexchanges.ui.common.MailUI;

import java.util.Map;

/**
 * 邮件监听器抽象基类
 */
public abstract class AbstractMailListener implements Listener {

    // 市场策略
    protected final MarketStrategy strategy;
    
    /**
     * 构造函数
     * @param strategy 市场策略
     */
    public AbstractMailListener(MarketStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * 获取邮件界面标题
     * @return 界面标题
     */
    protected abstract String getMailTitle();
    
    /**
     * 获取邮件数据
     * @param playerName 玩家名称
     * @return 邮件数据
     */
    protected abstract Map<String, Object> getMailData(String playerName);
    
    /**
     * 处理领取邮件
     * @param player 玩家
     * @param mailId 邮件ID
     * @return 是否成功领取
     */
    protected abstract boolean handlePickMail(Player player, String mailId);
    
    /**
     * 处理一键领取
     * @param player 玩家
     * @return 成功领取的邮件数量
     */
    protected abstract int handlePickAll(Player player);
    
    /**
     * 背包点击事件处理
     * @param event 事件对象
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // 检查是否是玩家点击
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        
        // 检查点击的是否是该监听器监听的背包
        if (inventory == null || !event.getView().getTitle().equals(getMailTitle())) {
            return;
        }
        
        // 获取点击的槽位
        int slot = event.getRawSlot();
        
        // 如果点击的是玩家背包，不处理
        if (slot >= inventory.getSize()) {
            return;
        }
        
        // 根据点击的槽位处理不同的功能
        switch (slot) {
            case MailUI.SLOT_PICK_ALL:
                // 一键领取
                int count = handlePickAll(player);
                if (count > 0) {
                    player.sendMessage("§a成功领取了 " + count + " 个邮件");
                    refreshMailUI(player, 1);
                } else {
                    player.sendMessage("§c没有可领取的邮件");
                }
                break;
                
            case MailUI.SLOT_RETURN:
                // 返回按钮，回到主界面
                openMainUI(player);
                break;
                
            case MailUI.SLOT_FIRST_PAGE:
                // 首页
                refreshMailUI(player, 1);
                break;
                
            case MailUI.SLOT_PREV_PAGE:
                // 上一页
                MailUI mailUI = getMailUI(player);
                if (mailUI != null) {
                    refreshMailUI(player, mailUI.getPage(player) - 1);
                }
                break;
                
            case MailUI.SLOT_NEXT_PAGE:
                // 下一页
                mailUI = getMailUI(player);
                if (mailUI != null) {
                    refreshMailUI(player, mailUI.getPage(player) + 1);
                }
                break;
                
            case MailUI.SLOT_LAST_PAGE: {
                // 末页
                Map<String, Object> mailData = getMailData(player.getName());
                int totalItems = mailData != null ? mailData.size() : 0;
                int itemsPerPage = 9; // 每页9封邮件
                int maxPage = (totalItems <= 0) ? 1 : ((totalItems - 1) / itemsPerPage + 1);
                refreshMailUI(player, maxPage);
                break;
            }
                
            default:
                // 检查是否点击了领取按钮
                if (slot >= 9 && slot < 18) {
                    // 计算对应的邮件物品槽位
                    int mailSlot = slot - 9;
                    handlePickButton(player, mailSlot);
                }
                break;
        }
        
        // 取消事件，防止玩家拿走界面中的物品
        event.setCancelled(true);
    }
    
    /**
     * 处理领取按钮点击
     * @param player 玩家
     * @param mailSlot 邮件物品的槽位
     */
    private void handlePickButton(Player player, int mailSlot) {
        // 获取邮件ID
        String mailId = getMailIdFromSlot(player, mailSlot);
        if (mailId == null) {
            return;
        }
        
        // 处理领取操作
        boolean success = handlePickMail(player, mailId);
        if (success) {
            player.sendMessage("§a成功领取邮件");
            
            // 刷新邮件界面
            MailUI mailUI = getMailUI(player);
            if (mailUI != null) {
                refreshMailUI(player, mailUI.getPage(player));
            }
        }
    }
    
    /**
     * 从槽位获取邮件ID
     * @param player 玩家
     * @param slot 槽位
     * @return 邮件ID，如果没有则返回null
     */
    protected abstract String getMailIdFromSlot(Player player, int slot);
    
    /**
     * 获取当前打开的邮件界面
     * @param player 玩家
     * @return 邮件界面实例
     */
    protected abstract MailUI getMailUI(Player player);
    
    /**
     * 刷新邮件界面
     * @param player 玩家
     * @param page 页码
     */
    protected abstract void refreshMailUI(Player player, int page);
    
    /**
     * 打开主界面
     * @param player 玩家
     */
    protected abstract void openMainUI(Player player);
}
