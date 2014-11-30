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
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.draganddrop.DragAndDropHandler;
import fi.jasoft.draganddrop.client.configurations.VerticalLayoutDragAndDropConfiguration.Alignment;
import fi.jasoft.draganddrop.client.configurations.VerticalLayoutDragAndDropConfiguration.VerticalLayoutDropHandlerRpc;

@DragAndDropHandler(VerticalLayout.class)
public class VerticalLayoutDropHandler extends DropHandler<VerticalLayout> {

    @SuppressWarnings("serial")
    private VerticalLayoutDropHandlerRpc rpc = new VerticalLayoutDropHandlerRpc() {

        @Override
        public void drop(Connector source, Connector dragged) {
            if (source == getSource()) {
                onDrop((Component) dragged);
            }
        }

        @Override
        public void drop(Connector source, Connector dragged, int index,
                Alignment verticalAlign) {
            if (source == getSource()) {
                onDrop((Component) dragged, index, verticalAlign);
            }
        }
    };

    @Override
    public VerticalLayoutDropHandlerRpc getRpc() {
        return rpc;
    }

    protected void onDrop(Component component, int index,
            Alignment verticalAlign) {
        if (index > -1) {
            if (verticalAlign != Alignment.TOP) {
                index++;
            }
            getSource().addComponent(component, index);
        } else {
            getSource().addComponent(component);
        }
    }

    @Override
    protected void onDrop(Component component) {
        getSource().addComponent(component);
    }
}
