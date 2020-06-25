package cn.mcplugin.aiomotd.plugin;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cn.mcplugin.aiomotd.plugin.economy.EconomyUtils;
import cn.mcplugin.aiomotd.plugin.economy.VaultEconomyUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin{
	static String uuid = "";
	static String version = "";
	static int sid = 0;
	static Thread t = null;
	static String economy = "";
	@Override
	public void onEnable() {
		FileConfiguration config = getConfig();
		File f = new File("./AIOMotd");
		if(!f.exists()) {
			config.addDefault("version", config.getString("version"));
			config.addDefault("uuid", config.getString("uuid"));
			config.addDefault("sid", config.getInt("sid"));
			config.options().copyDefaults(true);
			saveConfig();
		}
		uuid = config.getString("uuid");
		AIMPluginUtils.info = config.getBoolean("info");
		version = config.getString("version");
		sid = config.getInt("sid");
		getLogger().info("AIOMotd-v"+version+" �Ѽ��أ�");
		getLogger().info("\n    _    ___ ___  __  __       _      _ \r\n" + 
				"   / \\  |_ _/ _ \\|  \\/  | ___ | |_ __| |\r\n" + 
				"  / _ \\  | | | | | |\\/| |/ _ \\| __/ _` |\r\n" + 
				" / ___ \\ | | |_| | |  | | (_) | || (_| |\r\n" + 
				"/_/   \\_\\___\\___/|_|  |_|\\___/ \\__\\__,_|\r\n" + 
				"                                        \r\n");



		this.initEconomy();//���ؾ���

		economy = config.getString("economy");
		InfoThread it = new InfoThread();
		t = new Thread(it);
		t.start();
	}


	private void initEconomy() {
		if(true) {//�ж��û��Ƿ�������ͳ��ģʽ
			if(economy.equals("vault")) {
				if(initVault()) {
					//����Vaultǰ�ò�����سɹ�
					AIMPluginUtils.print("Vault����ǰ�ü��سɹ���");
				}else {
					AIMPluginUtils.print("Vault����ǰ�ü���ʧ�ܣ�");
				}
			}
		}
	}


	/**
	 * ��ʼ��Vault
	 * @return �Ƿ�ɹ�
	 */
	private boolean initVault(){
		boolean hasNull = false;
		//��ʼ������ϵͳʵ��
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			if ((VaultEconomyUtils.economy = economyProvider.getProvider()) == null) hasNull = true;
		}
		return !hasNull;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		t.stop();
		getLogger().info("AIOMotd-v"+version+" �Ѱ�ȫж�أ�");
		getLogger().info("\n    _    ___ ___  __  __       _      _ \r\n" + 
				"   / \\  |_ _/ _ \\|  \\/  | ___ | |_ __| |\r\n" + 
				"  / _ \\  | | | | | |\\/| |/ _ \\| __/ _` |\r\n" + 
				" / ___ \\ | | |_| | |  | | (_) | || (_| |\r\n" + 
				"/_/   \\_\\___\\___/|_|  |_|\\___/ \\__\\__,_|\r\n" + 
				"                                        \r\n");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("aim")) { // ������������/basic��ִ����������...
				        onEnable();
						getLogger().info("======AIOMotd-v"+version+" ���سɹ��� ======");
//			Socket s = null;
//			try {
//				s = new Socket("127.0.0.1",50000);
//			} catch (UnknownHostException e) {
//				// TODO �Զ����ɵ� catch ��
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO �Զ����ɵ� catch ��
//				e.printStackTrace();
//			}
//			ReceiveInfoThread rit = new ReceiveInfoThread(s);
//			Thread t = new Thread(rit);
//			t.start();
//			AIMPluginUtils.sendJsonToAIMServer("server_info",AIMPluginUtils.getServerInfo(),true);//��������
			
			return true;
		} //����������ݳɹ�ִ�У��򷵻�true 
		// ���ִ��ʧ�ܣ��򷵻�false.
		//	    if(cmd.getName().equalsIgnoreCase("send")) {
		//	    }
		return false;
	}
	public static void main(String[] args) {
		System.out.println("1");
	}
}
