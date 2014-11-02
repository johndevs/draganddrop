package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.DragAndDropServerRpc;

public abstract class DragOverHandler<T extends Component> extends AbstractHandler<T>{

	protected abstract void onOver(Component component);
	
	@Override
	public DragAndDropServerRpc getRpc() {
		return new DragAndDropServerRpcAdapter(){
			@Override
			public void over(Connector source, Connector dragged, Connector over) {
				onOver((Component)over);		
			}
		};
	}	
}
