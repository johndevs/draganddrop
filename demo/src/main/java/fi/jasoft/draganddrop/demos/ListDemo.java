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

import java.util.Arrays;
import java.util.List;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import fi.jasoft.draganddrop.DemoView;
import fi.jasoft.draganddrop.DragAndDrop;

public class ListDemo extends VerticalLayout implements DemoView {

	public ListDemo() {
		setSpacing(true);
		setMargin(true);
		setWidth("800px");
		setStyleName("drag-demo");

		Label header = new Label(getViewCaption());
		header.setStyleName(ValoTheme.LABEL_LARGE);
		addComponent(header);

		Label description = new Label(
				"Re-prioritize list by drag and dropping items to re-order");
		addComponent(description);

		// start-source
		// Create shopping list
		VerticalLayout list = new VerticalLayout();
		list.setSpacing(true);

		// Add some items
		List<String> items = Arrays.asList("Carrots", "Apples", "Lemons",
				"Broccoli", "Ginger");
		for (String caption : items) {
			Label item = new Label(caption);
			item.setStyleName("item");
			list.addComponent(item);
		}

		// Enable drag and drop reordering for the list
		DragAndDrop.enable(list);
		// end-source

		addComponent(list);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getViewPath() {
		return "list";
	}

	@Override
	public String getViewCaption() {
		return "List reordering";
	}
}
