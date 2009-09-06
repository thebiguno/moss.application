/*
 * Created on Aug 12, 2007 by wyatt
 */
package ca.digitalcave.moss.application.plugin.factory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.homeunix.thecave.moss.common.Version;

import ca.digitalcave.moss.application.plugin.MossPlugin;

/**
 * A factory which instantiates AbstractPlugin objects.  You can extend this to 
 * instantiate different implementations of AbstractPlugins, other than the 
 * default types already included.
 *  
 * @author wyatt
 *
 */
public class PluginFactory {
	

	/**
	 * Returns a list of all the class names (in filesystem format, 
	 * i.e. com/example/java/Test.class) of classes which
	 * implement the AbstractPlugin interface.  This is a relatively
	 * expensive operation - we have to instantiate each class
	 * within the jar file to be sure it is the correct type -
	 * so use this method with care. 
	 * @param jarFile
	 * @param programVersion the version of the running program
	 * @return
	 */
	public static List<MossPlugin> getMossPluginsFromJar(File jarFile, Version programVersion, String rootClass){

		final Logger logger = Logger.getLogger(PluginFactory.class.getName());

		List<MossPlugin> objects = new LinkedList<MossPlugin>();

		for (JarEntry entry : ClassLoaderFunctions.getAllClasses(jarFile, rootClass)) {
			try {
				logger.finest("Loading " + entry.getName() + " (" + filesystemToClass(entry.getName()) + ")");
				String entryClassName = filesystemToClass(entry.getName());
				Class<?> entryClass = ClassLoaderFunctions.getClass(jarFile, entryClassName);
				if (MossPlugin.class.isAssignableFrom(entryClass)) {
					logger.finest("Found MossPlugin: " + entry.getName());
					Object o = ClassLoaderFunctions.getObject(jarFile, entryClassName);

					MossPlugin plugin = (MossPlugin) o;
					Version pluginMin = plugin.getMinimumVersion();
					Version pluginMax = plugin.getMaximumVersion();

					//If the running program is too early for this plugin 
					if (pluginMin != null && programVersion != null && programVersion.compareTo(pluginMin) < 0){
						logger.severe("This plugin needs to be run on a version of the program greater than or equal to " + pluginMin + ".  Please upgrade and try again.");
					}
					else if (pluginMax != null && programVersion != null && programVersion.compareTo(pluginMax) > 0){
						logger.severe("This plugin needs to be run on a version of the program less than or equal to " + pluginMax + ".  Please see if there is a newer version of the plugin avalable, or run a previous version of the program.");
					}
					else {
						objects.add((MossPlugin) o);
					}
				}
				else {
					logger.finest("Resource " + entry.getName() + " is not an instance of MossPlugin.");
				}
			}
			catch (Exception e){
				logger.log(Level.SEVERE, "Exception when instantiating plugin " + entry.getName(), e);
			}
			catch (AbstractMethodError ame){				
				logger.log(Level.SEVERE, "Error when instantiating plugin " + entry.getName() + ".  This is probably due to loading an old plugin in a new version of the program.", ame);
			}
			catch (Error e){
				logger.log(Level.SEVERE, "Error when instantiating plugin " + entry.getName(), e);
			}
		}
		
		return objects;
	}

	/**
	 * Returns an instance of the plugin specified in the class name.
	 * The plugin must be currently in the classpath.
	 * @param className
	 * @return
	 */
	public static MossPlugin getValidPluginFromClasspath(String className){
		try {
			Object pluginObject = Class.forName(className).newInstance();

			if (pluginObject instanceof MossPlugin){
				return (MossPlugin) pluginObject;
			}
		}
		catch (Exception e){
			final Logger logger = Logger.getLogger(PluginFactory.class.getName());
			logger.log(Level.SEVERE, "Exception when loading plugin", e);
		}

		return null;
	}


	/**
	 * Converts classes from filesystem type (i.e., org/homeunix/thecave/Test.class)
	 * to Class type (i.e., org.homeunix.thecave.Test).
	 * @param filesystemClassName class in Filesystem type
	 * @return
	 */
	private static String filesystemToClass(String filesystemClassName){
		return filesystemClassName.replaceAll(".class$", "").replaceAll("/", ".");
	}
}
