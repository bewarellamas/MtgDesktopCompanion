
import org.magic.services.PluginRegistry;


PluginRegistry.inst().listPlugins().forEach(mtg->{
			out.println("*** Cleaning "+mtg.getName() +" " + mtg.getType());
			
			
			
		});