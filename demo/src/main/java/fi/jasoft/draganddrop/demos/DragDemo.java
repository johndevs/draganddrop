package fi.jasoft.draganddrop.demos;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import fi.jasoft.draganddrop.DragAndDrop;
import fi.jasoft.draganddrop.handlers.DropHandler;
import fi.jasoft.draganddrop.handlers.DragOverHandler;
import fi.jasoft.draganddrop.shared.DragAndDropOperation;

public class DragDemo extends VerticalLayout implements View {
	
	public DragDemo() {
		setSpacing(true);
		setMargin(true);
		setWidth("800px");
		setStyleName("drag-demo");
		
		Label header = new Label("Drag and Drop");
		header.setStyleName(ValoTheme.LABEL_LARGE);		
		addComponent(header);
		
		Label description = new Label("Drag the list items over the dustbin, and drop them to have the bin eat the item");
		addComponent(description);
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		addComponent(hl);
		
		final Image image = new Image(null, new ThemeResource("graphics/bin.jpg"));
		DragAndDrop.enable(image).disable(DragAndDropOperation.DRAGGING).onDrop(new DropHandler<Image>() {
			
			@Override
			protected void onDrop(Component component) {
				Notification.show("Dropped");				
			}
		}).onOver(new DragOverHandler<Image>() {
			
			@Override
			protected void onOver(Component component) {
				image.setSource(new ThemeResource("graphics/bin2.jpg"));
			}
		});
		
		hl.addComponent(image);
		hl.setComponentAlignment(image, Alignment.TOP_CENTER);
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setSpacing(true);
		hl.addComponent(vl);
		hl.setComponentAlignment(vl, Alignment.TOP_CENTER);
		
		for(int i=0; i < 5; i++){
			Label item = new Label("Item "+(i+1));
			item.setStyleName("item");
			item.setSizeUndefined();
			DragAndDrop.enable(item).disable(DragAndDropOperation.DROPPING);
			vl.addComponent(item);
		}		
	}
		
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

}
