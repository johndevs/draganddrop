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
package fi.jasoft.draganddrop;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;

import fi.jasoft.draganddrop.handlers.DefaultDropHandler;
import fi.jasoft.draganddrop.handlers.DropHandler;
import fi.jasoft.draganddrop.handlers.HorizontalLayoutDropHandler;
import fi.jasoft.draganddrop.handlers.VerticalLayoutDropHandler;

public final class DragAndDropHandlerFactory {

	private static final List<Class<? extends DropHandler<?>>> handlers = new ArrayList<Class<? extends DropHandler<?>>>();
	static {
		register(VerticalLayoutDropHandler.class);
		register(HorizontalLayoutDropHandler.class);
	}

	private DragAndDropHandlerFactory() {
		// Factory
	}

	public static final void register(
			Class<? extends DropHandler<?>> dropHandlerClass) {
		handlers.add(dropHandlerClass);
	}

	@SuppressWarnings("unchecked")
	public static final <T extends Component> DropHandler<T> getDropHandler(
			T connector) {
		for (Class<? extends DropHandler<?>> dropHandlerClass : handlers) {
			DragAndDropHandler dropHandlerAnnotation = dropHandlerClass
					.getAnnotation(DragAndDropHandler.class);
			Class<? extends Connector> connectorClass = dropHandlerAnnotation
					.value();
			if (connectorClass == connector.getClass()) {
				try {
					return (DropHandler<T>) dropHandlerClass.newInstance();
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		return (DropHandler<T>) new DefaultDropHandler();
	}
}
