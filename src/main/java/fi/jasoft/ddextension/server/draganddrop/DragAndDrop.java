package fi.jasoft.ddextension.server.draganddrop;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import fi.jasoft.ddextension.shared.draganddrop.DragAndDropOperations;
import fi.jasoft.ddextension.shared.draganddrop.DragAndDropState;

/**
 * Server side implementation of the component
 */
@SuppressWarnings("serial")
public class DragAndDrop<T extends Component> extends AbstractExtension {
			
	public static <T extends Component> DragAndDrop<T> enable (T component) {
		DragAndDrop<T> dragAndDrop = new DragAndDrop<T>(component);
		dragAndDrop.extend((AbstractClientConnector) component);
		return dragAndDrop;
	}	
	
	private T connector;
	
	private DropHandler<T> dropHandler;
	
	private DragAndDrop(T component) {
		this.connector = component;		
		onDrop(DragAndDropHandlerFactory.getDropHandler(component));
	}
	
	public DragAndDrop<T> disable(DragAndDropOperations operation) {		
		getState().disabled.add(operation);
		return this;
	}
	
	public DragAndDrop<T> enable(DragAndDropOperations operation) {	
		getState().disabled.remove(DragAndDropOperations.ALL);
		getState().disabled.remove(operation);
		return this;
	}
	
	public DragAndDrop<T> from(HasComponents layout) {
		getState().fromLayout = layout;
		return this;
	}
	
	public DragAndDrop<T> from(Component component) {
		getState().fromComponent = component;
		return this;
	}
	
	public DragAndDrop<T> onDrop(DropHandler<T> handler) {
		this.dropHandler = handler;
		dropHandler.setTargetComponent(connector);
		registerRpc(dropHandler.getRpc());		
		return this;
	}
	
	@Override
	protected DragAndDropState getState() {
		return (DragAndDropState) super.getState();
	}
	
	@Override
	protected DragAndDropState getState(boolean markAsDirty) {		
		return (DragAndDropState) super.getState(markAsDirty);
	}
}