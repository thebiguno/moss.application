/*
 * Created on Jul 30, 2007 by wyatt
 */
package ca.digitalcave.moss.application.prefs;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Prefs {
	
	protected PrefsBean bean;
	protected File prefsFile;
	
	public Prefs(File prefsFile) {
		this.prefsFile = prefsFile;
		
		try {
			XMLDecoder prefsDecoder = new XMLDecoder(new FileInputStream(prefsFile));
			bean = (PrefsBean) prefsDecoder.readObject();
			if (bean == null)
				throw new Exception("Error loading preferences - creating new file.");
		}
		catch (RuntimeException re){
			bean = new PrefsBean();
			getDefaultValues();
		}
		catch (Exception e){
			bean = new PrefsBean();
			getDefaultValues();
		}
	}
	
	/**
	 * There was a problem loading the file, so we need to make a new one.  By default,
	 * this doesn't set anything apart from the defaults; override this if you want
	 * to set them.  Call the superclass at the end, as that will save the file.
	 */
	public void getDefaultValues(){
		save();
	}

	public void save() {
		if (prefsFile != null){
			try {
				if (!prefsFile.getParentFile().exists())
					prefsFile.getParentFile().mkdirs();
				XMLEncoder encoder = new XMLEncoder(new FileOutputStream(prefsFile));
				encoder.writeObject(bean);
				encoder.flush();
				encoder.close();
			}
			catch (FileNotFoundException fnfe){
				final Logger logger = Logger.getLogger(Prefs.class.getName());
				logger.log(Level.SEVERE, "Problem saving preferences file: ", fnfe);
			}
		}
	}
	
	/**
	 * Streams the current Preferences object as an XML encoded string.  This is primarily meant
	 * for troubleshooting crashes. 
	 * @return
	 */
	public String toString(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(bean);
		encoder.flush();
		encoder.close();
		
		return baos.toString();
	}


}
