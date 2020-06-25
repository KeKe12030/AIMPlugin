package cn.mcplugin.aiomotd.plugin.javabean;

import java.util.Vector;

/*
 * {player:["Steve","VioletTec"]}
 * 
 * */
public class PlayerData {
	private Vector<Player> player;
	public class Player{
		public String uuid;
		public String playerName;
		public int money;
	}
	public PlayerData() {}
	/**
	 * @return player
	 */
	public Vector<Player> getPlayer() {
		return player;
	}
	/**
	 * @param player ÒªÉèÖÃµÄ player
	 */
	public void setPlayer(Vector<Player> player) {
		this.player = player;
	}
}
