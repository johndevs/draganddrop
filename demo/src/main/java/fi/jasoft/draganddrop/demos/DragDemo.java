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
package fi.jasoft.draganddrop.demos;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.draganddrop.DragAndDrop;
import fi.jasoft.draganddrop.handlers.DragOutHandler;
import fi.jasoft.draganddrop.handlers.DragOverHandler;
import fi.jasoft.draganddrop.handlers.DropHandler;
import fi.jasoft.draganddrop.shared.DragAndDropOperation;

public class DragDemo extends AbstractDemo {

	@Override
	public String getViewPath() {
		return "basic";
	}

	@Override
	public String getViewCaption() {
		return "Basic Drag and Drop";
	}

	@Override
	protected String getDemoDescription() {
		return "Drag the list items over the dustbin, and drop them to have the bin eat the item";
	}

	@Override
	protected Component getDemoContent() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();

		// source-start
		// Create image
		Image image = new Image(null, new ThemeResource("graphics/bin.jpg"));

		// Enable dropping items on image
		DragAndDrop.enable(image, DragAndDropOperation.DROP)

		// Set custom drop handler
		.onDrop(new DropHandler<Image>() {

			@Override
			protected void onDrop(Component component) {

				// Restor trash bin
				getSource().setSource(
						new ThemeResource("graphics/bin.jpg"));

				// Remove item
				((ComponentContainer) component.getParent())
						.removeComponent(component);
			}
		})

		// Set custom over handler
		.onOver(new DragOverHandler<Image>() {

			@Override
			protected void onOver(Component component) {

				// Add color to trash bin
				getSource().setSource(
						new ThemeResource("graphics/bin2.jpg"));
			}
		})

		// Set custom out handler
		.onOut(new DragOutHandler<Image>() {

			@Override
			protected void onOut(Component component) {

				// Restore trash bin
				getSource().setSource(
						new ThemeResource("graphics/bin.jpg"));
			}
		});

		// source-end

		hl.addComponent(image);
		hl.setComponentAlignment(image, Alignment.TOP_CENTER);

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setSpacing(true);
		hl.addComponent(vl);
		hl.setComponentAlignment(vl, Alignment.TOP_CENTER);

		// source-start
		/*
		 * Create item list to drag from
		 */
		for (int i = 0; i < 5; i++) {
			Label item = new Label("Item " + (i + 1));
			item.setStyleName("item");
			item.setSizeUndefined();

			// Enable dragging the items
			DragAndDrop.enable(item, DragAndDropOperation.DRAG);

			vl.addComponent(item);
		}
		// source-end

		return hl;
	}
}
