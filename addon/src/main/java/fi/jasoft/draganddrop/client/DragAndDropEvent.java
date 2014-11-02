package fi.jasoft.draganddrop.client;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.ComponentConnector;
import com.vaadin.shared.Connector;

public abstract class DragAndDropEvent {

	public static class DropEvent extends DragAndDropEvent {
		public DropEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, NativeEvent event) {
			super(targetConnector, draggedConnector, event);			
		}						
	}
	
	public static class DragEnterEvent extends DragAndDropEvent {
		
		private ComponentConnector over;
		
		public DragEnterEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, ComponentConnector over, NativeEvent event) {
			super(targetConnector, draggedConnector, event);
			this.over = over;
		}		
		
		public ComponentConnector getOverConnector() {
			return this.over;
		}
	}
	
	public static class DragLeaveEvent extends DragAndDropEvent {
		public DragLeaveEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, NativeEvent event) {
			super(targetConnector, draggedConnector, event);
		}		
	}
			
	private ComponentConnector targetConnector;
	
	private ComponentConnector draggedConnector;
	
	private NativeEvent event;
	
	public DragAndDropEvent(ComponentConnector targetConnector,
			ComponentConnector draggedConnector, NativeEvent event) {
		super();
		this.targetConnector = targetConnector;
		this.draggedConnector = draggedConnector;
		this.event = event;
	}
	
	public ComponentConnector getTargetConnector() {
		return targetConnector;
	}

	public ComponentConnector getDraggedConnector() {
		return draggedConnector;
	}

	public NativeEvent getEvent() {
		return event;
	}

}
