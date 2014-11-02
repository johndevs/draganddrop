package fi.jasoft.draganddrop.shared.draganddrop;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.SharedState;

public class DragAndDropState extends SharedState{

	public List<DragAndDropOperation> disabled = new ArrayList<DragAndDropOperation>();
	
	public Connector fromLayout;
	
	public Connector fromComponent;
}
