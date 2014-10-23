package fi.jasoft.ddextension.server.draganddrop.handlers;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.ddextension.server.draganddrop.AbstractDropHandler;

public class DefaultDropHandler extends AbstractDropHandler<AbstractClientConnector>{

	@Override
	protected void onDrop(Component component) {
		// Don't do anything
	}
}
