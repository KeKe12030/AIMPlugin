package cn.mcplugin.aiomotd.plugin.javabean;

import java.util.TreeMap;

/*
 * 
 * {world:{"fly_world":9,"end_world":10,"world":20}}
 * 
 * 
 * */
public class WorldData {
	private TreeMap<String,Integer> world = null;//����������������

	/**
	 * @return world
	 */
	public TreeMap<String, Integer> getWorld() {
		return world;
	}

	/**
	 * @param world Ҫ���õ� world
	 */
	public void setWorld(TreeMap<String, Integer> world) {
		this.world = world;
	} 
	public WorldData() {
		
	}
}
