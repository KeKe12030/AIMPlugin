package cn.mcplugin.aiomotd.plugin.economy;

import org.bukkit.OfflinePlayer;

public interface EconomyUtils {
	public boolean hasAccount(OfflinePlayer player);
	public boolean has(OfflinePlayer player,long money);
	public double getMoney(OfflinePlayer player);
	
}
