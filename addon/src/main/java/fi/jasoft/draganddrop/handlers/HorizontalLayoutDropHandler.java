package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import fi.jasoft.draganddrop.DragAndDropHandler;
import fi.jasoft.draganddrop.client.configurations.HorizontalLayoutDragAndDropConfiguration.Alignment;
import fi.jasoft.draganddrop.client.configurations.HorizontalLayoutDragAndDropConfiguration.HorizontalLayoutDropHandlerRpc;

@DragAndDropHandler(HorizontalLayout.class)
public class HorizontalLayoutDropHandler extends DropHandler<HorizontalLayout>{
		
	@SuppressWarnings("serial")
	private HorizontalLayoutDropHandlerRpc rpc = new HorizontalLayoutDropHandlerRpc() {
		
		@Override
		public void drop(Connector source, Connector dragged) {
			if(source == getSource()) {
				onDrop((Component) dragged); 
			}
		}
		
		@Override
		public void drop(Connector source, Connector dragged, int index, Alignment horizontalAlign) {
			if(source == getSource()) {
				onDrop((Component) dragged, index, horizontalAlign); 
			}			
		}
	};

	@Override
	public HorizontalLayoutDropHandlerRpc getRpc() {		
		return rpc;
	}
	
	protected void onDrop(Component component, int index, Alignment horizontalAlign) {	
		if(getSource().getComponent(index) == component){
			return;
		}		
		
		getSource().removeComponent(component);
		
		if(index > -1){
			if(horizontalAlign != Alignment.LEFT){
				index++;
			} 					
			getSource().addComponent(component, index);		
		} else {
			getSource().addComponent(component);		
		}		
	}		

	@Override
	protected void onDrop(Component component) {		
		getSource().addComponent(component);			
	}
}
