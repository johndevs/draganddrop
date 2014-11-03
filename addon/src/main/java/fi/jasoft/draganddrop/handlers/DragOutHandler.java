package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.rpc.DragOutServerRpc;

public abstract class DragOutHandler<T extends Component> extends AbstractHandler<T>{

	protected abstract void onOut(Component component);
	
	@Override
	public DragOutServerRpc getRpc() {
		return new DragOutServerRpc() {
			
			@Override
			public void out(Connector source, Connector dragged, Connector over) {
				onOut((Component)dragged);				
			}
		};
	}
}
