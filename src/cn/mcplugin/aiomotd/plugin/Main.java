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
			System.out.println("创建CONFIG.YML中.......");
			config.addDefault("version", config.getString("version"));
			config.addDefault("uuid", config.getString("uuid"));
			config.addDefault("sid", config.getInt("sid"));
			config.addDefault("economy", "vault");
			
			config.options().copyDefaults(true);
			
			saveConfig();
			try {
				config.save(file);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			System.out.println("CONFIG.YML创建成功！");
		}
		reloadConfig();
		uuid = config.getString("uuid");
		AIMPluginUtils.info = config.getBoolean("info");
		version = config.getString("version");
		sid = config.getInt("sid");
		economy = config.getString("economy");
		getLogger().info("AIOMotd-v"+version+" 已加载！");
		System.out.println("UUID AND SID:::"+uuid+"  "+sid);
		getLogger().info("\n    _    ___ ___  __  __       _      _ \r\n" + 
				"   / \\  |_ _/ _ \\|  \\/  | ___ | |_ __| |\r\n" + 
				"  / _ \\  | | | | | |\\/| |/ _ \\| __/ _` |\r\n" + 
				" / ___ \\ | | |_| | |  | | (_) | || (_| |\r\n" + 
				"/_/   \\_\\___\\___/|_|  |_|\\___/ \\__\\__,_|\r\n" + 
				"                                        \r\n");


		System.out.println("加载中！！！！");		

		initEconomy();//加载经济
		System.out.println("是否加载了Vault：："+getServer().getPluginManager().getPlugin("Vault"));
		InfoThread it = new InfoThread();
		t = new Thread(it);
		t.start();
	}
	

	private void initEconomy() {
		if(economy.equals("vault")) {
			if(initVault()) {
				//加载Vault前置插件加载成功
				AIMPluginUtils.print("Vault经济前置加载成功！");
				VaultEconomyUtils veu = (VaultEconomyUtils)economyUtils;
				System.out.println("Main方法的EconomyUtils.economy为："+veu.economy);
			}else {
				AIMPluginUtils.print("Vault经济前置加载失败！");
			}
		}
		
	}


	/**
	 * 初始化Vault
	 * @return 是否成功
	 */
	private boolean initVault(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
        	System.out.println("Vault是NULL");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	System.out.println("RSP是NUll");
            return false;
        }
        System.out.println("RPS的内容为：："+rsp.getProvider());
        VaultEconomyUtils veu = new VaultEconomyUtils(rsp.getProvider());
		economyUtils = veu;
        return veu.economy != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		t.stop();
		getLogger().info("AIOMotd-v"+version+" 已安全卸载！");
		getLogger().info("\n    _    ___ ___  __  __       _      _ \r\n" + 
				"   / \\  |_ _/ _ \\|  \\/  | ___ | |_ __| |\r\n" + 
				"  / _ \\  | | | | | |\\/| |/ _ \\| __/ _` |\r\n" + 
				" / ___ \\ | | |_| | |  | | (_) | || (_| |\r\n" + 
				"/_/   \\_\\___\\___/|_|  |_|\\___/ \\__\\__,_|\r\n" + 
				"                                        \r\n");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("aim")) { // 如果玩家输入了/basic则执行如下内容...
			onEnable();
			getLogger().info("======AIOMotd-v"+version+" 重载成功！ ======");
			//			Socket s = null;
			//			try {
			//				s = new Socket("127.0.0.1",50000);
			//			} catch (UnknownHostException e) {
			//				// TODO 自动生成的 catch 块
			//				e.printStackTrace();
			//			} catch (IOException e) {
			//				// TODO 自动生成的 catch 块
			//				e.printStackTrace();
			//			}
			//			ReceiveInfoThread rit = new ReceiveInfoThread(s);
			//			Thread t = new Thread(rit);
			//			t.start();
			//			AIMPluginUtils.sendJsonToAIMServer("server_info",AIMPluginUtils.getServerInfo(),true);//发送数据

			return true;
		} //如果以上内容成功执行，则返回true 
		// 如果执行失败，则返回false.
		//	    if(cmd.getName().equalsIgnoreCase("send")) {
		//	    }
		return false;
	}
	public static void main(String[] args) {
		System.out.println("1");
	}
}
