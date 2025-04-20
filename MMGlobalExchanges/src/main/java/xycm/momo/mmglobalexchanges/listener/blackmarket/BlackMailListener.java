package xycm.momo.mmglobalexchanges.listener.blackmarket;

import org.bukkit.entity.Player;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.blackmarket.BlackMarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.listener.common.AbstractMailListener;
import xycm.momo.mmglobalexchanges.ui.blackmarket.BlackMail;
import xycm.momo.mmglobalexchanges.ui.blackmarket.BlackMarket;
import xycm.momo.mmglobalexchanges.ui.common.MailUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑市邮件监听器
 */
public class BlackMailListener extends AbstractMailListener {

    // 存储槽位和邮件ID的映射关系
    private final Map<String, List<String>> playerMailIds = new HashMap<>();
    
    public BlackMailListener() {
        super(new BlackMarketStrategy());
    }
    
    @Override
    protected String getMailTitle() {
        return "黑市邮件";
    }
    
    @Override
    protected Map<String, Object> getMailData(String playerName) {
        return MMGlobalExchanges.instance.getPlayerData().getBlackMarketMail(playerName);
    }
    
    @Override
    protected boolean handlePickMail(Player player, String mailId) {
        return MMGlobalExchanges.instance.getPlayerData().pickBlackMarketMail(player, mailId);
    }
    
    @Override
    protected int handlePickAll(Player player) {
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        Map<String, Object> mailMap = getMailData(player.getName());
        
        if (mailMap == null || mailMap.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        for (String mailId : new ArrayList<>(mailMap.keySet())) {
            if (playerData.pickBlackMarketMail(player, mailId)) {
                count++;
            }
        }
        
        return count;
    }
    
    @Override
    protected String getMailIdFromSlot(Player player, int slot) {
        List<String> mailIds = playerMailIds.get(player.getName());
        if (mailIds != null && slot < mailIds.size()) {
            return mailIds.get(slot);
        }
        return null;
    }
    
    @Override
    protected MailUI getMailUI(Player player) {
        return BlackMail.getInstance();
    }
    
    @Override
    protected void refreshMailUI(Player player, int page) {
        // 准备存储当前页面的邮件ID
        List<String> currentPageMailIds = new ArrayList<>();
        playerMailIds.put(player.getName(), currentPageMailIds);
        
        // 获取玩家的邮件数据
        Map<String, Object> mailMap = getMailData(player.getName());
        if (mailMap != null && !mailMap.isEmpty()) {
            // 为了分页，先获取所有邮件ID
            List<String> allMailIds = new ArrayList<>(mailMap.keySet());
            
            // 计算当前页显示的邮件
            int itemsPerPage = 9; // 每页9封邮件
            int startIndex = (page - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, allMailIds.size());
            
            // 添加当前页的邮件ID
            for (int i = startIndex; i < endIndex; i++) {
                currentPageMailIds.add(allMailIds.get(i));
            }
        }
        
        // 打开黑市邮件界面
        BlackMail.getInstance().open(player, page);
    }
    
    @Override
    protected void openMainUI(Player player) {
        BlackMarket.getInstance().open(player, 1);
    }
}
