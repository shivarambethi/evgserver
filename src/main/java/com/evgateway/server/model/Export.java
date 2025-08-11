package com.evgateway.server.model;

public class Export {

	private String name;

	private String period;
	private boolean pdf;
	private boolean xls;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPdf() {
		return pdf;
	}

	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public boolean isXls() {
		return xls;
	}

	public void setXls(boolean xls) {
		this.xls = xls;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "Export [name=" + name + ", period=" + period + ", pdf=" + pdf + ", xls=" + xls + "]";
	}

}
