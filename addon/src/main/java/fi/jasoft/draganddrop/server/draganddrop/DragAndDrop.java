package fi.jasoft.draganddrop.server.draganddrop;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import fi.jasoft.draganddrop.shared.draganddrop.DragAndDropOperation;
import fi.jasoft.draganddrop.shared.draganddrop.DragAndDropState;

/**
 * Drag & Drop extension for enabling drag and drop operations on any component
 */
@SuppressWarnings("serial")
public class DragAndDrop<T extends Component> extends AbstractExtension {
			
	public static <T extends Component> DragAndDropConfigurator<T> enable (T component) {
		DragAndDrop<T> dragAndDrop = new DragAndDrop<T>();
		dragAndDrop.extend((AbstractClientConnector) component);
		return new DragAndDropConfigurator<T>(component, dragAndDrop);
	}	
	
	public static class DragAndDropConfigurator<T extends Component> {

		private T connector;
		
		private DropHandler<T> dropHandler;
		
		private DragAndDrop<T> extension;
		
		private DragAndDropConfigurator(T component, DragAndDrop<T> extension) {
			this.connector = component;		
			this.extension = extension;
			onDrop(DragAndDropHandlerFactory.getDropHandler(component));
		}
		
		public DragAndDropConfigurator<T> disable(DragAndDropOperation operation) {		
			extension.getState().disabled.add(operation);
			return this;
		}
		
		public DragAndDropConfigurator<T> enable(DragAndDropOperation operation) {	
			extension.getState().disabled.remove(DragAndDropOperation.ALL);
			extension.getState().disabled.remove(operation);
			return this;
		}
		
		public DragAndDropConfigurator<T> from(HasComponents layout) {
			extension.getState().fromLayout = layout;
			return this;
		}
		
		public DragAndDropConfigurator<T> from(Component component) {
			extension.getState().fromComponent = component;
			return this;
		}
		
		public DragAndDropConfigurator<T> onDrop(DropHandler<T> handler) {
			this.dropHandler = handler;
			dropHandler.setTargetComponent(connector);
			extension.registerRpc(dropHandler.getRpc());		
			return this;
		}
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