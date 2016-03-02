package ca.qc.ircm.smoothing;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import ca.qc.ircm.smoothing.bed.BedParser;
import ca.qc.ircm.smoothing.bed.BedParserBean;
import ca.qc.ircm.smoothing.bed.BedService;
import ca.qc.ircm.smoothing.bed.BedServiceBean;
import ca.qc.ircm.smoothing.service.ExecutableService;
import ca.qc.ircm.smoothing.service.ExecutableServiceBean;
import ca.qc.ircm.smoothing.service.SmoothingService;
import ca.qc.ircm.smoothing.service.SmoothingServiceBean;
import ca.qc.ircm.smoothing.service.SmoothingTask;
import ca.qc.ircm.smoothing.service.SmoothingTaskFactory;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

/**
 * Binds classes needed for application.
 */
public class ApplicationModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(OperatingSystemService.class).to(OperatingSystemServiceBean.class);
    bind(BedParser.class).to(BedParserBean.class);
    bind(BedService.class).to(BedServiceBean.class);
    bind(ExecutableService.class).to(ExecutableServiceBean.class);
    bind(SmoothingService.class).to(SmoothingServiceBean.class);
    bind(Executor.class).to(DefaultExecutor.class);
    install(new FactoryModuleBuilder().implement(SmoothingTask.class, SmoothingTask.class)
        .build(SmoothingTaskFactory.class));
  }

  @Override
  public int hashCode() {
    return ApplicationModule.class.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ApplicationModule;
  }
}
