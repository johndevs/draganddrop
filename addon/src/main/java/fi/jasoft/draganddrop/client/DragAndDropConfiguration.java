package fi.jasoft.draganddrop.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.shared.Connector;

import fi.jasoft.draganddrop.handlers.DropHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DragAndDropConfiguration {

	Class<?> value();
}
