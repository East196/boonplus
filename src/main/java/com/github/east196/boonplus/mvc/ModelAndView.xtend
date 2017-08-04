package com.github.east196.boonplus.mvc

import java.util.Map

class ModelAndView {
	Map<String, Object> modelMap
	View view

	new(Map<String, Object> modelMap, View view) {
		this.modelMap = modelMap
		this.view = view
	}

	def ModelAndView addModel(String attributeName, Object attributeValue) {
		this.modelMap.put(attributeName, attributeValue)
		return this
	}

	def ModelAndView addModelMap(Map<String, ?> modelMap) {
		this.modelMap.putAll(modelMap)
		return this
	}

	def ModelAndView setView(View view) {
		this.view = view
		return this
	}
}
