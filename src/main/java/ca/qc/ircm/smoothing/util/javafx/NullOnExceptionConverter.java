package ca.qc.ircm.smoothing.util.javafx;

import javafx.util.StringConverter;

/**
 * {@link StringConverter} that returns null when an exception is thrown by the
 * supplied converter.
 */
public class NullOnExceptionConverter<T> extends StringConverter<T> {
	private final StringConverter<T> converter;

	public NullOnExceptionConverter(StringConverter<T> converter) {
		this.converter = converter;
	}

	@Override
	public T fromString(String string) {
		try {
			return converter.fromString(string);
		} catch (Throwable e) {
			return null;
		}
	}

	@Override
	public String toString(T object) {
		try {
			return converter.toString(object);
		} catch (Throwable e) {
			return null;
		}
	}
}
