package cn.mcplugin.aiomotd.plugin.javabean;

import java.util.TreeMap;

/*
 * 
 * {world:{"fly_world":9,"end_world":10,"world":20}}
 * 
 * 
 * */
public class WorldData {
	private TreeMap<String,Integer> world = null;//世界名，在线人数

	/**
	 * @return world
	 */
	public TreeMap<String, Integer> getWorld() {
		return world;
	}

	/**
	 * @param world 要设置的 world
	 */
	public void setWorld(TreeMap<String, Integer> world) {
		this.world = world;
	} 
	public WorldData() {
		
	}
}
