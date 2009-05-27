/*
 * Created on Aug 11, 2007 by wyatt
 */
package org.homeunix.thecave.moss.application.document;

import java.util.EventObject;

public class DocumentChangeEvent extends EventObject {
	public static final long serialVersionUID = 0;

	public DocumentChangeEvent(AbstractDocument source) {
		super(source);
	}
}
