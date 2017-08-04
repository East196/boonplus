package com.github.east196.boonplus.mvc;

import java.util.Map;

public class ModelAndView {

	private Map<String, Object> modelMap;
	private View view;

	public ModelAndView(Map<String, Object> modelMap, View view) {
		this.modelMap = modelMap;
		this.view = view;
	}

	public ModelAndView addModel(String attributeName, Object attributeValue) {
		this.modelMap.put(attributeName, attributeValue);
		return this;
	}

	public ModelAndView addModelMap(Map<String, ?> modelMap) {
		this.modelMap.putAll(modelMap);
		return this;
	}

	public ModelAndView setView(View view) {
		this.view = view;
		return this;
	}

}
