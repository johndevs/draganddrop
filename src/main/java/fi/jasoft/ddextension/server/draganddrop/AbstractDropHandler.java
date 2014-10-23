package fi.jasoft.ddextension.server.draganddrop;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.Component;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;

@SuppressWarnings("serial")
public abstract class AbstractDropHandler<T extends AbstractClientConnector>{

	private T targetComponent;
	
	public void setTargetComponent(T targetComponent) {
		this.targetComponent = targetComponent;
	}
	
	public T getTargetComponent() {
		return targetComponent;
	}
	
	protected abstract void onDrop(Component component);
	
	protected DragAndDropServerRpc getRpc() {
		return new DragAndDropServerRpc() {

			@Override
			public void drop(Connector source, Connector dragged) {
				onDrop((Component) dragged);				
			}
		};
	}
	
}
