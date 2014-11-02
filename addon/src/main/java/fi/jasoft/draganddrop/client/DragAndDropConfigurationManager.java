package fi.jasoft.draganddrop.client;

import java.util.Map;

import com.vaadin.client.ComponentConnector;

import fi.jasoft.draganddrop.client.configurations.AbstractDragAndDropConfiguration;

public interface DragAndDropConfigurationManager {

	public Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> getConfigurations();
		
}
