package fi.jasoft.ddextension.client.draganddrop.configurations;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropConfiguration;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DropEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;
import fi.jasoft.ddextension.server.draganddrop.handlers.VerticalLayoutDropHandler;

@DragAndDropConfiguration(VerticalLayoutDropHandler.class)
public class VerticalLayoutDragAndDropConfiguration extends OrderedLayoutDragAndDropConfiguration<VerticalLayoutConnector> {

	public interface VerticalLayoutDropHandlerRpc extends DragAndDropServerRpc {		
		public void drop(Connector source, Connector dragged, int index, int verticalAlign);		
	}	
		
	@Override
	public void dragEnter(DragEnterEvent event) {			
		super.dragEnter(event);				
		if(currentSlot != null){			
			int verticalAlign = getVerticalAlign(event.getEvent());
			switch(verticalAlign){
			case 1: currentSlot.addStyleName(ACTIVE_SLOT+"-top"); break;
			case 2: currentSlot.addStyleName(ACTIVE_SLOT+"-middle"); break;
			case 3: currentSlot.addStyleName(ACTIVE_SLOT+"-bottom"); break;
			}
		}	
	}
	
	@Override
	protected void cleanupCurrentSlot() {		
		super.cleanupCurrentSlot();
		if(currentSlot != null) {
			currentSlot.removeStyleName(ACTIVE_SLOT+"-top");
			currentSlot.removeStyleName(ACTIVE_SLOT+"-middle"); 
			currentSlot.removeStyleName(ACTIVE_SLOT+"-bottom");
		}
	}
	
	@Override
	public void drop(DropEvent event) {	
		super.drop(event);	
		VerticalLayoutConnector connector = (VerticalLayoutConnector) event.getTargetConnector();
		int slotIndex = getSlotIndex(connector, event.getEvent());
		int verticalAlign = getVerticalAlign(event.getEvent());		
		getRpcProxy(VerticalLayoutDropHandlerRpc.class).drop(connector, event.getDraggedConnector(), slotIndex, verticalAlign);
	}
				
	private int getVerticalAlign(NativeEvent event) {
		Slot slot = getSlot(event);
		if(slot == null){
			// Over layout
			return -1;
		}
		
		int absoluteTop = slot.getAbsoluteTop();
		int fromTop = Util.getTouchOrMouseClientY(event) - absoluteTop;
		
		float percentageFromTop = (fromTop / (float) slot.getOffsetHeight());		
		if (percentageFromTop < 0.25) {
		  return 1;
		} else if (percentageFromTop > 1 - 0.25) {
		  return 3;
		} else {
		  return 2;
		}		
	}
}
