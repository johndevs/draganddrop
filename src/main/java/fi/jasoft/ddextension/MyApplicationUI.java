package fi.jasoft.ddextension;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.ddextension.server.draganddrop.AbstractDropHandler;
import fi.jasoft.ddextension.server.draganddrop.DragAndDrop;
import fi.jasoft.ddextension.server.draganddrop.handlers.VerticalLayoutDropHandler;

@Theme("MyApplication")
public class MyApplicationUI extends UI{
	
	@Override
	protected void init(VaadinRequest request){										
		Label label = new Label("Droppable label");
		DragAndDrop.enable(label).drop(new AbstractDropHandler<Label>() {
			
			@Override
			protected void onDrop(Component component) {
				Notification.show("Dropped "+component);				
			}
		});
		
		
		VerticalLayout vl = new VerticalLayout();		
		vl.setSizeFull();

		DragAndDrop.enable(vl);	
		
		for(int i=0; i<5; i++){
			if(i == 3){
				vl.addComponent(label);
			} else {
				vl.addComponent(new Label("Text label "+i));
			}			
		}		
		
		setContent(vl);
		
		VerticalLayout v2 = new VerticalLayout();
		v2.setSizeFull();
		
		DragAndDrop.enable(v2);
		
		vl.addComponent(v2);
		
	}
}
