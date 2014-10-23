package fi.jasoft.ddextension;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.vaadin.server.VaadinServlet;

@WebServlet(
    asyncSupported=false,
    urlPatterns={"/*","/VAADIN/*"},
    initParams={
        @WebInitParam(name="ui", value="fi.jasoft.ddextension.MyApplicationUI"),
	@WebInitParam(name="widgetset", value="fi.jasoft.ddextension.DDExtensionWidgetset")
    })
public class MyApplicationServlet extends VaadinServlet { }
