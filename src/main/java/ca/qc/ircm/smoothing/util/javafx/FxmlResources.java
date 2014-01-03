package ca.qc.ircm.smoothing.util.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Window;

import com.google.inject.Injector;

/**
 * Simplifies loading of resources.
 */
public class FxmlResources {
	private static Injector injector;

	public static URL getFxml(Class<?> clazz) {
		return clazz.getResource("/" + clazz.getName().replaceAll("\\.", "/") + ".fxml");
	}

	public static Object loadFxml(Window window, ResourceBundle bundle) {
		return loadFxml(new FXMLLoader(getFxml(window.getClass()), bundle), window);
	}

	public static void loadFxml(Node node, ResourceBundle bundle) {
		FXMLLoader fxmlLoader = new FXMLLoader(getFxml(node.getClass()), bundle);
		fxmlLoader.setRoot(node);
		loadFxml(fxmlLoader, node);
	}

	private static Object loadFxml(FXMLLoader fxmlLoader, Object obj) {
		injectDependencies(obj);
		fxmlLoader.setController(obj);
		try {
			return fxmlLoader.load();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void injectDependencies(Object obj) {
		if (injector != null) {
			injector.injectMembers(obj);
		}
	}

	public static void setInjector(Injector injector) {
		FxmlResources.injector = injector;
	}
}
