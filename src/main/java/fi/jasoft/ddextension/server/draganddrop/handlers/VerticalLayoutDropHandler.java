package fi.jasoft.ddextension.server.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;
import fi.jasoft.ddextension.client.draganddrop.configurations.VerticalLayoutDragAndDropConfiguration.Alignment;
import fi.jasoft.ddextension.client.draganddrop.configurations.VerticalLayoutDragAndDropConfiguration.VerticalLayoutDropHandlerRpc;
import fi.jasoft.ddextension.server.draganddrop.DropHandler;
import fi.jasoft.ddextension.server.draganddrop.DragAndDropHandler;

@DragAndDropHandler(VerticalLayout.class)
public class VerticalLayoutDropHandler extends DropHandler<VerticalLayout>{
		
	@SuppressWarnings("serial")
	private VerticalLayoutDropHandlerRpc rpc = new VerticalLayoutDropHandlerRpc() {
		
		@Override
		public void drop(Connector source, Connector dragged) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged); 
			}
		}
		
		@Override
		public void drop(Connector source, Connector dragged, int index, Alignment verticalAlign) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged, index, verticalAlign); 
			}			
		}
	};

	@Override
	protected DragAndDropServerRpc getRpc() {		
		return rpc;
	}
	
	protected void onDrop(Component component, int index, Alignment verticalAlign) {	
		if(getTargetComponent().getComponent(index) == component){
			return;
		}						
		if(index > -1){
			if(verticalAlign != Alignment.TOP){
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
