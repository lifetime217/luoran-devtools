package com.lifetime.ouyeel.devtools.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lifetime
 *
 */
public class TableInfo {
	private String name;
	private String remark;
	private List<FieldInfo> fields;
	
	public TableInfo() {
		fields = new ArrayList<>();
	}

	public TableInfo(String name, String remark) {
		this.name = name;
		this.remark = remark;
		fields = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
	}
	
	public String toString() {
		return name + "(" + remark + ")";
	}

}
