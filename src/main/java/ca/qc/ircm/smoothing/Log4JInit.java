package ca.qc.ircm.smoothing;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Configures Log4j.
 */
public class Log4JInit {
	public static void init() {
		// Layout to use in all appenders.
		PatternLayout layout = createLayout();

		// Create file appender.
		RollingFileAppender fileAppender = new RollingFileAppender();
		fileAppender.setName("fileAppender");
		fileAppender.setMaxFileSize("2024KB");
		fileAppender.setMaxBackupIndex(1);
		fileAppender.setFile(System.getProperty("user.home") + "/.ircm/smoothing.log");
		fileAppender.setAppend(true);
		fileAppender.setLayout(layout);
		fileAppender.activateOptions();
		Logger.getRootLogger().addAppender(fileAppender);

		// Change log levels.
		Logger.getRootLogger().setLevel(Level.WARN);
		Logger.getLogger("ca.qc.ircm.smoothing").setLevel(Level.INFO);
		if (System.getProperty("user.name").equals("poitrac")) {
			createConsoleAppender(layout);
			Logger.getLogger("ca.qc.ircm.smoothing").setLevel(Level.TRACE);
		} else {
			Logger.getLogger("ca.qc.ircm.smoothing").setLevel(Level.INFO);
		}
	}

	public static PatternLayout createLayout() {
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{ISO8601} - %-5p - %m - %c - (%x) - %L%n");
		return layout;
	}

	private static void createConsoleAppender(Layout layout) {
		// Create console appender.
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setName("consoleAppender");
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();
		Logger.getRootLogger().addAppender(consoleAppender);
	}
}
