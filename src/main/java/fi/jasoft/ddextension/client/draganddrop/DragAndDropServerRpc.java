package fi.jasoft.ddextension.client.draganddrop;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.ServerRpc;

public interface DragAndDropServerRpc extends ServerRpc {

	void drop(Connector source, Connector dragged);
}
