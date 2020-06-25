package cn.mcplugin.aiomotd.plugin;

import java.io.IOException;
import java.net.Socket;

public class ReceiveInfoThread implements Runnable{
	Socket s = null;
	public ReceiveInfoThread(Socket s) {
		this.s = s;
	}
	@Override
	public void run() {
		try {
			String[] infos = AIMPluginUtils.getInfoFromAIMServer(s.getInputStream());
			for(int i=0;i<infos.length;i++) {
				AIMPluginUtils.print(infos[i]);
			}
			AIMPluginUtils.close(s);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}
