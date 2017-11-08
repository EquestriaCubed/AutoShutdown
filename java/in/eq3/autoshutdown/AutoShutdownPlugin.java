package in.eq3.autoshutdown;

import in.eq3.autoshutdown.util.ServerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoShutdownPlugin extends JavaPlugin
{
	private final Settings settings = new Settings(this);
	private final ServerTask task = new ServerTask(this, this::onTick);

	private String oldMessage = null;
	private int oldTime = Integer.MAX_VALUE;

	@Override
	public final void onEnable()
	{
		settings.load();
		if (settings.isPluginEnabled())
			task.start(20);
	}

	@Override
	public final void onDisable()
	{
		task.stop();
	}

	public final void onTick()
	{
		final int time = settings.getSecondsToShutdown();
		final String message = settings.getShutdownMessage(time);
		final String kickMessage = settings.getKickMessage();

		if (message != null && !message.isEmpty() && !message.equals(oldMessage))
			Bukkit.getServer().broadcastMessage(message);
		if (time > oldTime)
		{
			for (final Player player : Bukkit.getServer().getOnlinePlayers())
				player.kickPlayer(kickMessage);
			Bukkit.getServer().shutdown();
		}

		oldMessage = message;
		oldTime = time;
	}
}
