package ca.qc.ircm.smoothing.test.config;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * Log4J test init.
 */
public class Log4JTestInit {
	/**
	 * Initialise a Log4J logger. <strong>This method must be called before any class initialises a Logger.</strong>
	 */
	public static void init() {
		// Layout to use in all appenders.
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{ISO8601} - %-5p - %m - %c - %L%n");

		// Create file appender.
		RollingFileAppender fileAppender = new RollingFileAppender();
		fileAppender.setName("fileAppender");
		fileAppender.setMaxFileSize("2024KB");
		fileAppender.setMaxBackupIndex(2);
		fileAppender.setFile(System.getProperty("user.home") + "/.ircm/smoothing_unit_test.log");
		fileAppender.setAppend(true);
		fileAppender.setLayout(layout);
		fileAppender.activateOptions();
		Logger.getRootLogger().addAppender(fileAppender);

		// Change log levels.
		Logger.getRootLogger().setLevel(Level.WARN);
		Logger.getLogger("ca.qc.ircm.smoothing").setLevel(Level.DEBUG);

		createConsoleAppender(layout);
	}

	private static void createConsoleAppender(Layout layout) {
		// Create console appender.
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setName("consoleAppender");
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();
		Logger.getRootLogger().addAppender(consoleAppender);
	}

	public static void shutdown() {
		LogManager.shutdown();
	}
}
