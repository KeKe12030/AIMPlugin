package cn.mcplugin.aiomotd.plugin;

public class InfoThread implements Runnable{
	@Override
	public void run() {
			try {//��һ��˯��10S
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}//˯��10����
		while(true) {
			try {
				AIMPluginUtils.sendJsonToAIMServer
				("online_player_info",AIMPluginUtils.getOnlinePlayers(),true);
				//���������������
				
				AIMPluginUtils.sendJsonToAIMServer
				("server_info",AIMPluginUtils.getServerInfo(),false);
				//���ͷ���������
				
				AIMPluginUtils.sendJsonToAIMServer
				("world_info",AIMPluginUtils.getWorldDataJson(),false);
				//������������
				
				
				Thread.sleep(1000*10*1);//һ����һ��
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}//˯��1����
		}
	}
}
