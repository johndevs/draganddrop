package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.rpc.DropServerRpc;

@SuppressWarnings("serial")
public abstract class DropHandler<T extends Component> extends AbstractHandler<T>{
			
	protected abstract void onDrop(Component component);
	
	@Override
	public DropServerRpc getRpc() {
		return new DropServerRpc() {
			
			@Override
			public void drop(Connector source, Connector dragged) {
				onDrop((Component)dragged);				
			}
		};
	}
}
