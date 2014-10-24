package fi.jasoft.ddextension;

import com.google.gwt.dom.client.Style.Display;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.jasoft.ddextension.server.draganddrop.DropHandler;
import fi.jasoft.ddextension.server.draganddrop.DragAndDrop;
import fi.jasoft.ddextension.server.draganddrop.handlers.VerticalLayoutDropHandler;
import fi.jasoft.ddextension.shared.draganddrop.DragAndDropOperations;

@Theme("MyApplication")
public class MyApplicationUI extends UI{
	
	@Override
	protected void init(VaadinRequest request){										
		Label label = new Label("Droppable label");
		DragAndDrop.enable(label).onDrop(new DropHandler<Label>() {
			
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
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		
		for(int i=0; i<5; i++){
			hl.addComponent(new Label("Text label "+i));	
		}				
		
		DragAndDrop.enable(hl);
		
		vl.addComponent(hl);
		
	}
}
