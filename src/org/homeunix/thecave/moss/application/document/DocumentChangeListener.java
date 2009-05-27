/*
 * Created on Aug 11, 2007 by wyatt
 */
package org.homeunix.thecave.moss.application.document;

import java.util.EventListener;

public interface DocumentChangeListener extends EventListener {
	public void documentChange(DocumentChangeEvent event);
}
