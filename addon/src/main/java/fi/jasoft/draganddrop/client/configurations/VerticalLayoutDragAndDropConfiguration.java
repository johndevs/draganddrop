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

import com.vaadin.client.Util;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.draganddrop.client.DragAndDropConfiguration;
import fi.jasoft.draganddrop.client.DragAndDropEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragOverEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;
import fi.jasoft.draganddrop.client.rpc.DropServerRpc;
import fi.jasoft.draganddrop.handlers.VerticalLayoutDropHandler;

@DragAndDropConfiguration(VerticalLayoutDropHandler.class)
public class VerticalLayoutDragAndDropConfiguration extends
        OrderedLayoutDragAndDropConfiguration<VerticalLayoutConnector> {

    public interface VerticalLayoutDropHandlerRpc extends DropServerRpc {
        public void drop(Connector source, Connector dragged, int index,
                Alignment verticalAlign);
    }

    public enum Alignment {
        TOP, MIDDLE, BOTTOM
    }

    @Override
    public void dragEnter(DragEnterEvent event) {
        super.dragEnter(event);
        updateCurrentSlot(event);
    }

    @Override
    public void dragOver(DragOverEvent event) {
        super.dragOver(event);
        updateCurrentSlot(event);
    }

    protected void updateCurrentSlot(DragAndDropEvent event) {
        if (currentSlot != null) {
            Alignment verticalAlign = getVerticalAlign(event);
            switch (verticalAlign) {
            case TOP:
                currentSlot.addStyleName(ACTIVE_SLOT + "-top");
                break;
            case MIDDLE:
                currentSlot.addStyleName(ACTIVE_SLOT + "-middle");
                break;
            case BOTTOM:
                currentSlot.addStyleName(ACTIVE_SLOT + "-bottom");
                break;
            }
        }
    }

    @Override
    protected void cleanupCurrentSlot() {
        super.cleanupCurrentSlot();
        if (currentSlot != null) {
            currentSlot.removeStyleName(ACTIVE_SLOT + "-top");
            currentSlot.removeStyleName(ACTIVE_SLOT + "-middle");
            currentSlot.removeStyleName(ACTIVE_SLOT + "-bottom");
        }
    }

    @Override
    public void drop(DropEvent event) {
        super.drop(event);
        VerticalLayoutConnector connector = (VerticalLayoutConnector) event
                .getTargetConnector();
        int slotIndex = getSlotIndex(connector, event);
        Alignment verticalAlign = getVerticalAlign(event);
        getRpcProxy(VerticalLayoutDropHandlerRpc.class).drop(connector,
                event.getDraggedConnector(), slotIndex, verticalAlign);
    }

    private Alignment getVerticalAlign(DragAndDropEvent event) {
        Slot slot = getSlot(event);
        if (slot == null) {
            // Over layout
            return Alignment.MIDDLE;
        }

        int absoluteTop = slot.getAbsoluteTop();
        int fromTop = Util.getTouchOrMouseClientY(event.getEvent())
                - absoluteTop;
        float percentageFromTop = (fromTop / (float) slot.getOffsetHeight());
        if (percentageFromTop < 0.25) {
            return Alignment.TOP;
        } else if (percentageFromTop > 1 - 0.25) {
            return Alignment.BOTTOM;
        } else {
            return Alignment.MIDDLE;
        }
    }
}
