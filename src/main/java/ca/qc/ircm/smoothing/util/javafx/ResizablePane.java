package ca.qc.ircm.smoothing.util.javafx;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * {@link Pane} that the user can resize by selecting it's edges.
 */
@DefaultProperty("content")
public class ResizablePane extends StackPane {
	private class ContentListener implements ChangeListener<Node> {
		@Override
		public void changed(ObservableValue<? extends Node> observableValue, Node oldValue, Node newValue) {
			container.getChildren().clear();
			if (newValue != null) {
				container.getChildren().add(newValue);
			}
		}
	}

	private class ResizeHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (resizeVerticallyProperty.get()) {
				setPrefHeight(resize.getBoundsInParent().getMinY() + event.getY() + 5);
			}
			if (resizeHorizontallyProperty.get()) {
				setPrefWidth(resize.getBoundsInParent().getMinX() + event.getX() + 5);
			}
			if (resizeVerticallyProperty.get() || resizeHorizontallyProperty.get()) {
				requestLayout();
			}
			event.consume();
		}
	}

	private final ObjectProperty<Node> contentProperty = new SimpleObjectProperty<Node>();
	private final BooleanProperty resizeVerticallyProperty = new SimpleBooleanProperty();
	private final BooleanProperty resizeHorizontallyProperty = new SimpleBooleanProperty();
	private final ResourceBundle bundle;
	@FXML
	private Pane container;
	@FXML
	private Node resize;

	public ResizablePane() {
		bundle = ResourceBundle.getBundle(getClass().getName(), Locale.getDefault());

		FxmlResources.loadFxml(this, bundle);

		contentProperty.addListener(new ContentListener());
		resize.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (resizeVerticallyProperty.get() && resizeHorizontallyProperty.get()) {
					setCursor(Cursor.SE_RESIZE);
				} else if (resizeVerticallyProperty.get()) {
					setCursor(Cursor.S_RESIZE);
				} else if (resizeHorizontallyProperty.get()) {
					setCursor(Cursor.E_RESIZE);
				} else {
					setCursor(null);
				}
			}
		});
		resize.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(null);
			}
		});
		resize.setOnMouseDragged(new ResizeHandler());

		// Default values.
		resizeVerticallyProperty.set(true);
		resizeHorizontallyProperty.set(true);
	}

	public ObjectProperty<Node> contentProperty() {
		return contentProperty;
	}

	public BooleanProperty resizeVerticallyProperty() {
		return resizeVerticallyProperty;
	}

	public BooleanProperty resizeHorizontallyProperty() {
		return resizeHorizontallyProperty;
	}

	public Node getContent() {
		return contentProperty.get();
	}

	public void setContent(Node content) {
		contentProperty.set(content);
	}

	public boolean isResizeVertically() {
		return resizeVerticallyProperty.get();
	}

	public void setResizeVertically(boolean resizeVertically) {
		resizeVerticallyProperty.set(resizeVertically);
	}

	public boolean isResizeHorizontally() {
		return resizeHorizontallyProperty.get();
	}

	public void setResizeHorizontally(boolean resizeHorizontally) {
		resizeHorizontallyProperty.set(resizeHorizontally);
	}
}
