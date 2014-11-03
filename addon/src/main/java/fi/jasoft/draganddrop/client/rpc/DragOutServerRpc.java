package fi.jasoft.draganddrop.client.rpc;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface DragOutServerRpc extends ServerRpc {

	void out(Connector source, Connector dragged, Connector over);
}
