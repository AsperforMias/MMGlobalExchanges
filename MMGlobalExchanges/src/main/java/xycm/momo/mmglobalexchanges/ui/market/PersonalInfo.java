package xycm.momo.mmglobalexchanges.ui.market;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xycm.momo.mmglobalexchanges.MMGlobalExchanges;
import xycm.momo.mmglobalexchanges.core.market.NormalMarketStrategy;
import xycm.momo.mmglobalexchanges.file.PlayerData;
import xycm.momo.mmglobalexchanges.ui.common.PersonalInfoUI;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场个人信息界面
 */
public class PersonalInfo extends PersonalInfoUI {

    // 单例实例
    private static PersonalInfo instance;
    
    /**
     * 私有构造函数
     */
    private PersonalInfo() {
        super("市场个人信息", 54, new NormalMarketStrategy());
    }
    
    /**
     * 获取实例
     * @return PersonalInfo实例
     */
    public static PersonalInfo getInstance() {
        if (instance == null) {
            instance = new PersonalInfo();
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
        lore.add("§f上架商品: " + playerData.getMarketLaunchItems(player.getName()));
        lore.add("§f出售商品: " + playerData.getMarketSellItems(player.getName()));
        lore.add("§f购买商品: " + playerData.getMarketBuyItems(player.getName()));
        
        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);
        
        // 添加到界面
        addItem(4, skull);
    }

    @Override
    public void openLaunchRecord(Player player) {
        LaunchRecord.getInstance().open(player, 1);
    }

    @Override
    public void openMail(Player player) {
        Mail.getInstance().open(player, 1);
    }

    @Override
    public void openPurchaseRecord(Player player) {
        PurchaseRecord.getInstance().open(player, 1);
    }

    @Override
    public void openSellRecord(Player player) {
        SellRecord.getInstance().open(player, 1);
    }
}
