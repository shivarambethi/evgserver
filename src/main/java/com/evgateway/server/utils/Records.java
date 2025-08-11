package com.evgateway.server.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Records {

	private String status;

	private String station_Refer;
	private LocalDateTime sdate;
	private LocalDateTime edate;
	private int i;
	private String stDate;

	public String getStDate() {
		return stDate;
	}

	public void setStDate(String stDate) {
		this.stDate = stDate;
	}

	public String getEdDate() {
		return edDate;
	}

	public void setEdDate(String edDate) {
		this.edDate = edDate;
	}

	private String edDate;

	private LocalDate ssdate;
	private LocalDate eedate;

	private String error_code;
	private String vendorError_code;

	private boolean activity;

	public String getPowerActiveImport() {
		return powerActiveImport;
	}

	public void setPowerActiveImport(String powerActiveImport) {
		this.powerActiveImport = powerActiveImport;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getkWUsed() {
		return kWUsed;
	}

	public void setkWUsed(String kWUsed) {
		this.kWUsed = kWUsed;
	}

	private long portId;

	private String powerActiveImport;
	private String sessionId;
	private String kWUsed;

	public Records(String status, String station_Refer, LocalDateTime sdate, LocalDateTime edate) {
		super();
		this.status = status;
		this.station_Refer = station_Refer;
		this.sdate = sdate;
		this.edate = edate;
	}

	public Records(String status, String station_Refer, LocalDate ssdate, LocalDate eedate, LocalDateTime sdate,
			LocalDateTime edate) {
		super();
		this.status = status;
		this.station_Refer = station_Refer;
		this.ssdate = ssdate;
		this.eedate = eedate;
		this.sdate = sdate;
		this.edate = edate;
	}

	public Records(String status, String station_Refer, LocalDateTime sdate, LocalDateTime edate, LocalDate ssdate,
			LocalDate eedate, String error_code, String vendorError_code, long portId) {
		super();
		this.status = status;
		this.station_Refer = station_Refer;
		this.sdate = sdate;
		this.edate = edate;
		this.ssdate = ssdate;
		this.eedate = eedate;
		this.error_code = error_code;
		this.vendorError_code = vendorError_code;
		this.portId = portId;
	}

	public Records( LocalDate ssdate, LocalDateTime sdate, boolean activity) {
		// TODO Auto-generated constructor stub

//		this.station_Refer = stationReferNo;
		this.sdate = sdate;
		this.ssdate = ssdate;
		this.activity = activity;
	}

	public Records(LocalDateTime sdate, LocalDateTime edate, long portId) {
		this.sdate = sdate;
		this.edate = edate;
		this.portId = portId;
	}

	public Records(int i, String stationReferNo, String stDate, String edDate, long portId, String powerActiveImport,
			String sessionId, String kWUsed) {
		this.i = i;
		this.station_Refer = stationReferNo;
		this.stDate = stDate;
		this.edDate = edDate;
		this.portId = portId;
		this.powerActiveImport = powerActiveImport;
		this.sessionId = sessionId;
		this.kWUsed = kWUsed;
	}

	public long getPortId() {
		return portId;
	}

	public void setPortId(long portId) {
		this.portId = portId;
	}

	public LocalDate getSsdate() {
		return ssdate;
	}

	public void setSsdate(LocalDate ssdate) {
		this.ssdate = ssdate;
	}

	public LocalDate getEedate() {
		return eedate;
	}

	public void setEedate(LocalDate eedate) {
		this.eedate = eedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStation_Refer() {
		return station_Refer;
	}

	public void setStation_Refer(String station_Refer) {
		this.station_Refer = station_Refer;
	}

	public LocalDateTime getSdate() {
		return sdate;
	}

	public void setSdate(LocalDateTime sdate) {
		this.sdate = sdate;
	}

	public LocalDateTime getEdate() {
		return edate;
	}

	public void setEdate(LocalDateTime edate) {
		this.edate = edate;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getVendorError_code() {
		return vendorError_code;
	}

	public void setVendorError_code(String vendorError_code) {
		this.vendorError_code = vendorError_code;
	}

	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "Records [status=" + status + ", station_Refer=" + station_Refer + ", sdate=" + sdate + ", edate="
				+ edate + ", ssdate=" + ssdate + ", eedate=" + eedate + ", sessionId=" + sessionId + "]";
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

}
