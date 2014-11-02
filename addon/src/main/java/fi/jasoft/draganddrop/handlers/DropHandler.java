package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.DragAndDropServerRpc;

@SuppressWarnings("serial")
public abstract class DropHandler<T extends Component> extends AbstractHandler<T>{
	
	protected abstract void onDrop(Component component);
	
	@Override
	public DragAndDropServerRpc getRpc() {
		return new DragAndDropServerRpcAdapter(){
			@Override
			public void drop(Connector source, Connector dragged) {
				onDrop((Component) dragged);			
			}
		};
	}	
}
