package xycm.momo.mmglobalexchanges.ui.blackmarket;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.blackmarket.BlackMarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.ui.common.PersonalInfoUI;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑市个人信息界面
 */
public class BlackPersonalInfo extends PersonalInfoUI {

    // 单例实例
    private static BlackPersonalInfo instance;
    
    /**
     * 私有构造函数
     */
    private BlackPersonalInfo() {
        super("黑市个人信息", 54, new BlackMarketStrategy());
    }
    
    /**
     * 获取实例
     * @return BlackPersonalInfo实例
     */
    public static BlackPersonalInfo getInstance() {
        if (instance == null) {
            instance = new BlackPersonalInfo();
        }
        return instance;
    }

    @Override
    protected void addPlayerInfo(Player player) {
        // 添加玩家头颅
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName("§f" + player.getName());
        skullMeta.setOwner(player.getName());
        
        // 获取玩家数据
        PlayerData playerData = MMGlobalExchanges.instance.getPlayerData();
        
        // 设置详细信息
        List<String> lore = new ArrayList<>();
        lore.add("§f余额: " + playerData.getBalance(player.getName()) + " 时息");
        lore.add("");
        lore.add("§f上架商品: " + playerData.getBlackMarketLaunchItems(player.getName()));
        lore.add("§f出售商品: " + playerData.getBlackMarketSellItems(player.getName()));
        lore.add("§f购买商品: " + playerData.getBlackMarketBuyItems(player.getName()));
        
        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);
        
        // 添加到界面
        addItem(4, skull);
    }

    @Override
    public void openLaunchRecord(Player player) {
        BlackLaunchRecord.getInstance().open(player, 1);
    }

    @Override
    public void openMail(Player player) {
        BlackMail.getInstance().open(player, 1);
    }

    @Override
    public void openPurchaseRecord(Player player) {
        BlackPurchaseRecord.getInstance().open(player, 1);
    }

    @Override
    public void openSellRecord(Player player) {
        BlackSellRecord.getInstance().open(player, 1);
    }
}
