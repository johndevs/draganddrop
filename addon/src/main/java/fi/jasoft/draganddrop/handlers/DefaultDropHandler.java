package fi.jasoft.draganddrop.handlers;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.draganddrop.DropHandler;

public class DefaultDropHandler extends DropHandler<Component>{

	@Override
	protected void onDrop(Component component) {
		// Don't do anything
	}
}