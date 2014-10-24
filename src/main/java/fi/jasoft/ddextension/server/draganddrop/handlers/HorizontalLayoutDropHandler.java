package fi.jasoft.ddextension.server.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;
import fi.jasoft.ddextension.client.draganddrop.configurations.HorizontalLayoutDragAndDropConfiguration.Alignment;
import fi.jasoft.ddextension.client.draganddrop.configurations.HorizontalLayoutDragAndDropConfiguration.HorizontalLayoutDropHandlerRpc;
import fi.jasoft.ddextension.server.draganddrop.DropHandler;
import fi.jasoft.ddextension.server.draganddrop.DragAndDropHandler;

@DragAndDropHandler(HorizontalLayout.class)
public class HorizontalLayoutDropHandler extends DropHandler<HorizontalLayout>{
		
	@SuppressWarnings("serial")
	private HorizontalLayoutDropHandlerRpc rpc = new HorizontalLayoutDropHandlerRpc() {
		
		@Override
		public void drop(Connector source, Connector dragged) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged); 
			}
		}
		
		@Override
		public void drop(Connector source, Connector dragged, int index, Alignment horizontalAlign) {
			if(source == getTargetComponent()) {
				onDrop((Component) dragged, index, horizontalAlign); 
			}			
		}
	};

	@Override
	protected DragAndDropServerRpc getRpc() {		
		return rpc;
	}
	
	protected void onDrop(Component component, int index, Alignment horizontalAlign) {	
		if(getTargetComponent().getComponent(index) == component){
			return;
		}		
		
		getTargetComponent().removeComponent(component);
		
		if(index > -1){
			if(horizontalAlign != Alignment.LEFT){
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
