/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create custom reports.
 */
package ca.digitalcave.moss.application.plugin;

import java.awt.event.ActionListener;

/**
 * A plugin interface which extends ActionListener.  This allows the 
 * plugin to be attached to buttons and components in the interface.
 *  
 * @author wyatt
 */
public interface MossActionPlugin extends MossPlugin, ActionListener {

}
