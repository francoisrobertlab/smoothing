package ca.qc.ircm.smoothing;

import ca.qc.ircm.javafx.SpringAfterburnerInstanceSupplier;
import ca.qc.ircm.smoothing.gui.MainGui;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts program.
 */
@SpringBootApplication
public class Main extends AbstractSpringBootJavafxApplication {
  @Override
  public void init() throws Exception {
    super.init();
    com.airhacks.afterburner.injection.Injector
        .setInstanceSupplier(new SpringAfterburnerInstanceSupplier(applicationContext));
  }

  @Override
  public void start(Stage stage) throws Exception {
    MainGui view = new MainGui();
    notifyPreloader(new ApplicationStarted());
    view.show();
  }

  public static void main(String[] args) {
    System.setProperty("javafx.preloader", MainPreloader.class.getName());
    Application.launch(args);
  }
}
