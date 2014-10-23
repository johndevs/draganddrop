package fi.jasoft.ddextension.client.draganddrop;

import java.util.Map;

import com.vaadin.client.ComponentConnector;

import fi.jasoft.ddextension.client.draganddrop.configurations.AbstractDragAndDropConfiguration;

public interface DragAndDropConfigurationManager {

	public Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> getConfigurations();
		
}
