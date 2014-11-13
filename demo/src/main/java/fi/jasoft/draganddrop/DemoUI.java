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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.johan.Toolbox;
import org.vaadin.johan.Toolbox.ORIENTATION;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.util.IllegalConfigurationException;
import fi.jasoft.draganddrop.demos.DragDemo;
import fi.jasoft.draganddrop.demos.ListDemo;

@Theme("Demo")
public class DemoUI extends UI {

	private List<DemoView> views = new ArrayList<>();
	{
		views.add(new DragDemo());
		views.add(new ListDemo());
	}

	private Label codeLabel = new Label("No source available.",
			ContentMode.HTML);

	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {

		Panel showcase = new Panel();
		showcase.setSizeUndefined();

		navigator = new Navigator(this, showcase);

		for (DemoView view : views) {
			navigator.addView(view.getViewPath(), view);
		}

		// default
		openView(views.get(0));

		MenuBar demos = new MenuBar();
		demos.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

		for (final DemoView view : views) {
			demos.addItem(view.getViewCaption(), new Command() {

				@Override
				public void menuSelected(MenuItem selectedItem) {
					openView(view);
				}
			});
		}

		VerticalLayout root = new VerticalLayout(demos, showcase);
		root.setSizeFull();
		root.setExpandRatio(showcase, 1);
		root.setComponentAlignment(showcase, Alignment.MIDDLE_CENTER);
		setContent(root);

		HorizontalLayout sourceWrapperLayout = new HorizontalLayout();
		Label caption = new Label("Source code for example");
		caption.setStyleName("source-caption");
		sourceWrapperLayout.addComponent(caption);

		Panel sourceWrapper = new Panel(codeLabel);
		sourceWrapper.setStyleName(ValoTheme.PANEL_BORDERLESS);
		sourceWrapper.setHeight(getPage().getBrowserWindowHeight() + "px");
		sourceWrapperLayout.addComponent(sourceWrapper);
		sourceWrapperLayout.setExpandRatio(sourceWrapper, 1);

		Toolbox sourceBox = new Toolbox();
		sourceBox.setOrientation(ORIENTATION.RIGHT_CENTER);
		sourceBox.setContent(sourceWrapperLayout);
		sourceBox.setOverflowSize(30);
		root.addComponent(sourceBox);
	}

	private void openView(DemoView view) {
		navigator.navigateTo(view.getViewPath());
		codeLabel.setValue(getSourceCodeFromFile(view.getClass()));
	}

	private static String getSourceCodeFromFile(Class clazz) {
		String file = clazz.getCanonicalName().replaceAll("\\.", "/") + ".java";
		InputStream inputStream = DemoUI.class.getClassLoader()
				.getResourceAsStream(file);
		if (inputStream == null) {
			return "";
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		StringBuilder snippet = new StringBuilder();
		String line;
		boolean includeLine = false;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.contains("source-start")) {
					includeLine = true;
				} else if (line.contains("source-end")) {
					includeLine = false;
				} else if (includeLine) {
					snippet.append(line);
					snippet.append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getFormattedSourceCode(snippet.toString());
	}

	private static String getFormattedSourceCode(String sourceCode) {
		try {

			JavaSource source = new JavaSourceParser().parse(new StringReader(
					sourceCode));
			JavaSource2HTMLConverter converter = new JavaSource2HTMLConverter();
			StringWriter writer = new StringWriter();

			JavaSourceConversionOptions options = JavaSourceConversionOptions
					.getDefault();
			options.setShowLineNumbers(true);
			options.setAddLineAnchors(false);
			converter.convert(source, options, writer);
			return writer.toString();
		} catch (IllegalConfigurationException exception) {
			return sourceCode;
		} catch (IOException exception) {
			return sourceCode;
		}
	}
}
