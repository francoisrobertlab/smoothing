package ca.qc.ircm.smoothing;

import javafx.application.Application;

import ca.qc.ircm.smoothing.gui.MainApplication;

/**
 * Starts program.
 */
public class Main {
    public static void main(String[] args) {
	Log4JInit.init();
	Application.launch(MainApplication.class, args);
    }
}
