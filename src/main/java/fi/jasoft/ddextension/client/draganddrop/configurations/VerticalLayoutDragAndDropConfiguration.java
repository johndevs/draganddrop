package fi.jasoft.ddextension.client.draganddrop.configurations;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropConfiguration;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DragLeaveEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DropEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;
import fi.jasoft.ddextension.server.draganddrop.handlers.VerticalLayoutDropHandler;

@DragAndDropConfiguration(VerticalLayoutDropHandler.class)
public class VerticalLayoutDragAndDropConfiguration extends AbstractDragAndDropConfiguration<VerticalLayoutConnector> {

	public interface VerticalLayoutDropHandlerRpc extends DragAndDropServerRpc {		
		public void drop(Connector source, Connector dragged, int index, int verticalAlign);		
	}	
	
	private Slot currentSlot;
	
	private static final String ACTIVE_SLOT = "dd-active-slot";
		
	@Override
	public void dragEnter(DragEnterEvent event) {							
		cleanupCurrentSlot();
		
		VerticalLayoutConnector connector = (VerticalLayoutConnector) event.getTargetConnector();
		currentSlot = getSlot(connector, event.getEvent());
		
		if(currentSlot != null){
			currentSlot.addStyleName(ACTIVE_SLOT);
			int verticalAlign = getVerticalAlign(connector, event.getEvent());
			switch(verticalAlign){
			case 1: currentSlot.addStyleName(ACTIVE_SLOT+"-top"); break;
			case 2: currentSlot.addStyleName(ACTIVE_SLOT+"-middle"); break;
			case 3: currentSlot.addStyleName(ACTIVE_SLOT+"-bottom"); break;
			}
		}	
	}
	
	@Override
	public void dragLeave(DragLeaveEvent event) {		
		cleanupCurrentSlot();	
	}
	
	@Override
	public void drop(DropEvent event) {	
		cleanupCurrentSlot();
	
		VerticalLayoutConnector connector = (VerticalLayoutConnector) event.getTargetConnector();
		int slotIndex = getSlotIndex(connector, event.getEvent());
		int verticalAlign = getVerticalAlign(connector, event.getEvent());		
		getRpcProxy(VerticalLayoutDropHandlerRpc.class).drop(connector, event.getDraggedConnector(), slotIndex, verticalAlign);
	}
	
	private void cleanupCurrentSlot(){
		if(currentSlot != null){
			currentSlot.removeStyleName(ACTIVE_SLOT);
			currentSlot.removeStyleName(ACTIVE_SLOT+"-top");
			currentSlot.removeStyleName(ACTIVE_SLOT+"-middle");
			currentSlot.removeStyleName(ACTIVE_SLOT+"-bottom");
		}
	}
	
	private Slot getSlot(VerticalLayoutConnector connector, NativeEvent event) {	
		Element e = Util.getElementUnderMouse(event);
		assert e != null: "Event target was null";		
		Widget w = Util.findWidget(e, null);		
		assert w != null: "Widget was not found";
		
		if(w != null) {
			while(!(w instanceof Slot) && w != null) {
				w = w.getParent();
			}
		}				
		
		return (Slot) w;		
	}
	
	private int getSlotIndex(VerticalLayoutConnector connector, NativeEvent event) {	
		Slot slot = getSlot(connector, event);
		if(slot == null) {
			// Over layout
			return -1;
		}
		return connector.getWidget().getWidgetIndex(slot);
	}
	
	private int getVerticalAlign(VerticalLayoutConnector connector, NativeEvent event) {
		Slot slot = getSlot(connector, event);
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
