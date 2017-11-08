package in.eq3.autoshutdown.util;

import java.util.function.Function;

public final class StringConverter
{
	private static final <T> T convert(final String string, final T def, final Function<String, T> func)
	{
		try
		{
			return func.apply(string);
		}
		catch (final Exception e)
		{
			return def;
		}
	}

	public static final boolean toBoolean(final String string)
	{
		return toBoolean(string, false);
	}

	public static final boolean toBoolean(final String string, final boolean def)
	{
		return convert(string, def, Boolean::parseBoolean);
	}

	public static final byte toByte(final String string)
	{
		return toByte(string, (byte) 0);
	}

	public static final byte toByte(final String string, final byte def)
	{
		return convert(string, def, Byte::parseByte);
	}

	public static final double toDouble(final String string)
	{
		return toDouble(string, 0.0);
	}

	public static final double toDouble(final String string, final double def)
	{
		return convert(string, def, Double::parseDouble);
	}

	public static final float toFloat(final String string)
	{
		return toFloat(string, 0.0f);
	}

	public static final float toFloat(final String string, final float def)
	{
		return convert(string, def, Float::parseFloat);
	}

	public static final int toInt(final String string)
	{
		return toInt(string, 0);
	}

	public static final int toInt(final String string, final int def)
	{
		return convert(string, def, Integer::parseInt);
	}

	public static final long toLong(final String string)
	{
		return toLong(string, 0L);
	}

	public static final long toLong(final String string, final long def)
	{
		return convert(string, def, Long::parseLong);
	}

	public static final short toShort(final String string)
	{
		return toShort(string, (short) 0);
	}

	public static final short toShort(final String string, final short def)
	{
		return convert(string, def, Short::parseShort);
	}
}
