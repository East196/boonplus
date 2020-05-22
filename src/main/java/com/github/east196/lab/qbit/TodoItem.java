package com.github.east196.lab.qbit;

import java.util.Date;

public class TodoItem {

	private final String description;
	private final String name;
	private final Date due;

	public TodoItem(String description, String name, Date due) {
		this.description = description;
		this.name = name;
		this.due = due;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public Date getDue() {
		return due;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((due == null) ? 0 : due.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TodoItem other = (TodoItem) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (due == null) {
			if (other.due != null)
				return false;
		} else if (!due.equals(other.due))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TodoItem [description=" + description + ", name=" + name + ", due=" + due + "]";
	}

}