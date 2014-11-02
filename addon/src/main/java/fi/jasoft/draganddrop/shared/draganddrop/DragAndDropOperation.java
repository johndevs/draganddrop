package fi.jasoft.draganddrop.shared.draganddrop;

public enum DragAndDropOperation {
	/**
	 * Disables starting drag operations from compontents
	 */
	DRAGGING, 
	
	/**
	 * Disables dropping components
	 */
	DROPPING, 
	
	
	/**
	 * Disables re-ordering components in layouts
	 */
	REORDERING,
	
	
	/**
	 * Disable all drag & drop operations in layout
	 */
	ALL
}