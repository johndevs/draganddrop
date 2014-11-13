/*
 * Copyright 2014 John Ahlroos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.jasoft.draganddrop.client.configurations;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.FastStringMap;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.shared.communication.ServerRpc;

import fi.jasoft.draganddrop.client.DragAndDropConnector;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragLeaveEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragOverEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;
import fi.jasoft.draganddrop.client.rpc.DragOutServerRpc;
import fi.jasoft.draganddrop.client.rpc.DragOverServerRpc;
import fi.jasoft.draganddrop.client.rpc.DropServerRpc;

public abstract class AbstractDragAndDropConfiguration<T extends ComponentConnector> {

	public static final String ACTIVE_STYLE_NAME = "dd-active";

	private final FastStringMap<ServerRpc> rpcProxyMap = FastStringMap.create();

	private DragAndDropConnector connector;

	public void setDragAndDropConnector(DragAndDropConnector connector) {
		this.connector = connector;
	}

	public void drop(DropEvent event) {
		event.getTargetConnector().getWidget()
				.removeStyleName(ACTIVE_STYLE_NAME);
		getRpcProxy(DropServerRpc.class).drop(event.getTargetConnector(),
				event.getDraggedConnector());
	}

	public void dragEnter(DragEnterEvent event) {
		event.getTargetConnector().getWidget().addStyleName(ACTIVE_STYLE_NAME);
		getRpcProxy(DragOverServerRpc.class).over(event.getTargetConnector(),
				event.getDraggedConnector(), event.getOverConnector());
	}

	public void dragLeave(DragLeaveEvent event) {
		event.getTargetConnector().getWidget()
				.removeStyleName(ACTIVE_STYLE_NAME);
		getRpcProxy(DragOutServerRpc.class).out(event.getTargetConnector(),
				event.getDraggedConnector(), event.getOverConnector());
	}

	public void dragOver(DragOverEvent event) {
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
