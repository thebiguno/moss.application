/*
 * Created on Aug 11, 2007 by wyatt
 */
package org.homeunix.thecave.moss.application.document;

import java.io.File;

import org.homeunix.thecave.moss.application.document.exception.DocumentSaveException;

public interface StandardDocument {

	public boolean isChanged();

	public void setChanged();
	
	public void resetChanged();
	
	/**
	 * Returns the file associated with this document
	 * @return
	 */
	public File getFile();
	
	/**
	 * Sets the file associated with this document
	 * @param file
	 */
	public void setFile(File file);
	
	/**
	 * Turn off notification of changes to the data model, pending the start of 
	 * many change operations.  This is implemented as a stack; you must finish
	 * each of the change operations before you are able to continue processing
	 * events.
	 */
	public void startBatchChange();

	/**
	 * Turn on notification of changes again, and if the model has changed in the 
	 * interim (and there are no more change events on the stack), fire an event. 
	 */
	public void finishBatchChange();

	/**
	 * Save the data file to the current file (obtained by getFile()).  If the current file has 
	 * not yet been set, perform a saveAs().
	 */
	public void save() throws DocumentSaveException;
	
	/**
	 * Prompt the user for a different file, and save the model to that file.  Set the
	 * current file, for future saves.
	 * @param file File to save to
	 * @throws DocumentSaveException
	 */
	public void saveAs(File file) throws DocumentSaveException;
	
	
	/**
	 * Registers this listener to recieve events when the document changes.
	 * @param listener
	 */
	public void addDocumentChangeListener(DocumentChangeListener listener);

	/**
	 * Unregisters this listener from recieving events when the document changes.
	 * @param listener
	 */
	public void removeDocumentChangeListener(DocumentChangeListener listener);
}
