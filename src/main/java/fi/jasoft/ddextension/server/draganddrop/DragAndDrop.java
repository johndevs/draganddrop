package fi.jasoft.ddextension.server.draganddrop;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;

/**
 * Server side implementation of the component
 */
@SuppressWarnings("serial")
public class DragAndDrop<T extends AbstractClientConnector> extends AbstractExtension {
			
	public static <T extends AbstractClientConnector> DragAndDrop<T> enable (T component) {
		DragAndDrop<T> dragAndDrop = new DragAndDrop<T>(component);
		dragAndDrop.extend(component);
		return dragAndDrop;
	}	
	
	private T connector;
	
	private AbstractDropHandler<T> dropHandler;
	
	private DragAndDrop(T component) {
		this.connector = component;		
		drop(DragAndDropHandlerFactory.getDropHandler(component));
	}
	
	public DragAndDrop<T> disable() {
		//TODO
		return this;
	}
	
	public DragAndDrop<T> drop(AbstractDropHandler<T> handler) {
		this.dropHandler = handler;
		dropHandler.setTargetComponent(connector);
		registerRpc(dropHandler.getRpc());		
		return this;
	}
}