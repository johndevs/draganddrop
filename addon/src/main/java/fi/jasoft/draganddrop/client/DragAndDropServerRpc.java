package fi.jasoft.draganddrop.client;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface DragAndDropServerRpc extends ServerRpc {

	void drop(Connector source, Connector dragged);
	
	void over(Connector source, Connector dragged, Connector over);
}
