package com.lifetime.ouyeel.devtools.model;

/**
 * @author lifetime
 *
 */
public class FieldInfo {
	private String name;
	private String type;
	private String remark;
	private boolean isPri;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		return name + "(" + type + ")" + remark;
	}

	public boolean isPri() {
		return isPri;
	}

	public void setPri(boolean isPri) {
		this.isPri = isPri;
	}

}
