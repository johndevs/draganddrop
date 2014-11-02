package fi.jasoft.draganddrop;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.handlers.DefaultDropHandler;
import fi.jasoft.draganddrop.handlers.DropHandler;
import fi.jasoft.draganddrop.handlers.HorizontalLayoutDropHandler;
import fi.jasoft.draganddrop.handlers.VerticalLayoutDropHandler;

public final class DragAndDropHandlerFactory {

	private static final List<Class<? extends DropHandler<?>>> handlers = new ArrayList<Class<? extends DropHandler<?>>>();
	static {
		register(VerticalLayoutDropHandler.class);
		register(HorizontalLayoutDropHandler.class);
	}
	
	private DragAndDropHandlerFactory() {
		// Factory
	}	
	
	public static final void register(Class<? extends DropHandler<?>> dropHandlerClass) {
		handlers.add(dropHandlerClass);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends Component> DropHandler<T> getDropHandler(T connector) {
		for (Class<? extends DropHandler<?>> dropHandlerClass : handlers) {
			DragAndDropHandler dropHandlerAnnotation = dropHandlerClass.getAnnotation(DragAndDropHandler.class);
			Class<? extends Connector> connectorClass = dropHandlerAnnotation.value();
			if(connectorClass == connector.getClass()){
				try {
					return (DropHandler<T>) dropHandlerClass.newInstance();
				} catch (InstantiationException e) {					
				} catch (IllegalAccessException e) {				
				}
			}						
		}
		return (DropHandler<T>) new DefaultDropHandler();
	}
}
