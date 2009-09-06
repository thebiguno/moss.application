/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create custom reports.
 */
package ca.digitalcave.moss.application.plugin;




/**
 * @author wyatt
 * 
 * A generic plugin type, which should be loaded at the beginning of 
 * the session, just after the init() of the first frame is completed.
 * You can use this plugin type for extending main program
 * functionality.
 * 
 * To aid in running this (and to potentially allow it to be run in 
 * a thread, if desired) this plugin implements Runnable.
 */
public interface MossRunnablePlugin extends MossPlugin, Runnable {

}
