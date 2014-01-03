package ca.qc.ircm.smoothing.util.javafx;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * {@link ScrollPane} that grows based on it's content size.
 */
public class GrowingScrollPane extends ScrollPane {
	@Override
	protected double computePrefWidth(double height) {
		Node content = this.getContent();
		content.autosize();
		return content.getLayoutBounds().getWidth() + 2;
	}

	@Override
	protected double computePrefHeight(double width) {
		Node content = this.getContent();
		content.autosize();
		return content.getLayoutBounds().getHeight() + 2;
	}
}
