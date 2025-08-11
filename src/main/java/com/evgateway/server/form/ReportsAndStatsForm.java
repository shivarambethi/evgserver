
package com.evgateway.server.form;

import java.util.Arrays;
import java.util.ArrayList;
import com.evgateway.server.pojo.User;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

public class ReportsAndStatsForm {
	private long siteId;
	private String advanceReportType;
	private long orgId;
	private String userId;
	private boolean needAMail;
	private String type;
	private String basedOn;
	private List<Long> data;
	private List<String> emails;
	private String searchText;
	private String searchBy;
	private String range;
	private String reportType;
	private String subReportType;
	private String displayBy;
	private List<Map<String, Object>> reportData;
	private String lastDate;
	private String chartType;
	private String startDate;
	private String endDate;
	private String fromtoDate;
	private boolean overAll;
	private String exportType;
	private String userTimeZone;
	private String listofIds;
	private String timeZone;
	private String orgName;
	private boolean level1;
	private boolean level2;
	private boolean level3;
	private boolean available;
	private boolean free;
	private boolean freeVen;
	private String userStandardTimeZone;
	private boolean advance;
	private String[] keys;
	private float processingFee;
	private String provision;
	private String exportBasedOn;
	private User user;
	private String role;
	private String requestType;
	private long driverId;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public ReportsAndStatsForm(final String type, final String reportType, final String basedOn, final List<Long> data,
			final String range, final String subReportType, final String displayBy, final String startDate,
			final String endDate, final String exportType) {
		this.data = new ArrayList();
		this.type = type;
		this.basedOn = basedOn;
		this.data = data;
		this.range = range;
		this.reportType = reportType;
		this.subReportType = subReportType;
		this.displayBy = displayBy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exportType = exportType;
	}

	public ReportsAndStatsForm(final String reportType, final String basedOn, final List<Long> data, final String range,
			final String subReportType, final String displayBy, final String startDate, final String endDate,
			final String exportType, final boolean overAll) {
		this.data = new ArrayList();
		this.basedOn = basedOn;
		this.data = data;
		this.range = range;
		this.reportType = reportType;
		this.subReportType = subReportType;
		this.displayBy = displayBy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.exportType = exportType;
		this.overAll = overAll;
	}

	public ReportsAndStatsForm(final String type, final String basedOn, final List<Long> data, final String range,
			final String startDate, final String endDate, final String exportType, final String userTimeZone,
			final boolean freeVen, final String timeZone, final float processingFee, final String provision,
			final String exportBasedOn) {
		this.data = new ArrayList();
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
		this.processingFee = processingFee;
		this.provision = provision;
		this.exportBasedOn = exportBasedOn;
	}

	public ReportsAndStatsForm(final String type, final String basedOn, final List<Long> data, final String range,
			final String startDate, final String endDate, final String exportType, final String userTimeZone,
			final boolean freeVen, final String timeZone, final String reportType, final boolean advance,
			final String[] keys, final String advanceReportType, final String startDate2) {
		this.data = new ArrayList();
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
		this.advanceReportType = advanceReportType;
		this.startDate = startDate2;
	}

	public ReportsAndStatsForm(final String type, final String basedOn, final List<Long> data, final String range,
			final String startDate, final String endDate, final String exportType, final String userTimeZone,
			final boolean freeVen, final String timeZone, final String exportBasedOn) {
		this.data = new ArrayList();
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
		this.exportBasedOn = exportBasedOn;
	}

	public ReportsAndStatsForm() {
		this.data = new ArrayList();
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getSearchText() {
		return this.searchText;
	}

	public void setSearchText(final String searchText) {
		this.searchText = searchText;
	}

	public String getSearchBy() {
		return this.searchBy;
	}

	public void setSearchBy(final String searchBy) {
		this.searchBy = searchBy;
	}

	public String getFromtoDate() {
		return this.fromtoDate;
	}

	public void setFromtoDate(final String fromtoDate) {
		this.fromtoDate = fromtoDate;
	}

	public String getListofIds() {
		return this.listofIds;
	}

	public void setListofIds(final String listofIds) {
		this.listofIds = listofIds;
	}

	public boolean isLevel1() {
		return this.level1;
	}

	public void setLevel1(final boolean level1) {
		this.level1 = level1;
	}

	public boolean isLevel2() {
		return this.level2;
	}

	public void setLevel2(final boolean level2) {
		this.level2 = level2;
	}

	public boolean isLevel3() {
		return this.level3;
	}

	public void setLevel3(final boolean level3) {
		this.level3 = level3;
	}

	public boolean isAvailable() {
		return this.available;
	}

	public void setAvailable(final boolean available) {
		this.available = available;
	}

	public boolean isFree() {
		return this.free;
	}

	public void setFree(final boolean free) {
		this.free = free;
	}

	public String[] getKeys() {
		return this.keys;
	}

	public void setKeys(final String[] keys) {
		this.keys = keys;
	}

	public float getProcessingFee() {
		return this.processingFee;
	}

	public void setProcessingFee(final float processingFee) {
		this.processingFee = processingFee;
	}

	public String getExportBasedOn() {
		return this.exportBasedOn;
	}

	public void setExportBasedOn(final String exportBasedOn) {
		this.exportBasedOn = exportBasedOn;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(final String reportType) {
		this.reportType = reportType;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(final String subReportType) {
		this.subReportType = subReportType;
	}

	public String getDisplayBy() {
		return this.displayBy;
	}

	public void setDisplayBy(final String displayBy) {
		this.displayBy = displayBy;
	}

	public String getBasedOn() {
		return this.basedOn;
	}

	public void setBasedOn(final String basedOn) {
		this.basedOn = basedOn;
	}

	public List<Long> getData() {
		return (List<Long>) this.data;
	}

	public void setData(final List<Long> data) {
		this.data = data;
	}

	public String getRange() {
		return this.range;
	}

	public void setRange(final String range) {
		this.range = range;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final String endDate) {
		this.endDate = endDate;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getExportType() {
		return this.exportType;
	}

	public void setExportType(final String exportType) {
		this.exportType = exportType;
	}

	public String getUserTimeZone() {
		return this.userTimeZone;
	}

	public void setUserTimeZone(final String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(final String orgName) {
		this.orgName = orgName;
	}

	public boolean isFreeVen() {
		return this.freeVen;
	}

	public void setFreeVen(final boolean freeVen) {
		this.freeVen = freeVen;
	}

	public boolean isAdvance() {
		return this.advance;
	}

	public void setAdvance(final boolean advance) {
		this.advance = advance;
	}

	public String getProvision() {
		return this.provision;
	}

	public void setProvision(final String provision) {
		this.provision = provision;
	}

	public boolean isOverAll() {
		return this.overAll;
	}

	public void setOverAll(final boolean overAll) {
		this.overAll = overAll;
	}

	public long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(final long orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public List<Map<String, Object>> getReportData() {
		return (List<Map<String, Object>>) this.reportData;
	}

	public void setReportData(final List<Map<String, Object>> reportData) {
		this.reportData = reportData;
	}

	@Override
	public String toString() {
		return "ReportsAndStatsForm [siteId=" + siteId + ", advanceReportType=" + advanceReportType + ", orgId=" + orgId
				+ ", userId=" + userId + ", needAMail=" + needAMail + ", type=" + type + ", basedOn=" + basedOn
				+ ", data=" + data + ", emails=" + emails + ", searchText=" + searchText + ", searchBy=" + searchBy
				+ ", range=" + range + ", reportType=" + reportType + ", subReportType=" + subReportType
				+ ", displayBy=" + displayBy + ", reportData=" + reportData + ", lastDate=" + lastDate + ", chartType="
				+ chartType + ", startDate=" + startDate + ", endDate=" + endDate + ", fromtoDate=" + fromtoDate
				+ ", overAll=" + overAll + ", exportType=" + exportType + ", userTimeZone=" + userTimeZone
				+ ", listofIds=" + listofIds + ", timeZone=" + timeZone + ", orgName=" + orgName + ", level1=" + level1
				+ ", level2=" + level2 + ", level3=" + level3 + ", available=" + available + ", free=" + free
				+ ", freeVen=" + freeVen + ", advance=" + advance + ", keys=" + Arrays.toString(keys)
				+ ", processingFee=" + processingFee + ", provision=" + provision + ", exportBasedOn=" + exportBasedOn
				+ ", user=" + user + ", role=" + role + ", requestType=" + requestType + ", driverId=" + driverId + "]";
	}

	public String getLastDate() {
		return this.lastDate;
	}

	public void setLastDate(final String lastDate) {
		this.lastDate = lastDate;
	}

	public boolean isNeedAMail() {
		return this.needAMail;
	}

	public void setNeedAMail(final boolean needAMail) {
		this.needAMail = needAMail;
	}

	public String getAdvanceReportType() {
		return this.advanceReportType;
	}

	public void setAdvanceReportType(final String advanceReportType) {
		this.advanceReportType = advanceReportType;
	}

	public String getChartType() {
		return this.chartType;
	}

	public void setChartType(final String chartType) {
		this.chartType = chartType;
	}

	public List<String> getEmails() {
		return (List<String>) this.emails;
	}

	public void setEmails(final List<String> emails) {
		this.emails = emails;
	}

	public String getUserStandardTimeZone() {
		return userStandardTimeZone;
	}

	public void setUserStandardTimeZone(String userStandardTimeZone) {
		this.userStandardTimeZone = userStandardTimeZone;
	}
}
