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
import com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.Connector;

import fi.jasoft.draganddrop.client.DragAndDropConfiguration;
import fi.jasoft.draganddrop.client.DragAndDropEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragEnterEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DragOverEvent;
import fi.jasoft.draganddrop.client.DragAndDropEvent.DropEvent;
import fi.jasoft.draganddrop.client.rpc.DropServerRpc;
import fi.jasoft.draganddrop.handlers.HorizontalLayoutDropHandler;

@DragAndDropConfiguration(HorizontalLayoutDropHandler.class)
public class HorizontalLayoutDragAndDropConfiguration extends
        OrderedLayoutDragAndDropConfiguration<VerticalLayoutConnector> {

    public interface HorizontalLayoutDropHandlerRpc extends DropServerRpc {
        public void drop(Connector source, Connector dragged, int index,
                Alignment horizontalAlign);
    }

    public enum Alignment {
        LEFT, CENTER, RIGHT
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
            Alignment horizontalAlign = getHorizontalAlign(event);
            switch (horizontalAlign) {
            case LEFT:
                currentSlot.addStyleName(ACTIVE_SLOT + "-left");
                break;
            case CENTER:
                currentSlot.addStyleName(ACTIVE_SLOT + "-center");
                break;
            case RIGHT:
                currentSlot.addStyleName(ACTIVE_SLOT + "-right");
                break;
            }
        }
    }

    @Override
    protected void cleanupCurrentSlot() {
        super.cleanupCurrentSlot();
        if (currentSlot != null) {
            currentSlot.removeStyleName(ACTIVE_SLOT + "-left");
            currentSlot.removeStyleName(ACTIVE_SLOT + "-center");
            currentSlot.removeStyleName(ACTIVE_SLOT + "-right");
        }
    }

    @Override
    public void drop(DropEvent event) {
        super.drop(event);
        HorizontalLayoutConnector connector = (HorizontalLayoutConnector) event
                .getTargetConnector();
        int slotIndex = getSlotIndex(connector, event);
        Alignment horizontalAlign = getHorizontalAlign(event);
        getRpcProxy(HorizontalLayoutDropHandlerRpc.class).drop(connector,
                event.getDraggedConnector(), slotIndex, horizontalAlign);
    }

    private Alignment getHorizontalAlign(DragAndDropEvent event) {
        Slot slot = getSlot(event);
        if (slot == null) {
            // Over layout
            return Alignment.LEFT;
        }

        int absoluteLeft = slot.getAbsoluteLeft();
        int fromTop = Util.getTouchOrMouseClientX(event.getEvent())
                - absoluteLeft;

        float percentageFromTop = (fromTop / (float) slot.getOffsetHeight());
        if (percentageFromTop < 0.25) {
            return Alignment.LEFT;
        } else if (percentageFromTop > 1 - 0.25) {
            return Alignment.RIGHT;
        } else {
            return Alignment.CENTER;
        }
    }
}
