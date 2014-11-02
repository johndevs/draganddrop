package fi.jasoft.draganddrop.server.draganddrop.configurations;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.vaadin.client.ComponentConnector;
import com.vaadin.shared.ui.Connect;

import fi.jasoft.draganddrop.client.draganddrop.DragAndDropConfiguration;
import fi.jasoft.draganddrop.client.draganddrop.DragAndDropConfigurationManager;
import fi.jasoft.draganddrop.client.draganddrop.configurations.AbstractDragAndDropConfiguration;
import fi.jasoft.draganddrop.server.draganddrop.DragAndDropHandler;
import fi.jasoft.draganddrop.server.draganddrop.DropHandler;

public class DragAndDropConfigurationManagerGenerator extends Generator {

	private static final String IMPL_TYPE_NAME = 
			  DragAndDropConfigurationManager.class.getSimpleName() + "Generated";
	private static final String IMPL_PACKAGE_NAME = 
			  DragAndDropConfigurationManager.class.getPackage().getName();
	
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		
	    TypeOracle typeOracle = context.getTypeOracle();
        JClassType functionType = typeOracle.findType(typeName);
        assert DragAndDropConfigurationManager.class.equals(functionType.getClass());

        ClassSourceFileComposerFactory composerFactory =
            new ClassSourceFileComposerFactory(
                IMPL_PACKAGE_NAME, IMPL_TYPE_NAME);

        composerFactory.addImport(DragAndDropConfigurationManager.class.getCanonicalName());
        composerFactory.addImplementedInterface(DragAndDropConfigurationManager.class.getName());

        composerFactory.addImport(Map.class.getCanonicalName());
        composerFactory.addImport(HashMap.class.getCanonicalName());
        composerFactory.addImport(AbstractDragAndDropConfiguration.class.getCanonicalName());
        composerFactory.addImport(ComponentConnector.class.getCanonicalName());

		// Get all connectors and configurations
		List<JClassType> connectors = new ArrayList<JClassType>();
		List<JClassType> configurations = new ArrayList<JClassType>();
		for (JClassType type : typeOracle.getTypes()) {
			if(type.getAnnotation(Connect.class) != null){
				connectors.add(type);
			}
			if(type.getAnnotation(DragAndDropConfiguration.class) != null) {			
				configurations.add(type);
			}	
		}
		
		// Map connector to configuration
		Map<JClassType, JClassType> connectorToConfigurationMap = new HashMap<JClassType, JClassType>();
		for(JClassType connectorType : connectors) {
			Connect connect = connectorType.getAnnotation(Connect.class);			    			
			for(JClassType confType : configurations) {
				DragAndDropConfiguration conf = confType.getAnnotation(DragAndDropConfiguration.class);
				Class<?> dropHandler = conf.value();
				DragAndDropHandler ddHandler = dropHandler.getAnnotation(DragAndDropHandler.class);
				if(connect.value() == ddHandler.value()){
					connectorToConfigurationMap.put(connectorType, confType);		
					composerFactory.addImport(confType.getQualifiedSourceName());
				}    				
			}				
		}		
        
        PrintWriter printWriter = context
            .tryCreate(logger, IMPL_PACKAGE_NAME, IMPL_TYPE_NAME);
           	        	
        if(printWriter != null) {
            SourceWriter sourceWriter = composerFactory
                .createSourceWriter(context, printWriter);

            sourceWriter.println("@Override");
            sourceWriter.println("public Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> getConfigurations(){");
            sourceWriter.indent();
            sourceWriter.println("Map<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>> configurations = new HashMap<String, AbstractDragAndDropConfiguration<? extends ComponentConnector>>();");
    		
            for(Entry<JClassType, JClassType> e: connectorToConfigurationMap.entrySet()) {
            	sourceWriter.println("configurations.put(\""+
						e.getKey().getQualifiedSourceName()+
						"\", new "+e.getValue().getSimpleSourceName()+"());");
            }
            
    		sourceWriter.println("return configurations;");
    		sourceWriter.outdent();
    		sourceWriter.println("}");

            sourceWriter.commit(logger);  	
        }		        
        
        return IMPL_PACKAGE_NAME + "." + IMPL_TYPE_NAME;	
	}
}
