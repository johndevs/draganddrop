package fi.jasoft.draganddrop.handlers;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.Component;

public abstract class AbstractHandler<T extends Component> {

	private T source;
		
	public void setSource(T component) {
		this.source = component;
	}
	
	public T getSource() {
		return source;
	}
	
	public abstract ServerRpc getRpc();
}
