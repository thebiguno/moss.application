/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.moss.application.prefs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * The backing bean for preferences.  Includes a number of different map types,
 * including boolean, string, number, dimension, point, and list of strings.
 * There are also Objects included; it is up to the implementor to ensure
 * that the given objects can be serialized to XML properly.
 * 
 * @author wyatt
 *
 */
public class PrefsBean {

	private Map<String, String> stringValues = new HashMap<String, String>();
	private Map<String, Number> numberValues = new HashMap<String, Number>();
	private Map<String, Boolean> booleanValues = new HashMap<String, Boolean>();
	private Map<String, List<String>> stringList = new HashMap<String, List<String>>();
	private Map<String, List<? extends Object>> objectList = new HashMap<String, List<? extends Object>>();
	private Map<String, Dimension> dimensionValues = new HashMap<String, Dimension>();
	private Map<String, Point> locationValues = new HashMap<String, Point>();
	private Map<String, Object> objectValues = new HashMap<String, Object>();
	
	public Map<String, List<? extends Object>> getObjectList() {
		return objectList;
	}
	public void setObjectList(Map<String, List<? extends Object>> objectList) {
		this.objectList = objectList;
	}
	public Map<String, Object> getObjectValues() {
		return objectValues;
	}
	public void setObjectValues(Map<String, Object> objectValues) {
		this.objectValues = objectValues;
	}
	public Map<String, Boolean> getBooleanValues() {
		return booleanValues;
	}
	public void setBooleanValues(Map<String, Boolean> booleanValues) {
		this.booleanValues = booleanValues;
	}
	public Map<String, Dimension> getDimensionValues() {
		return dimensionValues;
	}
	public void setDimensionValues(Map<String, Dimension> dimensionValues) {
		this.dimensionValues = dimensionValues;
	}
	public Map<String, Point> getLocationValues() {
		return locationValues;
	}
	public void setLocationValues(Map<String, Point> locationValues) {
		this.locationValues = locationValues;
	}
	public Map<String, Number> getNumberValues() {
		return numberValues;
	}
	public void setNumberValues(Map<String, Number> numberValues) {
		this.numberValues = numberValues;
	}
	public Map<String, List<String>> getStringList() {
		return stringList;
	}
	public void setStringList(Map<String, List<String>> stringList) {
		this.stringList = stringList;
	}
	public Map<String, String> getStringValues() {
		return stringValues;
	}
	public void setStringValues(Map<String, String> stringValues) {
		this.stringValues = stringValues;
	}
	
	
}
