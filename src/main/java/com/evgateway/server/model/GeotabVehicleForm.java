

package com.evgateway.server.model;

public class GeotabVehicleForm
{
    private boolean manualflag;
    private boolean telemetryFlag;
    private boolean integrationFlag;
    private String registrationNo;
    private String manufacturer;
    private String model;
    private String batteryCapacity;
    private String range;
    private String vehicle_type;
    private String type;
    private String telemetryLink;
    private String dbname;
    private String username;
    private String password;
    private String macId;
    private long vehicleId;
    private String uuid;
    private long id;
    private String url;
    private String deviceType;
    
    public boolean isManualflag() {
        return this.manualflag;
    }
    
    public void setManualflag(final boolean manualflag) {
        this.manualflag = manualflag;
    }
    
    public boolean isTelemetryFlag() {
        return this.telemetryFlag;
    }
    
    public void setTelemetryFlag(final boolean telemetryFlag) {
        this.telemetryFlag = telemetryFlag;
    }
    
    public boolean isIntegrationFlag() {
        return this.integrationFlag;
    }
    
    public void setIntegrationFlag(final boolean integrationFlag) {
        this.integrationFlag = integrationFlag;
    }
    
    public String getRegistrationNo() {
        return this.registrationNo;
    }
    
    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }
    
    public String getManufacturer() {
        return this.manufacturer;
    }
    
    public void setManufacturer(final String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getBatteryCapacity() {
        return this.batteryCapacity;
    }
    
    public void setBatteryCapacity(final String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
    
    public String getRange() {
        return this.range;
    }
    
    public void setRange(final String range) {
        this.range = range;
    }
    
    public String getVehicle_type() {
        return this.vehicle_type;
    }
    
    public void setVehicle_type(final String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getTelemetryLink() {
        return this.telemetryLink;
    }
    
    public void setTelemetryLink(final String telemetryLink) {
        this.telemetryLink = telemetryLink;
    }
    
    public String getDbname() {
        return this.dbname;
    }
    
    public void setDbname(final String dbname) {
        this.dbname = dbname;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public void setModel(final String model) {
        this.model = model;
    }
    
    public String getMacId() {
        return this.macId;
    }
    
    public void setMacId(final String macId) {
        this.macId = macId;
    }
    
    public long getVehicleId() {
        return this.vehicleId;
    }
    
    public void setVehicleId(final long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getDeviceType() {
        return this.deviceType;
    }
    
    public void setDeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }
    
    @Override
    public String toString() {
        return "GeotabVehicleForm [manualflag=" + this.manualflag + ", telemetryFlag=" + this.telemetryFlag + ", integrationFlag=" + this.integrationFlag + ", registrationNo=" + this.registrationNo + ", manufacturer=" + this.manufacturer + ", model=" + this.model + ", batteryCapacity=" + this.batteryCapacity + ", range=" + this.range + ", vehicle_type=" + this.vehicle_type + ", type=" + this.type + ", telemetryLink=" + this.telemetryLink + ", dbname=" + this.dbname + ", username=" + this.username + ", password=" + this.password + ", macId=" + this.macId + ", vehicleId=" + this.vehicleId + ", uuid=" + this.uuid + ", id=" + this.id + ", url=" + this.url + ", deviceType=" + this.deviceType + "]";
    }
}
