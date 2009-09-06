/*
 * Created on Aug 11, 2007 by wyatt
 */
package ca.digitalcave.moss.application.document;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

/**
 * An abstract implementation of StandardDocument, which incldues some default methods.
 * 
 * IMPORTANT: We use a weak hash map as backing for the DocumentChangeListener set.  This
 * means that you must keep a reference to the listener in each class which calls it.  See
 * addDocumentChangeListener() javadocs for more information.
 * 
 * @author wyatt
 *
 */
public abstract class AbstractDocument implements StandardDocument {
	private boolean changed;
	private final Stack<String> disableDocumentChangeEvents = new Stack<String>();
	
//	private EventListenerList listenerList = new EventListenerList();
	private WeakHashMap<DocumentChangeListener, Object> listenerMap = new WeakHashMap<DocumentChangeListener, Object>();
	private final Object listenerValue = new Object(); //Something to put into the WeakHashMap, that we don't create every time.
	
	private File file;
	
	private long lastChangeEvent = 0;
	private long minimumChangeEventPeriod = 1000;
			
	/**
	 * Has the document changed since the last save
	 * @return
	 */
	public boolean isChanged() {
		return changed;
	}
	/**
	 * Indicates that the document has changed.
	 */
	public void setChanged() {
		changed = true;
		if (!isBatchChange())
			fireDocumentChangeEvent();
	}
	/**
	 * The document has not been changed since the last save.
	 */
	public void resetChanged() {
		changed = false;
	}
	/**
	 * Returns the file associated with this document
	 * @return
	 */
	public File getFile() {
		return file;
	}
	/**
	 * Sets the file associated with this document
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	
	
	/**
	 * Returns the minimum amount of time (in millis) between fire events.
	 * If you try to fire more events than this, we will ignore them.
	 * @return
	 */
	public long getMinimumChangeEventPeriod() {
		return minimumChangeEventPeriod;
	}
	/**
	 * Sets the minimum time (in millis) between fire events.  Defaults to 1000 (1 second).
	 * @param minimumChangeEventPeriod
	 */
	public void setMinimumChangeEventPeriod(long minimumChangeEventPeriod) {
		this.minimumChangeEventPeriod = minimumChangeEventPeriod;
	}
	
	/**
	 * Turn off notification of changes to the data model, pending the start of 
	 * many change operations.  This is implemented as a stack; you must finish
	 * each of the change operations before you are able to continue processing
	 * events.
	 */
	public void startBatchChange(){
		disableDocumentChangeEvents.push("");
	}

	/**
	 * Turn on notification of changes again, and if the model has changed in the 
	 * interim (and there are no more change events on the stack), fire an event. 
	 */
	public void finishBatchChange(){
		disableDocumentChangeEvents.pop();

		if (isChanged() && !isBatchChange())
			fireDocumentChangeEvent();
	}
	
	/**
	 * Is there a batch change currently going on?
	 * @return
	 */
	public boolean isBatchChange(){
		return disableDocumentChangeEvents.size() > 0;
	}
	
	/**
	 * Registers this listener to recieve events when the document changes.
	 * 
	 * ***IMPORTANT***
	 * This implementation of AbstractDocument uses a WeakHashMap as list
	 * backing.  This means that when the only reference to the listener
	 * object remaining is in the map, it allows the object to be GC'd.
	 * If you add a listener to your object, you MUST include a reference
	 * to the listener in the object itself.  This means you cannot just
	 * call abstractDocument.addDocumentChangeListener(new DocumentChangeListener(... .
	 * Instead, you must create a private field variable DocumentChangeListener l = new 
	 * DocumentChangeListener(..., and then call abstractDocument.addDocumentChangeListener(l);.
	 * If you don't do this, your reference will be removed as soon as GC is invoked.
	 * @param listener
	 */
	public void addDocumentChangeListener(DocumentChangeListener listener){
//		listenerList.add(DocumentChangeListener.class, listener);
		listenerMap.put(listener, listenerValue);
//		System.out.println(listenerMap.keySet().size());
	}

	/**
	 * Unregisters this listener from recieving events when the document changes.
	 * @param listener
	 */
	public void removeDocumentChangeListener(DocumentChangeListener listener){
//		listenerList.remove(DocumentChangeListener.class, listener);
		listenerMap.remove(listener);
	}

	/**
	 * Fires a document change event.  This can happen at most once per second; if we
	 * try to fire more than that, we will delay the first until the minimum
	 * change event period is over; if any more than that happen, we just ignore them.
	 * 
	 * This gives us the advantage of a) not sending multiple change events for a single
	 * action (which may cause performance delays on slower machines), and b) ensures
	 * that if there are multiple distinct events, we will ensure that all listening
	 * objects will be notified of the last event at most minimumChangeEventPeriod
	 * milliseconds after the event was fired.
	 */
	private boolean changeEventWaiting = false;
//	private final Timer t = new Timer();
	protected synchronized void fireDocumentChangeEvent(){
		long time = System.currentTimeMillis();
		if (time - lastChangeEvent > minimumChangeEventPeriod){
			DocumentChangeEvent event = new DocumentChangeEvent(this);

//			for (Iterator<DocumentChangeListener> i = listenerMap.keySet().iterator(); i.hasNext(); ){
//				i.next().documentChange(event);
//			}
			Collection<DocumentChangeListener> listeners = Collections.synchronizedList(new LinkedList<DocumentChangeListener>(listenerMap.keySet()));
			for (DocumentChangeListener listener : listeners) {
				listener.documentChange(event);
			}
			
			
//			Object[] listeners = listenerList.getListenerList();
//			// Each listener occupies two elements - the first is the listener class
//			// and the second is the listener instance
//			for (int i=0; i<listeners.length; i+=2) {
//				if (listeners[i]==DocumentChangeListener.class) {
//					((DocumentChangeListener) listeners[i+1]).documentChange(event);
//				}
//			}
			
			lastChangeEvent = time;
		}
		else if (!changeEventWaiting){
			changeEventWaiting = true;
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {
					changeEventWaiting = false;
					fireDocumentChangeEvent();
				}
			}, (long) (minimumChangeEventPeriod * 1.1));
		}
	}
	
	/**
	 * Generate a UID string for a particular object.  This is guaranteed to be unique
	 * for each call to this method, even if the object is the same.  It is generated 
	 * by concatinating the following information, separated by the dash (-):
	 * 
	 * 1) The canonical name of the object (e.g. org.homeunix.thecave.buddi.model3.Account).
	 * 2) A hexadecimal representation of the current system time in milliseconds
	 * 3) A hexadecimal representation of a 16 bit random number
	 * 4) A hexadecimal representation of the 16 least significant bits of this object's hash code (object.hashCode()).
	 * @param object
	 * @return
	 */
	public static String getGeneratedUid(Object object){
		long time = System.currentTimeMillis();
		int random = (int) (Math.random() * 0xFFFF);
		int hash = object.hashCode() & 0xFFFF;

		String uid = object.getClass().getCanonicalName() + "-" + Long.toHexString(time) + "-" + Integer.toHexString(random) + "-" + Integer.toHexString(hash);

		return uid;
	}
}
