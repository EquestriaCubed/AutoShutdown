package in.eq3.autoshutdown;

import in.eq3.autoshutdown.util.StringConverter;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Settings
{
	private final String CONFIG_ENABLE_PLUGIN = "AutoShutdown";
	private final String CONFIG_SHUTDOWN_TIME = "ShutdownTime";
	private final String CONFIG_SHUTDOWN_MESSAGE = "ShutdownMessages";
	private final String CONFIG_KICK_MESSAGE = "KickMessage";

	private final JavaPlugin plugin;
	private FileConfiguration config;

	private boolean isEnabled = false;
	private int shutdownHour = 0;
	private int shutdownMinute = 0;
	private int shutdownSecond = 0;
	private final Map<Integer, String> shutdownMessages = new TreeMap<Integer, String>();
	private String kickMessage = "";

	public Settings(final JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	public final boolean isPluginEnabled()
	{
		return isEnabled;
	}

	public final String getKickMessage()
	{
		return kickMessage;
	}

	public final String getShutdownMessage(final int secondsToShutdown)
	{
		String nearestMessage = null;
		int nearestTime = Integer.MAX_VALUE;
		for (Entry<Integer, String> entry : shutdownMessages.entrySet())
		{
			if (secondsToShutdown <= entry.getKey() && nearestTime > entry.getKey())
			{
				nearestTime = entry.getKey();
				nearestMessage = entry.getValue();
			}
		}
		return nearestMessage;
	}

	public final int getSecondsToShutdown()
	{
		final Calendar currentTime = Calendar.getInstance();
		final int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
		final int currentMinute = currentTime.get(Calendar.MINUTE);
		final int currentSecond = currentTime.get(Calendar.SECOND);
		final int currentPointInTime = (currentHour * 60 + currentMinute) * 60 + currentSecond;

		final int targetPointInTime = (shutdownHour * 60 + shutdownMinute) * 60 + shutdownSecond;

		final int diff = targetPointInTime - currentPointInTime;
		return diff < 0 ? diff + 86400 : diff;
	}

	public final void load()
	{
		File file = new File("config.yml");
		if (!file.exists())
			plugin.saveDefaultConfig();
		config = plugin.getConfig();

		isEnabled = config.getBoolean(CONFIG_ENABLE_PLUGIN);
		kickMessage = config.getString(CONFIG_KICK_MESSAGE, "");
		loadShutdownTime(config);
		loadShutdownMessages(config);
	}

	private final void loadShutdownTime(final FileConfiguration config)
	{
		final String timeString = config.getString(CONFIG_SHUTDOWN_TIME, "");
		final String[] parts = timeString.split(":");

		switch (parts.length)
		{
		case 1:
			shutdownHour = StringConverter.toInt(parts[0]);
			shutdownMinute = 0;
			shutdownSecond = 0;
			break;
		case 2:
			shutdownHour = StringConverter.toInt(parts[0]);
			shutdownMinute = StringConverter.toInt(parts[1]);
			shutdownSecond = 0;
			break;
		case 3:
			shutdownHour = StringConverter.toInt(parts[0]);
			shutdownMinute = StringConverter.toInt(parts[1]);
			shutdownSecond = StringConverter.toInt(parts[2]);
			break;
		default:
			shutdownHour = 0;
			shutdownMinute = 0;
			shutdownSecond = 0;
		}
	}

	private final void loadShutdownMessages(final FileConfiguration config)
	{
		shutdownMessages.clear();
		for (String key : getKeys(CONFIG_SHUTDOWN_MESSAGE))
		{
			final int time = StringConverter.toInt(key, -1);
			final String message = config.getString(CONFIG_SHUTDOWN_MESSAGE + "." + key, "");

			if (time != -1 && !message.isEmpty())
				shutdownMessages.put(time, ChatColor.translateAlternateColorCodes('&', message));
		}
	}

	private final Collection<String> getKeys(final String section)
	{
		ConfigurationSection config = this.config.getConfigurationSection(section);
		if (config == null)
			return new HashSet<String>();
		return config.getKeys(false);
	}
}
