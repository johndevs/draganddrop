package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.draganddrop.DragAndDropHandler;
import fi.jasoft.draganddrop.client.DragAndDropServerRpc;
import fi.jasoft.draganddrop.client.configurations.VerticalLayoutDragAndDropConfiguration.Alignment;
import fi.jasoft.draganddrop.client.configurations.VerticalLayoutDragAndDropConfiguration.VerticalLayoutDropHandlerRpc;

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

		@Override
		public void over(Connector source, Connector dragged, Connector over) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public DragAndDropServerRpc getRpc() {		
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
