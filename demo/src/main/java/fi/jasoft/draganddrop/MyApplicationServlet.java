package fi.jasoft.draganddrop;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.vaadin.server.VaadinServlet;

@WebServlet(
    asyncSupported=false,
    urlPatterns={"/*","/VAADIN/*"},
    initParams={
        @WebInitParam(name="ui", value="fi.jasoft.draganddrop.DemoUI"),
	    @WebInitParam(name="widgetset", value="fi.jasoft.draganddrop.DemoWidgetset")
    })
public class MyApplicationServlet extends VaadinServlet { }
