package fi.jasoft.ddextension.server.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;
import fi.jasoft.ddextension.client.draganddrop.configurations.VerticalLayoutDragAndDropConfiguration.VerticalLayoutDropHandlerRpc;
import fi.jasoft.ddextension.server.draganddrop.AbstractDropHandler;
import fi.jasoft.ddextension.server.draganddrop.DragAndDropHandler;

@DragAndDropHandler(VerticalLayout.class)
public class VerticalLayoutDropHandler extends AbstractDropHandler<VerticalLayout>{
		
	@SuppressWarnings("serial")
	private VerticalLayoutDropHandlerRpc rpc = new VerticalLayoutDropHandlerRpc() {
		
		@Override
		public void drop(Connector source, Connector dragged) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged); 
			}
		}
		
		@Override
		public void drop(Connector source, Connector dragged, int index, int verticalAlign) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged, index, verticalAlign); 
			}			
		}
	};

	@Override
	protected DragAndDropServerRpc getRpc() {		
		return rpc;
	}
	
	protected void onDrop(Component component, int index, int verticalAlign) {	
		if(getTargetComponent().getComponent(index) == component){
			return;
		}		
		
		getTargetComponent().removeComponent(component);
		
		if(index > -1){
			if(verticalAlign == 3){
				index++;
			}			
			getTargetComponent().addComponent(component, index);		
		} else {
			getTargetComponent().addComponent(component);		
		}		
	}		

	@Override
	protected void onDrop(Component component) {		
		getTargetComponent().addComponent(component);			
	}
}
