package xycm.momo.mmglobalexchanges.listener.market;

import org.bukkit.entity.Player;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.listener.common.AbstractMailListener;
import xycm.momo.mmglobalexchanges.ui.common.MailUI;
import xycm.momo.mmglobalexchanges.ui.market.Mail;
import xycm.momo.mmglobalexchanges.ui.market.Market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场邮件监听器
 */
public class MailListener extends AbstractMailListener {

    // 存储槽位和邮件ID的映射关系
    private final Map<String, List<String>> playerMailIds = new HashMap<>();
    
    public MailListener() {
        super(new NormalMarketStrategy());
    }
    
    @Override
    protected String getMailTitle() {
        return "市场邮件";
    }
    
    @Override
    protected Map<String, Object> getMailData(String playerName) {
        return MMGlobalExchanges.instance.getPlayerData().getMarketMail(playerName);
    }
    
    @Override
    protected boolean handlePickMail(Player player, String mailId) {
        return MMGlobalExchanges.instance.getPlayerData().pickMarketMail(player, mailId);
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
            if (playerData.pickMarketMail(player, mailId)) {
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
        return Mail.getInstance();
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
        
        // 打开邮件界面
        Mail.getInstance().open(player, page);
    }
    
    @Override
    protected void openMainUI(Player player) {
        Market.getInstance().open(player, 1);
    }
}
