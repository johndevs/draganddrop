package fi.jasoft.ddextension.client.draganddrop.configurations;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.FastStringMap;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.shared.communication.ServerRpc;

import fi.jasoft.ddextension.client.draganddrop.DragAndDropConnector;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropEvent.*;
import fi.jasoft.ddextension.client.draganddrop.DragAndDropServerRpc;

public abstract class AbstractDragAndDropConfiguration<T extends ComponentConnector> {
	
	public static final String ACTIVE_STYLE_NAME = "dd-active";
	
	private FastStringMap<ServerRpc> rpcProxyMap = FastStringMap.create();
	
	private DragAndDropConnector connector;
	
	public void setDragAndDropConnector(DragAndDropConnector connector) {
		this.connector = connector;
	}
	
	public void drop(DropEvent event){
		event.getTargetConnector().getWidget().removeStyleName(ACTIVE_STYLE_NAME);
		getRpcProxy(DragAndDropServerRpc.class).drop(event.getTargetConnector(), event.getDraggedConnector());
	}
	
	public void dragEnter(DragEnterEvent event) {
		event.getTargetConnector().getWidget().addStyleName(ACTIVE_STYLE_NAME);
	}
	
	public void dragLeave(DragLeaveEvent event) {
		event.getTargetConnector().getWidget().removeStyleName(ACTIVE_STYLE_NAME);
	}
	
	protected <C extends ServerRpc> C getRpcProxy(Class<C> rpcInterface) {
		assert connector != null;
	    String name = rpcInterface.getName();
	    if (!rpcProxyMap.containsKey(name)) {
	        rpcProxyMap.put(name, RpcProxy.create(rpcInterface, connector));
	    }
	    return (C) rpcProxyMap.get(name);
	}		
}
