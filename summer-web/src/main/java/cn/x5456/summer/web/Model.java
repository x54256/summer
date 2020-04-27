package cn.x5456.summer.web;

import java.util.Map;

public interface Model {

	Model addAttribute(String attributeName, Object attributeValue);

	boolean containsAttribute(String attributeName);

	/**
	 * Return the current set of model attributes as a Map.
	 */
	Map<String, Object> asMap();

}