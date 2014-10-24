package fi.jasoft.ddextension.client.draganddrop.configurations;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.client.ui.orderedlayout.Slot;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DragLeaveEvent;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.DropEvent;

public class OrderedLayoutDragAndDropConfiguration<T extends AbstractOrderedLayoutConnector> extends
		AbstractDragAndDropConfiguration<T> {

	protected Slot currentSlot;
	
	protected static final String ACTIVE_SLOT = "dd-active-slot";
		
	@Override
	public void dragEnter(DragEnterEvent event) {							
		cleanupCurrentSlot();
		
		currentSlot = getSlot(event.getEvent());
		
		if(currentSlot != null){
			currentSlot.addStyleName(ACTIVE_SLOT);		
		}	
	}
	
	@Override
	public void dragLeave(DragLeaveEvent event) {		
		cleanupCurrentSlot();	
	}
	
	@Override
	public void drop(DropEvent event) {	
		cleanupCurrentSlot();
	}
	
	protected void cleanupCurrentSlot(){
		if(currentSlot != null){
			currentSlot.removeStyleName(ACTIVE_SLOT);			
		}
	}
	
	protected Slot getSlot(NativeEvent event) {	
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
	
	protected int getSlotIndex(AbstractOrderedLayoutConnector connector, NativeEvent event) {	
		Slot slot = getSlot(event);
		if(slot == null) {
			// Over layout
			return -1;
		}
		return connector.getWidget().getWidgetIndex(slot);
	}
}
