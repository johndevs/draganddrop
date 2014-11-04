package fi.jasoft.draganddrop.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.Util;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.shared.ui.Connect;

import fi.jasoft.draganddrop.DragAndDrop;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragLeaveEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;
import fi.jasoft.draganddrop.client.configurations.AbstractDragAndDropConfiguration;
import fi.jasoft.draganddrop.client.configurations.DefaultDragAndDropConfiguration;
import fi.jasoft.draganddrop.shared.DragAndDropOperation;
import fi.jasoft.draganddrop.shared.DragAndDropState;

/**
 * Connects the server side component with the client side widget
 */
@SuppressWarnings("serial")
@Connect(DragAndDrop.class)
public class DragAndDropConnector extends AbstractExtensionConnector {
	private static ComponentConnector currentDraggedComponent;
	private static Element dragElement;	
	private static final List<DragAndDropConnector> extensions = new ArrayList<DragAndDropConnector>();	
	private static HandlerRegistration previewHandlerRegistration;
	private static NativePreviewHandler previewHandler = new NativePreviewHandler() {
		
		
		private DragAndDropConnector currentlyOverConnector;
		
		@Override
		public void onPreviewNativeEvent(NativePreviewEvent event) {
			NativeEvent nativeEvent = event.getNativeEvent();
			String type = nativeEvent.getType();
			
			switch(type) {
				case BrowserEvents.MOUSEDOWN:
				case BrowserEvents.TOUCHSTART:{
					DragAndDropConnector targetConnector = getTargetConnector(nativeEvent);
					if(targetConnector != null){
						targetConnector.onMouseDown(nativeEvent);
					}					
				}
				break;
				
				case BrowserEvents.TOUCHMOVE:
				case BrowserEvents.MOUSEMOVE: {
					if(dragElement != null){						
						updateDragImagePosition(nativeEvent);
						
						DragAndDropConnector targetConnector = getTargetConnector(nativeEvent);
						if(targetConnector != currentlyOverConnector) {														
							if(currentlyOverConnector != null){
								currentlyOverConnector.onMouseOut(nativeEvent);			
							}																		
							currentlyOverConnector = targetConnector;
							if(currentlyOverConnector != null){
								currentlyOverConnector.onMouseOver(nativeEvent);
							}
						}
					}
				}
				break;

				case BrowserEvents.DRAGEND:
				case BrowserEvents.MOUSEUP: {
					if(dragElement != null){
						updateDragImagePosition(nativeEvent);										

						Event.releaseCapture(RootPanel.getBodyElement());

						DragAndDropConnector targetConnector = getTargetConnector(nativeEvent);							
						if(targetConnector != null){
							targetConnector.onMouseUp(nativeEvent);
						}							
						currentlyOverConnector = null;

						detachDragElement();						
					}							
				} 
				break;
			}						
		}
	};		

	private NativeEvent dragStartEvent;

	private ComponentConnector targetComponent;

	private final Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> configurations;

	public DragAndDropConnector() {
		DragAndDropConfigurationManager configurationManager = GWT
				.create(DragAndDropConfigurationManagerImpl.class);
		configurations = configurationManager.getConfigurations();
		for (AbstractDragAndDropConfiguration<? extends ComponentConnector> configuration : configurations
				.values()) {
			configuration.setDragAndDropConnector(this);
		}
	}

	@Override
	protected void extend(ServerConnector target) {
		getLogger().info("Registering "+target+" for drag and drop.");
		extensions.add(this);
		if(previewHandlerRegistration == null) {
			previewHandlerRegistration = Event.addNativePreviewHandler(previewHandler);
		}		
		targetComponent = (ComponentConnector) target;		
	}
	
	public ComponentConnector getTargetComponent() {
		return targetComponent;
	}

	@Override
	public void onUnregister() {
		super.onUnregister();
		extensions.remove(this);		
		if(extensions.isEmpty() && previewHandlerRegistration != null) {
			previewHandlerRegistration.removeHandler();
			previewHandlerRegistration = null;
		}		
	}
	
	private boolean isDragAndDropDisabled() {
		return getState().disabled.contains(DragAndDropOperation.ALL);
	}
	
	private boolean isDraggingDisabled() {
		return isDragAndDropDisabled() || getState().disabled.contains(DragAndDropOperation.DRAG);
	}
	
	private boolean isReorderingDisabled() {
		return isDragAndDropDisabled() || getState().disabled.contains(DragAndDropOperation.REORDER);
	}
	
	private boolean isDroppingDisabled() {
		boolean disabled = isDragAndDropDisabled() || getState().disabled.contains(DragAndDropOperation.DROP);
		if(getState().fromLayout != null) {
			// from layout
			disabled |= currentDraggedComponent.getParent() != getState().fromLayout;
		}		
		if(getState().fromComponent != null) {
			// from component
			disabled |= currentDraggedComponent != getState().fromComponent; 
		}
		if(isReorderingDisabled()){
			// re-ordering
			disabled |= getState().fromLayout == targetComponent;
		}		
		return disabled;
	}
	
	protected void onMouseDown(NativeEvent event) {					
		if(isDraggingDisabled()){
			return;
		}		
		
		Element element = Element.as(event.getEventTarget());
		Widget widget = Util.findWidget(element, null);
		ComponentConnector connector = Util.findConnectorFor(widget);
		if(connector == null){
			return;
		}
		
		dragStartEvent = event;
		createDragImage(connector.getWidget().getElement(), true);
		attachDragElement(connector);
		updateDragImagePosition(event);

		currentDraggedComponent = connector;
		getLogger().warning("Started drag");

		Event.setCapture(RootPanel.getBodyElement());

		event.preventDefault();
		event.stopPropagation();
	}

	
	private static DragAndDropConnector getTargetConnector(NativeEvent event) {		
		Node node;
		if(dragElement != null){
			dragElement.getStyle().setDisplay(Display.NONE);	
			node = Util.getElementUnderMouse(event);
			dragElement.getStyle().setDisplay(Display.BLOCK);
			if(node == null){
				return null;
			}
		} else if(event.getEventTarget() != null){
			node = Node.as(event.getEventTarget());
		} else {
			return null;
		}
		return getTargetConnector(node);		
	}
	
	private static DragAndDropConnector getTargetConnector(Node node) {
		assert node != null : "Cannot get connector for null node";
		
		List<DragAndDropConnector> matchingConnectors = new ArrayList<DragAndDropConnector>();
		for (DragAndDropConnector connector : extensions) {						
			if(connector.getTargetComponent().getWidget().getElement().isOrHasChild(node)) {
				matchingConnectors.add(connector);
				
			}
		}		
		
		// Only one matching connector, return it
		if(matchingConnectors.size() == 1){
			return matchingConnectors.get(0);
		}
		
		// Inner connectors with drag and drop enabled, need to search for the inner most child (maximum depth)
		int depth = 0;
		DragAndDropConnector con = null;
		for(int i=0; i<matchingConnectors.size(); i++){
			ServerConnector c = matchingConnectors.get(i);
			int d = 0;
			while(c != null){
				d++;
				c = c.getParent();
			}
			
			if(d > depth){
				depth = d;
				con = matchingConnectors.get(i);
			}					
		}				
		return con;
	}
	
	protected void onMouseUp(NativeEvent event) {
		if(isDroppingDisabled()){
			return;
		}		
		
		AbstractDragAndDropConfiguration<? extends ComponentConnector> configuration = getConfiguration();
		try {
			if (currentDraggedComponent != null) {			
				DropEvent ddEvemt = new DropEvent(targetComponent,
						currentDraggedComponent, event);
				dragElement.getStyle().setDisplay(Display.NONE);
				configuration.drop(ddEvemt);		
			} else {
				getLogger().warning(
						"Could not get drop configuration for "
								+ targetComponent.getClass().getName());
			}	
		} finally {
			currentDraggedComponent = null;
		}	
	}

	protected void onMouseOver(NativeEvent event) {	
		AbstractDragAndDropConfiguration<? extends ComponentConnector> configuration = getConfiguration();
		if (currentDraggedComponent != null) {		
			getLogger().info("Drag over "+targetComponent);
			DragEnterEvent ddEvemt = new DragEnterEvent(targetComponent,
					currentDraggedComponent, targetComponent, event);
			try{
				dragElement.getStyle().setDisplay(Display.NONE);
				configuration.dragEnter(ddEvemt);
			} finally {
				dragElement.getStyle().setDisplay(Display.BLOCK);
			}			
		}		
	}

	protected void onMouseOut(NativeEvent event) {			
		AbstractDragAndDropConfiguration<? extends ComponentConnector> configuration = getConfiguration();
		if (currentDraggedComponent != null) {			
			DragLeaveEvent ddEvemt = new DragLeaveEvent(targetComponent,
					currentDraggedComponent, event);
			try {
				dragElement.getStyle().setDisplay(Display.NONE);
				configuration.dragLeave(ddEvemt);
			} finally {
				dragElement.getStyle().setDisplay(Display.BLOCK);
			}			
		}				
	}
	
	protected AbstractDragAndDropConfiguration<? extends ComponentConnector> getConfiguration() {
		String targetClassName = targetComponent.getClass().getName();
		if (configurations.containsKey(targetClassName)) {
			return configurations.get(targetClassName);
		}			
		
		DefaultDragAndDropConfiguration defaultConf = new DefaultDragAndDropConfiguration();
		defaultConf.setDragAndDropConnector(this);
		return defaultConf;
	}

	private void setDragImage(Element dragImage) {
		setDragImage(dragImage, 0, 0);
	}

	private void setDragImage(Element dragImage, double offsetX, double offsetY) {
		dragElement = dragImage;
		dragElement.getStyle().setMarginLeft(offsetX, Unit.PX);
		dragElement.getStyle().setMarginTop(offsetY, Unit.PX);		
	}

	private void createDragImage(Element element, boolean alignImageToEvent) {
		Element cloneNode = (Element) element.cloneNode(true);
		cloneNode.getStyle().setPosition(Position.ABSOLUTE);
		cloneNode.addClassName("drag-image");
		if (alignImageToEvent) {
			int absoluteTop = element.getAbsoluteTop();
			int absoluteLeft = element.getAbsoluteLeft();
			int clientX = Util.getTouchOrMouseClientX(dragStartEvent);
			int clientY = Util.getTouchOrMouseClientY(dragStartEvent);
			int offsetX = absoluteLeft - clientX;
			int offsetY = absoluteTop - clientY;
			setDragImage(cloneNode, offsetX, offsetY);
		} else {
			setDragImage(cloneNode);
		}
	}

	private void attachDragElement(ComponentConnector connector) {
		assert dragElement != null : "dragElement has not been set";
		Element dragImageParent = VOverlay.getOverlayContainer(connector
				.getConnection());
		dragImageParent.appendChild(dragElement);
		getLogger().warning("Attached drag image " + dragElement);
	}

	private static void detachDragElement() {
		assert dragElement != null : "dragElement has not been set";
		dragElement.removeFromParent();
		dragElement = null;
	}

	private Logger getLogger() {
		return Logger.getLogger("DragAndDrop");
	}

	private static void updateDragImagePosition(NativeEvent event) {
		if (dragElement != null) {
			Style style = dragElement.getStyle();
			int clientY = Util.getTouchOrMouseClientY(event);
			int clientX = Util.getTouchOrMouseClientX(event);
			style.setTop(clientY, Unit.PX);
			style.setLeft(clientX, Unit.PX);
		}
	}

	@Override
	public DragAndDropState getState() {
		return (DragAndDropState) super.getState();
	}
	
	@OnStateChange("disabled")
	private void onDisabledOperationsChange() {
		//TODO
	}
	
}
