package cn.mcplugin.aiomotd.plugin.economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultEconomyUtils implements EconomyUtils{
	public Economy economy = null;
	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return economy.hasAccount(player);
	}
	@Override
	public boolean has(OfflinePlayer player, long money) {
		return economy.has(player, money);
	}
	@Override
	public double getMoney(OfflinePlayer player) {
		return economy.getBalance(player);
	}
	
	/**
	 * 留个空构造器，方便反射
	 * */
	public VaultEconomyUtils(Economy economy) {
		this.economy = economy;
	}
}
