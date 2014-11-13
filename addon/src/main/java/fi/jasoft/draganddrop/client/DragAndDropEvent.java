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
package fi.jasoft.draganddrop.client;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.ComponentConnector;

public abstract class DragAndDropEvent {

	public static class DropEvent extends DragAndDropEvent {
		public DropEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, NativeEvent event) {
			super(targetConnector, draggedConnector, event);
		}
	}

	public static class DragEnterEvent extends DragAndDropEvent {

		private ComponentConnector over;

		public DragEnterEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, ComponentConnector over,
				NativeEvent event) {
			super(targetConnector, draggedConnector, event);
			this.over = over;
		}

		public ComponentConnector getOverConnector() {
			return this.over;
		}
	}

	public static class DragLeaveEvent extends DragAndDropEvent {

		private ComponentConnector over;

		public DragLeaveEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, NativeEvent event) {
			super(targetConnector, draggedConnector, event);
			this.over = targetConnector;
		}

		public ComponentConnector getOverConnector() {
			return this.over;
		}
	}

	public static class DragOverEvent extends DragAndDropEvent {

		private ComponentConnector over;

		public DragOverEvent(ComponentConnector targetConnector,
				ComponentConnector draggedConnector, NativeEvent event) {
			super(targetConnector, draggedConnector, event);
			this.over = targetConnector;
		}

		public ComponentConnector getOverConnector() {
			return this.over;
		}
	}

	private ComponentConnector targetConnector;

	private ComponentConnector draggedConnector;

	private NativeEvent event;

	public DragAndDropEvent(ComponentConnector targetConnector,
			ComponentConnector draggedConnector, NativeEvent event) {
		super();
		this.targetConnector = targetConnector;
		this.draggedConnector = draggedConnector;
		this.event = event;
	}

	public ComponentConnector getTargetConnector() {
		return targetConnector;
	}

	public ComponentConnector getDraggedConnector() {
		return draggedConnector;
	}

	public NativeEvent getEvent() {
		return event;
	}

}
