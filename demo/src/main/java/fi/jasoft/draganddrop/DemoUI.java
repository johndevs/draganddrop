package fi.jasoft.draganddrop;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.draganddrop.demos.DragDemo;

@Theme("Demo")
public class DemoUI extends UI{
	
	@Override
	protected void init(VaadinRequest request){			

		Panel showcase = new Panel();
		showcase.setSizeUndefined();
		
		Navigator navigator = new Navigator(this, showcase);
		navigator.addView("drag", DragDemo.class);
		
		// default
		navigator.navigateTo("drag");
		
		MenuBar demos = new MenuBar();
		demos.addItem("Drag", new Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				navigator.navigateTo("drag");				
			}
		});
		
		VerticalLayout root = new VerticalLayout(demos, showcase);
		root.setSizeFull();
		root.setExpandRatio(showcase, 1);
		root.setComponentAlignment(showcase, Alignment.MIDDLE_CENTER);
		setContent(root);
	}
}
