package fi.jasoft.draganddrop;

import java.util.Arrays;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import fi.jasoft.draganddrop.handlers.DragOutHandler;
import fi.jasoft.draganddrop.handlers.DropHandler;
import fi.jasoft.draganddrop.handlers.DragOverHandler;
import fi.jasoft.draganddrop.shared.DragAndDropOperation;
import fi.jasoft.draganddrop.shared.DragAndDropState;

/**
 * Drag & Drop extension for enabling drag and drop operations on any component
 */
@SuppressWarnings("serial")
public class DragAndDrop<T extends Component> extends AbstractExtension {
			
	/**
	 * Enables drag & drop for component. Allows all drag & drop operations on the component
	 * 
	 * @param component
	 * 		The component to enable drag & drop operations on
	 * @return
	 * 		A configurator for configuring the drag & drop behaviour
	 */
	public static <T extends Component> DragAndDropConfigurator<T> enable (T component) {
		DragAndDrop<T> dragAndDrop = new DragAndDrop<T>();
		dragAndDrop.extend((AbstractClientConnector) component);
		return new DragAndDropConfigurator<T>(component, dragAndDrop);
	}	
	
	/**
	 * Enables drag & drop for component. Allows only one kind of operation to be performed on the component.
	 * 
	 * @param component
	 * 		The component to enable drag & drop operations on
	 * @return
	 * 		A configurator for configuring the drag & drop behaviour
	 */
	public static <T extends Component> DragAndDropConfigurator<T> enable (T component, DragAndDropOperation operation) {
		DragAndDropConfigurator<T> configurator = enable(component);		
		for (DragAndDropOperation op : Arrays.asList(DragAndDropOperation.values())) {
			if(op != DragAndDropOperation.ALL && op != operation){
				configurator.disable(op);
			}
		}
		return configurator;
	}	
	
	/**
	 * Configurator for drag & drop operations
	 *
	 * @param <T>
	 * 		The target component type
	 */
	public static class DragAndDropConfigurator<T extends Component> {

		private T connector;
		
		private DropHandler<T> dropHandler;
		
		private DragOverHandler<T> overHandler;
		
		private DragOutHandler<T> outHandler;
		
		private DragAndDrop<T> extension;
		
		private DragAndDropConfigurator(T component, DragAndDrop<T> extension) {
			this.connector = component;		
			this.extension = extension;
			onDrop(DragAndDropHandlerFactory.getDropHandler(component));
		}
		
		/**
		 * Disables a drag & drop operation for the component.
		 * 
		 * @param operation
		 * 		The operation to disable
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> disable(DragAndDropOperation operation) {		
			extension.getState().disabled.add(operation);
			return this;
		}
		
		/**
		 * Enables a drag & drop operation for the component
		 * 
		 * @param operation
		 * 		The operation to enable
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> enable(DragAndDropOperation operation) {	
			extension.getState().disabled.remove(DragAndDropOperation.ALL);
			extension.getState().disabled.remove(operation);
			return this;
		}
		
		/**
		 * Only allow dropping components from this layout
		 * 
		 * @param layout
		 * 		The layout to only allow drops from		 * 
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> from(HasComponents layout) {
			extension.getState().fromLayout = layout;
			return this;
		}
		
		/**
		 * Only allow dropping components from this component
		 * 
		 * @param component
		 * 		The component to allow to be dropped
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> from(Component component) {
			extension.getState().fromComponent = component;
			return this;
		}
		
		/**
		 * Sets the drop handler for handling drops on the component
		 * 
		 * @param handler
		 * 		The handler to handle drops		 * 
		 * @return	
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> onDrop(DropHandler<T> handler) {
			this.dropHandler = handler;
			dropHandler.setSource(connector);
			extension.registerRpc(dropHandler.getRpc());		
			return this;
		}
		
		/**
		 * Sets the drag over handler to handle drag over events on the component
		 * 
		 * @param handler
		 * 		The handler to handle drag over events
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> onOver(DragOverHandler<T> handler) {
			this.overHandler = handler;
			overHandler.setSource(connector);
			extension.registerRpc(overHandler.getRpc());
			return this;
		}
		
		/**
		 * Sets the drag out handler to handler drag out events on the component
		 * 
		 * @param handler
		 * 		The handler to handler drag out events		 * 		
		 * @return
		 * 		A configurator for configuring the drag & drop behavior
		 */
		public DragAndDropConfigurator<T> onOut(DragOutHandler<T> handler) {
			this.outHandler = handler;
			outHandler.setSource(connector);
			extension.registerRpc(outHandler.getRpc());
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