package fi.jasoft.ddextension.shared.draganddrop;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.communication.SharedState;

public class DragAndDropState extends SharedState{

	public List<DragAndDropOperations> disabled = new ArrayList<DragAndDropOperations>();
}
