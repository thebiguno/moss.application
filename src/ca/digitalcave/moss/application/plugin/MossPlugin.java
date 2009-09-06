/*
 * Created on Sep 14, 2006 by wyatt
 */
package ca.digitalcave.moss.application.plugin;

import ca.digitalcave.moss.common.Version;


public interface MossPlugin {
	/**
	 * Returns the plugin description.  This may be used in different places,
	 * depending on the type of plugin.
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Returns the plugin name.  This may be used in different places, depending
	 * on the type of plugin.
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the minimum program version needed to run this plugin.
	 * If this is null, there is no limitation on the program 
	 * (not recommended unless you can guarantee that the plugin uses 
	 * no API calls within the program).
	 * 
	 * @return The Version object containing the minimum version number. 
	 */
	public Version getMinimumVersion();
	
	/**
	 * Returns the maximum program version needed to run this plugin.
	 * If this is null, there is no limitation on the program 
	 * (not recommended unless you can guarantee that the plugin uses 
	 * no API calls within the program).
	 * 
	 * @return The Version object containing the maximum version number. 
	 */
	public Version getMaximumVersion();
	
	/**
	 * Should this plugin be activated?  Most people can just
	 * put true here; if there is some logic which determines if this
	 * is to be shown or not, though, you can add it here.
	 * @return
	 */
	public boolean isPluginActive();
}
