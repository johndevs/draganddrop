package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.DragAndDropServerRpc;

public abstract class AbstractHandler<T extends Component> {

	public static class DragAndDropServerRpcAdapter implements DragAndDropServerRpc {

		@Override
		public void drop(Connector source, Connector dragged) {				
		}

		@Override
		public void over(Connector source, Connector dragged, Connector over) {					
		}
	}
	
	private T targetComponent;
	
	public void setTargetComponent(T targetComponent) {
		this.targetComponent = targetComponent;
	}
	
	public T getTargetComponent() {
		return targetComponent;
	}
	
	public abstract DragAndDropServerRpc getRpc();
}
