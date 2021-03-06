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
package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.client.rpc.DragOverServerRpc;

public abstract class DragOverHandler<T extends Component> extends
		AbstractHandler<T> {

	protected abstract void onOver(Component component);

	@Override
	public DragOverServerRpc getRpc() {
		return new DragOverServerRpc() {

			@Override
			public void over(Connector source, Connector dragged, Connector over) {
				onOver((Component) dragged);
			}
		};
	}
}
