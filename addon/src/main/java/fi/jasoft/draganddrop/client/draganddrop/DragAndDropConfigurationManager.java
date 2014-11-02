package fi.jasoft.draganddrop.client.draganddrop;

import java.util.Map;

import com.vaadin.client.ComponentConnector;

import fi.jasoft.draganddrop.client.draganddrop.configurations.AbstractDragAndDropConfiguration;

public interface DragAndDropConfigurationManager {

	public Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> getConfigurations();
		
}
