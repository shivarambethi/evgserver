package com.evgateway.server.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterForm {

	private String type;

	private String basedOn;

	private List<Long> data = new ArrayList<Long>();

	private String searchText;
	private String searchBy;
	private String range;
	private String reportType;

	private String startDate;
	private String endDate;
	private String fromtoDate;

	private long siteId;

	private String exportType;
	private String userTimeZone;

	private String listofIds;
	private String timeZone;
	// For filter
	private String orgName;
	private boolean level1;
	private boolean level2;
	private boolean level3;
	private boolean available;
	private boolean free;

	private boolean freeVen;

	private boolean advance;
	private String[] keys;

	private float processingFee;
	
	
	private String provision;
	private String exportBasedOn;

	public FilterForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilterForm(String type, String basedOn, List<Long> data, String range, String startDate, String endDate,
			String exportType, String userTimeZone, boolean freeVen, String timeZone, float processingFee,String provision,String exportBasedOn) {
		this.type = type;
		this.basedOn = basedOn;
		this.data = data;
		this.range = range;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exportType = exportType;
		this.userTimeZone = userTimeZone;
		this.freeVen = freeVen;
		this.timeZone = timeZone;
		this.processingFee= processingFee;
		this.provision= provision;
		this.exportBasedOn=exportBasedOn;
	}

	public FilterForm(String type, String basedOn, List<Long> data, String range, String startDate, String endDate,
			String exportType, String userTimeZone, boolean freeVen, String timeZone, String reportType,
			boolean advance, String[] keys) {
		this.type = type;
		this.basedOn = basedOn;
		this.data = data;
		this.range = range;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exportType = exportType;
		this.userTimeZone = userTimeZone;
		this.freeVen = freeVen;
		this.timeZone = timeZone;
		this.reportType = reportType;
		this.advance = advance;
		this.keys = keys;

	}

	public FilterForm(String type, String basedOn, List<Long> data, String range, String startDate, String endDate,
			String exportType, String userTimeZone, boolean freeVen, String timeZone, String exportBasedOn) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.basedOn = basedOn;
		this.data = data;
		this.range = range;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exportType = exportType;
		this.userTimeZone = userTimeZone;
		this.freeVen = freeVen;
		this.timeZone = timeZone;
		this.exportBasedOn=exportBasedOn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getBasedOn() {
		return basedOn;
	}

	public void setBasedOn(String basedOn) {
		this.basedOn = basedOn;
	}

	public List<Long> getData() {
		return data;
	}

	public void setData(List<Long> data) {
		this.data = data;
	}

	public String getUserTimeZone() {

		return userTimeZone;
	}

	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public String getFromtoDate() {
		return fromtoDate;
	}

	public void setFromtoDate(String fromtoDate) {
		this.fromtoDate = fromtoDate;
	}

	public String getListofIds() {
		return listofIds;
	}

	public void setListofIds(String listofIds) {
		this.listofIds = listofIds;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isLevel1() {
		return level1;
	}

	public void setLevel1(boolean level1) {
		this.level1 = level1;
	}

	public boolean isLevel2() {
		return level2;
	}

	public void setLevel2(boolean level2) {
		this.level2 = level2;
	}

	public boolean isLevel3() {
		return level3;
	}

	public void setLevel3(boolean level3) {
		this.level3 = level3;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public boolean isFreeVen() {
		return freeVen;
	}

	public void setFreeVen(boolean freeVen) {
		this.freeVen = freeVen;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isAdvance() {
		return advance;
	}

	public void setAdvance(boolean advance) {
		this.advance = advance;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	
	

	public String getProvision() {
		return provision;
	}

	public void setProvision(String provision) {
		this.provision = provision;
	}

	public String getExportBasedOn() {
		return exportBasedOn;
	}

	public void setExportBasedOn(String exportBasedOn) {
		this.exportBasedOn = exportBasedOn;
	}

	@Override
	public String toString() {
		return "FilterForm [type=" + type + ", basedOn=" + basedOn + ", data=" + data + ", searchText=" + searchText
				+ ", searchBy=" + searchBy + ", range=" + range + ", reportType=" + reportType + ", startDate="
				+ startDate + ", endDate=" + endDate + ", fromtoDate=" + fromtoDate + ", siteId=" + siteId
				+ ", exportType=" + exportType + ", userTimeZone=" + userTimeZone + ", listofIds=" + listofIds
				+ ", timeZone=" + timeZone + ", orgName=" + orgName + ", level1=" + level1 + ", level2=" + level2
				+ ", level3=" + level3 + ", available=" + available + ", free=" + free + ", freeVen=" + freeVen
				+ ", advance=" + advance + ", keys=" + Arrays.toString(keys) + ", processingFee=" + processingFee
				+ ", provision=" + provision + ", exportBasedOn=" + exportBasedOn + "]";
	}

	public float getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(float processingFee) {
		this.processingFee = processingFee;
	}

}
