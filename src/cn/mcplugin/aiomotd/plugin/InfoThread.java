package cn.mcplugin.aiomotd.plugin;

public class InfoThread implements Runnable{
	@Override
	public void run() {
			try {//第一次睡眠10S
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}//睡眠10秒钟
		while(true) {
			try {
				AIMPluginUtils.sendJsonToAIMServer
				("online_player_info",AIMPluginUtils.getOnlinePlayers(),true);
				//发送在线玩家数据
				
				AIMPluginUtils.sendJsonToAIMServer
				("server_info",AIMPluginUtils.getServerInfo(),false);
				//发送服务器数据
				
				AIMPluginUtils.sendJsonToAIMServer
				("world_info",AIMPluginUtils.getWorldDataJson(),false);
				//发送世界数据
				
				
				Thread.sleep(1000*10*1);//一分钟一次
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}//睡眠1分钟
		}
	}
}
