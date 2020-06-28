package cn.mcplugin.aiomotd.plugin;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import cn.mcplugin.aiomotd.plugin.javabean.PlayerData;
import cn.mcplugin.aiomotd.plugin.javabean.ServerInfoData;
import cn.mcplugin.aiomotd.plugin.javabean.WorldData;

public class AIMPluginUtils {
	static boolean info = true;
	/**
	 *	��ȡ������ҵ�JSON����
	 *{player:["",""]} 
	 * */
	public static PlayerData getOnlinePlayers(){//��ȡ���������
		PlayerData pd = new PlayerData();
		Vector<cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player> v = new Vector<cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player>();
		Collection<? extends Player> playersArray = Bukkit.getServer().getOnlinePlayers();
		Iterator it = playersArray.iterator();
		cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player p = null;
		while(it.hasNext()) {
			Player player = (Player) it.next();
			p = pd.new Player();//����Player
			p.uuid = player.getUniqueId().toString();
			p.playerName = player.getName();
			System.out.println("EconomyUtils��ֵ��"+Main.economyUtils);
			if(Main.economyUtils!=null) {
				AIMPluginUtils.print("economyUtils���ǿգ�");
				p.money = Main.economyUtils.getMoney(player);
			}
			v.add(p);//��ȡ�����
		}
		pd.setPlayer(v);
		return pd;
	}
	public static WorldData getWorldDataJson() {
		WorldData wd = new WorldData();
		TreeMap<String,Integer> tm = new TreeMap<>();
		List<World> worldList = Bukkit.getWorlds();
		for(int i =0;i<worldList.size();i++) {
			//�������������ȶȣ���������������Ŀǰ��
			tm.put(worldList.get(i).getName(), worldList.get(i).getPlayers().size());
		}
		wd.setWorld(tm);
		return wd;//�õ�JSON
	}
	public static ServerInfoData getServerInfo() {//��ȡ�����TPS
		ServerInfoData si = new ServerInfoData();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);//������λС����
		si.setTPS(new Double(TPSUtils.getRecentTps()[0]).intValue());
		return si;
	}
	/**
	 * �������ݣ��涨Э�飺��ͷһ�У�UUID|SID   1.����������ݣ�2.��ͼ���ݣ�3.��������Ϣ����
	 * */
	public static void sendJsonToAIMServer(String type,Object context,Class<?> clazz,boolean infoSwitch) {
		Socket s = null;
		int sid = Main.sid;
		String uuid = Main.uuid;
		DataOutputStream dos = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			s = new Socket("transfer.aim.mcplugin.cn",50000);
			//			s = new Socket("127.0.0.1",50000);
			if(infoSwitch) {//�����Ƿ����������߳�
				//			//���������߳�
				ReceiveInfoThread rit = new ReceiveInfoThread(s);
				Thread t = new Thread(rit);
				t.start();
			}
			dos = new DataOutputStream(s.getOutputStream());
			String curlInfo = "POST / HTTP/1.0\r\n" + 
					"Host: transfer.aim.mcplugin.cn\r\n" + 
					"User-Agent: AIOMotdPost/1.0.0\r\n" + 
					"Accept: */*\r\n" + 
					"Content-Type: application/x-www-form-urlencoded\r\n" + 
					"X-NWS-LOG-UUID: 2577315965380684280 6120dc087f7c60165b32f7b1a6f5655b\r\n" + 
					"Qvia: 249a57c54b37f08fa9b3fcd5ab126e32a7a4ebbf\r\n" + 
					"X-Tencent-Ua: Qcloud\r\n" + 
					"X-Forwarded-For: 1.1.1.1\r\n" + 
					"X-Forwarded-Proto: http\r\n" + 
					"X-Daa-Tunnel: hop_count=2\r\n" + 
					"Content-Length: ";
			StringBuilder sb = new StringBuilder();
			sb.append("#"+type+"#"+"\n");//������Ϣ������
			sb.append(uuid+"@"+sid+"\n");//SID �� UUID
			AIMPluginUtils.print("UID and SSID:"+uuid+" "+sid);
			String json = new Gson().toJson(context,clazz);
			sb.append(json);//������ת��ΪJSON
			System.out.println("json"+json);
			dos.writeBytes(curlInfo+(sb.toString().getBytes().length)+"\r\n"+"\r\n"+sb.toString());
			dos.writeBytes("!");
			dos.flush();//���ݷ��ͳɹ�
		} catch (IOException e) {
			if(infoSwitch) {
				AIMPluginUtils.print("AIM�ٷ�������崻�������ϵQQ:3352772828�޸���");
			}
		}

	}
	public static String[] getInfoFromAIMServer(InputStream is) throws IOException {
		DataInputStream br = new DataInputStream(is);
		String head = br.readUTF();//��һ��UTF�Ǳ�ͷ
		String context = br.readUTF();//�ڶ���readUTF��������
		int start = context.indexOf("text:")+"text:".getBytes().length;
		return context.substring(start).split("\r\n");

	}
	public static void print(String msg) {
		if(info) {//����һ���Ƿ��������
			Bukkit.getLogger().info("[AIOMotd] "+msg);
		}
	}
	public static void close(Closeable... closeables) {
		for(Closeable closeable:closeables) {
			if(null != closeable) {
				try {
					closeable.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		AIMPluginUtils.sendJsonToAIMServer("player_info","{uuid:\"ASDFWEFDASFAS-2WQ3EDF\",name:\"VioletTec\"}",PlayerData.class,true);//��������


	}
}
