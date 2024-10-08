package ca.qc.ircm.progressbar;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Implementation of {@link ProgressBar} using JavaFX properties.
 */
public class JavafxProgressBar implements ProgressBar {
  private static class StepProgressBar implements ProgressBar {
    private final ProgressBar parent;
    private final double startProgress;
    private final double step;
    private double progress;

    private StepProgressBar(ProgressBar parent, double startProgress, double step) {
      this.parent = parent;
      this.startProgress = startProgress;
      this.step = step;
    }

    @Override
    public double getProgress() {
      return progress;
    }

    @Override
    public void setProgress(double progress) {
      progress = Math.max(0.0, progress);
      progress = Math.min(1.0, progress);
      this.progress = progress;
      parent.setProgress(startProgress + progress * step);
    }

    @Override
    public String getMessage() {
      return parent.getMessage();
    }

    @Override
    public void setMessage(String message) {
      parent.setMessage(message);
    }

    @Override
    public String getTitle() {
      return parent.getTitle();
    }

    @Override
    public void setTitle(String title) {
      parent.setTitle(title);
    }

    @Override
    public ProgressBar step(double step) {
      return new StepProgressBar(this, progress, step);
    }
  }

  /**
   * Progress value (by default between 0 and 1).
   */
  private final DoubleProperty progress = new SimpleDoubleProperty();
  /**
   * Progress message.
   */
  private final StringProperty message = new SimpleStringProperty();
  /**
   * Progress title.
   */
  private final StringProperty title = new SimpleStringProperty();

  public DoubleProperty progress() {
    return progress;
  }

  public StringProperty message() {
    return message;
  }

  public StringProperty title() {
    return title;
  }

  @Override
  public double getProgress() {
    return progress.get();
  }

  @Override
  public void setProgress(double progress) {
    progress = Math.max(0.0, progress);
    progress = Math.min(1.0, progress);
    this.progress.set(progress);
  }

  @Override
  public String getMessage() {
    return message.get();
  }

  @Override
  public void setMessage(String message) {
    this.message.set(message);
  }

  @Override
  public String getTitle() {
    return title.get();
  }

  @Override
  public void setTitle(String title) {
    this.title.set(title);
  }

  @Override
  public ProgressBar step(double step) {
    return new StepProgressBar(this, progress.get(), step);
  }
}
