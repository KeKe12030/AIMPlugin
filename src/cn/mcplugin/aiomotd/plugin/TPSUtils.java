package cn.mcplugin.aiomotd.plugin;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class TPSUtils {
    private static Object minecraftServer;
    private static Field recentTps;

    public static double[] getRecentTps() {
        try {
            if (minecraftServer == null) {
                Server server = Bukkit.getServer();
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                minecraftServer = consoleField.get(server);
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return new double[] {20, 20, 20};
    }
}
