package in.eq3.autoshutdown.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerTask
{
	private final JavaPlugin plugin;
	private final Runnable task;

	private int id = -1;

	public ServerTask(final JavaPlugin plugin, final Runnable task)
	{
		this.plugin = plugin;
		this.task = task;
	}

	public final boolean start(final int repeatTime)
	{
		if (id != -1)
			return false;
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, repeatTime);
		return true;
	}

	public final boolean stop()
	{
		if (id == -1)
			return false;
		Bukkit.getServer().getScheduler().cancelTask(id);
		id = -1;
		return true;
	}
}
