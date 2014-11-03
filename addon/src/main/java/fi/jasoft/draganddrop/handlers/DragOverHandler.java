package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.rpc.DragOverServerRpc;

public abstract class DragOverHandler<T extends Component> extends AbstractHandler<T>{

	protected abstract void onOver(Component component);
	
	@Override
	public DragOverServerRpc getRpc() {
		return new DragOverServerRpc() {
			
			@Override
			public void over(Connector source, Connector dragged, Connector over) {
				onOver((Component)dragged);
			}
		};
	}
}
