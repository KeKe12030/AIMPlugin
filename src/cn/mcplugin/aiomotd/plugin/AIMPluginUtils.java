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
	 *	获取在线玩家的JSON数据
	 *{player:["",""]} 
	 * */
	public static PlayerData getOnlinePlayers(){//获取在线玩家名
		PlayerData pd = new PlayerData();
		Vector<cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player> v = new Vector<cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player>();
		Collection<? extends Player> playersArray = Bukkit.getServer().getOnlinePlayers();
		Iterator it = playersArray.iterator();
		cn.mcplugin.aiomotd.plugin.javabean.PlayerData.Player p = null;
		while(it.hasNext()) {
			Player player = (Player) it.next();
			p = pd.new Player();//存入Player
			p.uuid = player.getUniqueId().toString();
			p.playerName = player.getName();
			System.out.println("EconomyUtils的值："+Main.economyUtils);
			if(Main.economyUtils!=null) {
				AIMPluginUtils.print("economyUtils不是空！");
				p.money = Main.economyUtils.getMoney(player);
			}
			v.add(p);//获取玩家名
		}
		pd.setPlayer(v);
		return pd;
	}
	public static WorldData getWorldDataJson() {
		WorldData wd = new WorldData();
		TreeMap<String,Integer> tm = new TreeMap<>();
		List<World> worldList = Bukkit.getWorlds();
		for(int i =0;i<worldList.size();i++) {
			//世界名，世界热度（用在线人数代替目前）
			tm.put(worldList.get(i).getName(), worldList.get(i).getPlayers().size());
		}
		wd.setWorld(tm);
		return wd;//得到JSON
	}
	public static ServerInfoData getServerInfo() {//获取最近的TPS
		ServerInfoData si = new ServerInfoData();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);//保留两位小数点
		si.setTPS(new Double(TPSUtils.getRecentTps()[0]).intValue());
		return si;
	}
	/**
	 * 发送数据，规定协议：开头一行：UUID|SID   1.在线玩家数据，2.地图数据，3.服务器信息数据
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
			if(infoSwitch) {//控制是否启动接收线程
				//			//启动接收线程
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
			sb.append("#"+type+"#"+"\n");//发送消息的类型
			sb.append(uuid+"@"+sid+"\n");//SID 和 UUID
			AIMPluginUtils.print("UID and SSID:"+uuid+" "+sid);
			String json = new Gson().toJson(context,clazz);
			sb.append(json);//把内容转换为JSON
			System.out.println("json"+json);
			dos.writeBytes(curlInfo+(sb.toString().getBytes().length)+"\r\n"+"\r\n"+sb.toString());
			dos.writeBytes("!");
			dos.flush();//数据发送成功
		} catch (IOException e) {
			if(infoSwitch) {
				AIMPluginUtils.print("AIM官方服务器宕机，请联系QQ:3352772828修复！");
			}
		}

	}
	public static String[] getInfoFromAIMServer(InputStream is) throws IOException {
		DataInputStream br = new DataInputStream(is);
		String head = br.readUTF();//第一个UTF是报头
		String context = br.readUTF();//第二个readUTF是内容提
		int start = context.indexOf("text:")+"text:".getBytes().length;
		return context.substring(start).split("\r\n");

	}
	public static void print(String msg) {
		if(info) {//控制一下是否允许输出
			Bukkit.getLogger().info("[AIOMotd] "+msg);
		}
	}
	public static void close(Closeable... closeables) {
		for(Closeable closeable:closeables) {
			if(null != closeable) {
				try {
					closeable.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		AIMPluginUtils.sendJsonToAIMServer("player_info","{uuid:\"ASDFWEFDASFAS-2WQ3EDF\",name:\"VioletTec\"}",PlayerData.class,true);//发送数据


	}
}
