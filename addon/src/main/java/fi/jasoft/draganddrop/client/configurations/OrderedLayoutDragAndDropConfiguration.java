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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.client.ui.orderedlayout.Slot;

import fi.jasoft.draganddrop.client.DragAndDropEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragLeaveEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragOverEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;

public class OrderedLayoutDragAndDropConfiguration<T extends AbstractOrderedLayoutConnector>
		extends AbstractDragAndDropConfiguration<T> {

	protected Slot currentSlot;

	protected static final String ACTIVE_SLOT = "dd-active-slot";

	@Override
	public void dragEnter(DragEnterEvent event) {
		cleanupCurrentSlot();

		currentSlot = getSlot(event);

		if (currentSlot != null) {
			currentSlot.addStyleName(ACTIVE_SLOT);
		}
	}

	@Override
	public void dragOver(DragOverEvent event) {
		cleanupCurrentSlot();

		currentSlot = getSlot(event);

		if (currentSlot != null) {
			currentSlot.addStyleName(ACTIVE_SLOT);
		}
	}

	@Override
	public void dragLeave(DragLeaveEvent event) {
		cleanupCurrentSlot();
	}

	@Override
	public void drop(DropEvent event) {
		cleanupCurrentSlot();
	}

	protected void cleanupCurrentSlot() {
		if (currentSlot != null) {
			currentSlot.removeStyleName(ACTIVE_SLOT);
		}
	}

	protected Slot getSlot(DragAndDropEvent event) {
		Element e = Util.getElementUnderMouse(event.getEvent());

		/*
		 * If we are hitting between slot search around the cursor for the
		 * closest slot
		 */
		int nextDirection = 0;
		int width = 1;
		int pageX = Util.getTouchOrMouseClientX(event.getEvent());
		int pageY = Util.getTouchOrMouseClientY(event.getEvent());
		while (event.getTargetConnector().getWidget().getElement() == e) {
			if (nextDirection == 0) {
				pageY -= width;
				nextDirection++;
			} else if (nextDirection == 1) {
				pageX += width;
				nextDirection++;
			} else if (nextDirection == 2) {
				pageY += width;
				nextDirection++;
			} else if (nextDirection == 3) {
				pageX -= width;
				width++;
				nextDirection = 0;
			}
			e = Util.getElementFromPoint(pageX, pageY);
		}

		assert e != null : "Event target was null";
		Widget w = Util.findWidget(e, null);
		assert w != null : "Widget was not found";

		if (w != null) {
			while (!(w instanceof Slot) && w != null) {
				w = w.getParent();
			}
		}

		assert w instanceof Slot;
		return (Slot) w;
	}

	protected int getSlotIndex(AbstractOrderedLayoutConnector connector,
			DragAndDropEvent event) {
		Slot slot = getSlot(event);
		assert slot != null;
		return connector.getWidget().getWidgetIndex(slot);
	}
}
