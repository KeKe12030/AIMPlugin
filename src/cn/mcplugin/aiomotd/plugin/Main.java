package cn.mcplugin.aiomotd.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cn.mcplugin.aiomotd.plugin.economy.EconomyUtils;
import cn.mcplugin.aiomotd.plugin.economy.VaultEconomyUtils;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	static EconomyUtils economyUtils = null;
	static String uuid = "";
	static String version = "";
	static int sid = 0;
	static Thread t = null;
	static String economy = "";
	@Override
	public void onEnable() {
		File file = new File(this.getDataFolder().getAbsolutePath() + 
			      "/config.yml");
		System.out.println(file);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		System.out.println(!file.exists());
		if(!file.exists()){
			System.out.println("����CONFIG.YML��.......");
			config.addDefault("version", config.getString("version"));
			config.addDefault("uuid", config.getString("uuid"));
			config.addDefault("sid", config.getInt("sid"));
			config.addDefault("economy", "vault");
			
			config.options().copyDefaults(true);
			
			saveConfig();
			try {
				config.save(file);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
			System.out.println("CONFIG.YML�����ɹ���");
		}
		reloadConfig();
		uuid = config.getString("uuid");
		AIMPluginUtils.info = config.getBoolean("info");
		version = config.getString("version");
		sid = config.getInt("sid");
		economy = config.getString("economy");
		getLogger().info("AIOMotd-v"+version+" �Ѽ��أ�");
		System.out.println("UUID AND SID:::"+uuid+"  "+sid);
		getLogger().info("\n    _    ___ ___  __  __       _      _ \r\n" + 
				"   / \\  |_ _/ _ \\|  \\/  | ___ | |_ __| |\r\n" + 
				"  / _ \\  | | | | | |\\/| |/ _ \\| __/ _` |\r\n" + 
				" / ___ \\ | | |_| | |  | | (_) | || (_| |\r\n" + 
				"/_/   \\_\\___\\___/|_|  |_|\\___/ \\__\\__,_|\r\n" + 
				"                                        \r\n");


		System.out.println("�����У�������");		

		initEconomy();//���ؾ���
		System.out.println("�Ƿ������Vault����"+getServer().getPluginManager().getPlugin("Vault"));
		InfoThread it = new InfoThread();
		t = new Thread(it);
		t.start();
	}
	

	private void initEconomy() {
		if(economy.equals("vault")) {
			if(initVault()) {
				//����Vaultǰ�ò�����سɹ�
				AIMPluginUtils.print("Vault����ǰ�ü��سɹ���");
				VaultEconomyUtils veu = (VaultEconomyUtils)economyUtils;
				System.out.println("Main������EconomyUtils.economyΪ��"+veu.economy);
			}else {
				AIMPluginUtils.print("Vault����ǰ�ü���ʧ�ܣ�");
			}
		}
		
	}


	/**
	 * ��ʼ��Vault
	 * @return �Ƿ�ɹ�
	 */
	private boolean initVault(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	System.out.println("Vault��NULL");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	System.out.println("RSP��NUll");
            return false;
        }
        System.out.println("RPS������Ϊ����"+rsp.getProvider());
        VaultEconomyUtils veu = new VaultEconomyUtils(rsp.getProvider());
		economyUtils = veu;
        return veu.economy != null;
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
