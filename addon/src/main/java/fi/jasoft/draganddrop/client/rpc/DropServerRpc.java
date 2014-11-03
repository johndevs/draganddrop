package fi.jasoft.draganddrop.client.rpc;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface DropServerRpc extends ServerRpc {
	
	void drop(Connector source, Connector dragged);
}
