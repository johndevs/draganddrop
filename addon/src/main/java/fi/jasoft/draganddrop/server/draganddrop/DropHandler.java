package fi.jasoft.draganddrop.server.draganddrop;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.draganddrop.DragAndDropServerRpc;

@SuppressWarnings("serial")
public abstract class DropHandler<T extends Component>{

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
