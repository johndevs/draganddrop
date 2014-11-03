package fi.jasoft.draganddrop.client.rpc;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface DragOverServerRpc extends ServerRpc {
	
	void over(Connector source, Connector dragged, Connector over);
}
