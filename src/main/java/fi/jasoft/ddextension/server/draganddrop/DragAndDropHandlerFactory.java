package fi.jasoft.ddextension.server.draganddrop;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.ddextension.server.draganddrop.handlers.DefaultDropHandler;
import fi.jasoft.ddextension.server.draganddrop.handlers.VerticalLayoutDropHandler;

public final class DragAndDropHandlerFactory {

	private static final List<Class<? extends AbstractDropHandler<?>>> handlers = new ArrayList<Class<? extends AbstractDropHandler<?>>>();
	static {
		register(VerticalLayoutDropHandler.class);
	}
	
	private DragAndDropHandlerFactory() {
		// Factory
	}	
	
	public static final void register(Class<? extends AbstractDropHandler<?>> dropHandlerClass) {
		handlers.add(dropHandlerClass);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends AbstractClientConnector> AbstractDropHandler<T> getDropHandler(T connector) {
		for (Class<? extends AbstractDropHandler<?>> dropHandlerClass : handlers) {
			DragAndDropHandler dropHandlerAnnotation = dropHandlerClass.getAnnotation(DragAndDropHandler.class);
			Class<? extends Connector> connectorClass = dropHandlerAnnotation.value();
			if(connectorClass == connector.getClass()){
				try {
					return (AbstractDropHandler<T>) dropHandlerClass.newInstance();
				} catch (InstantiationException e) {					
				} catch (IllegalAccessException e) {				
				}
			}						
		}
		return (AbstractDropHandler<T>) new DefaultDropHandler();
	}
}
