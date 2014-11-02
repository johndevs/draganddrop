package fi.jasoft.draganddrop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.shared.Connector;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DragAndDropHandler {

	Class<? extends Connector> value();
}
