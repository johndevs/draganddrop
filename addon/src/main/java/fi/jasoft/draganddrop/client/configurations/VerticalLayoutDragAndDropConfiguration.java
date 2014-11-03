package fi.jasoft.draganddrop.client.configurations;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.draganddrop.client.DragAndDropConfiguration;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;
import fi.jasoft.draganddrop.client.rpc.DropServerRpc;
import fi.jasoft.draganddrop.handlers.VerticalLayoutDropHandler;

@DragAndDropConfiguration(VerticalLayoutDropHandler.class)
public class VerticalLayoutDragAndDropConfiguration extends OrderedLayoutDragAndDropConfiguration<VerticalLayoutConnector> {

	public interface VerticalLayoutDropHandlerRpc extends DropServerRpc {		
		public void drop(Connector source, Connector dragged, int index, Alignment verticalAlign);		
	}	
	
	public enum Alignment {
		TOP, MIDDLE, BOTTOM
	}
		
	@Override
	public void dragEnter(DragEnterEvent event) {			
		super.dragEnter(event);				
		if(currentSlot != null){			
			Alignment verticalAlign = getVerticalAlign(event.getEvent());
			switch(verticalAlign){
			case TOP: currentSlot.addStyleName(ACTIVE_SLOT+"-top"); break;
			case MIDDLE: currentSlot.addStyleName(ACTIVE_SLOT+"-middle"); break;
			case BOTTOM: currentSlot.addStyleName(ACTIVE_SLOT+"-bottom"); break;
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
		Alignment verticalAlign = getVerticalAlign(event.getEvent());		
		getRpcProxy(VerticalLayoutDropHandlerRpc.class).drop(connector, event.getDraggedConnector(), slotIndex, verticalAlign);
	}
				
	private Alignment getVerticalAlign(NativeEvent event) {
		Slot slot = getSlot(event);
		if(slot == null){
			// Over layout
			return Alignment.MIDDLE;
		}
		
		int absoluteTop = slot.getAbsoluteTop();
		int fromTop = Util.getTouchOrMouseClientY(event) - absoluteTop;		
		float percentageFromTop = (fromTop / (float) slot.getOffsetHeight());		
		if (percentageFromTop < 0.25) {
		  return Alignment.TOP;
		} else if (percentageFromTop > 1 - 0.25) {
		  return Alignment.BOTTOM;
		} else {
		  return Alignment.MIDDLE;
		}		
	}
}
