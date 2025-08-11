package com.evgateway.server.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.evgateway.server.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "site")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler", "station", "users" })
public class Site implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long id;

	private String oldRefId;
	private String siteName;

	/* private String name; */

	private String streetNo;
	private String streetName;
	private String city;
	private String state;
	private String land_mark;
	private String country;
	private String postal_code;
	private String gps;
	private String address;

	private String parkingType;
	private double parkingperHour;

	private String adaAccess;
	private boolean privateAccess;

	private boolean fleetAccess;
	private boolean publicAccess;
	private boolean priceSyncSite;
	private boolean dataSyncSite;
	private boolean scheduledMaintenance;
	private boolean powerOutage;
	private boolean kiosk;
	private String managerName;
	private String managerEmail;
	private String managerPhone;
	private boolean open24X7;
	private String spocName;
	private String spocEmail;
	private String spocPhoneNo;
	private String spocCountryCode;

	private String installerName;
	private String installerEmail;
	private String installerPhone;
	private String installerCompanyName;
	private String installerCountryCode;

	private Date installationDate;

	private boolean siteVisibilityOnMap;
	private boolean dynamicPriceFlag;
	private boolean ocpiflag;
	private String currencyType;

	private String timeZone;

	private double processingFee;

	private String parkingPriceNotes;

	private String utility_Name;

	
	@JsonProperty
	private GeoLocation coordinates;
	@JsonProperty
	private DisplayText directions;
	@JsonProperty
	private Image images;

	@JsonIgnore
	private Set<User> users = new HashSet<User>();

	@JsonProperty
	private Organization org;

	private String currencySymbol;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "Modified_Date", length = 10)
	private Date modifiedDate;

	@Column
	private String createdBy;

	private String uuid = Utils.getUUIDString();

	private String time_zone;

	private double saleTexPerc;

	private List<String> parkingRestricitions;

	private List<String> parkingTypes;
	private List<String> facility;
	private List<String> siteAccess;

	private double freeChargingKwh;
	private boolean geofencing;
	
	@Column(name = "freeChargingKwh", columnDefinition = "double precision default 0 not null")
	public double getFreeChargingKwh() {
		return freeChargingKwh;
	}

	public void setFreeChargingKwh(double freeChargingKwh) {
		this.freeChargingKwh = freeChargingKwh;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public boolean isPriceSyncSite() {
		return priceSyncSite;
	}

	public void setPriceSyncSite(boolean priceSyncSite) {
		this.priceSyncSite = priceSyncSite;
	}

	public boolean isDataSyncSite() {
		return dataSyncSite;
	}

	public void setDataSyncSite(boolean dataSyncSite) {
		this.dataSyncSite = dataSyncSite;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column
	private String lastModifiedBy;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "siteId", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOldRefId() {
		return oldRefId;
	}

	public void setOldRefId(String oldRefId) {
		this.oldRefId = oldRefId;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = User.class)

	@JoinTable(name = "users_in_sites", joinColumns = {
			@JoinColumn(name = "siteId", nullable = false, updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), referencedColumnName = "siteId") }, inverseJoinColumns = {
					@JoinColumn(name = "userId", nullable = false, updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), referencedColumnName = "userId") })
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	

	@JoinColumn(name = "org")
	@ManyToOne(fetch = FetchType.EAGER)
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public boolean isPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

	public double getParkingperHour() {
		return parkingperHour;
	}

	public void setParkingperHour(double parkingperHour) {
		this.parkingperHour = parkingperHour;
	}

	public String getAdaAccess() {
		return adaAccess;
	}

	public void setAdaAccess(String adaAccess) {
		this.adaAccess = adaAccess;
	}

	public boolean isFleetAccess() {
		return fleetAccess;
	}

	public void setFleetAccess(boolean fleetAccess) {
		this.fleetAccess = fleetAccess;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public String getSpocName() {
		return spocName;
	}

	public void setSpocName(String spocName) {
		this.spocName = spocName;
	}

	public String getSpocEmail() {
		return spocEmail;
	}

	public void setSpocEmail(String spocEmail) {
		this.spocEmail = spocEmail;
	}

	public String getSpocPhoneNo() {
		return spocPhoneNo;
	}

	public void setSpocPhoneNo(String spocPhoneNo) {
		this.spocPhoneNo = spocPhoneNo;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public boolean isPrivateAccess() {
		return privateAccess;
	}

	public void setPrivateAccess(boolean privateAccess) {
		this.privateAccess = privateAccess;
	}

	public boolean isSiteVisibilityOnMap() {
		return siteVisibilityOnMap;
	}

	public void setSiteVisibilityOnMap(boolean siteVisibilityOnMap) {
		this.siteVisibilityOnMap = siteVisibilityOnMap;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public double getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(double processingFee) {
		this.processingFee = processingFee;
	}

	public String getInstallerName() {
		return installerName;
	}

	public void setInstallerName(String installerName) {
		this.installerName = installerName;
	}

	public String getInstallerEmail() {
		return installerEmail;
	}

	public void setInstallerEmail(String installerEmail) {
		this.installerEmail = installerEmail;
	}

	public String getInstallerPhone() {
		return installerPhone;
	}

	public void setInstallerPhone(String installerPhone) {
		this.installerPhone = installerPhone;
	}

	public String getInstallerCompanyName() {
		return installerCompanyName;
	}

	public void setInstallerCompanyName(String installerCompanyName) {
		this.installerCompanyName = installerCompanyName;
	}

	public Date getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}

	public String getParkingType() {
		return parkingType;
	}

	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}

	public String getSpocCountryCode() {
		return spocCountryCode;
	}

	public void setSpocCountryCode(String spocCountryCode) {
		this.spocCountryCode = spocCountryCode;
	}

	public String getInstallerCountryCode() {
		return installerCountryCode;
	}

	public void setInstallerCountryCode(String installerCountryCode) {
		this.installerCountryCode = installerCountryCode;
	}

	public String getParkingPriceNotes() {
		return parkingPriceNotes;
	}

	public void setParkingPriceNotes(String parkingPriceNotes) {
		this.parkingPriceNotes = parkingPriceNotes;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/*
	 * public String getSiteRef() { return siteRef; }
	 * 
	 * public void setSiteRef(String siteRef) { this.siteRef = siteRef; }
	 */

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	@JoinColumn(name = "coordinateId")
	@ManyToOne(fetch = FetchType.EAGER)
	public GeoLocation getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GeoLocation coordinates) {
		this.coordinates = coordinates;
	}

	@JoinColumn(name = "directionId")
	@ManyToOne(fetch = FetchType.EAGER)
	public DisplayText getDirections() {
		return directions;
	}

	public void setDirections(DisplayText directions) {
		this.directions = directions;
	}

	@JoinColumn(name = "imageId")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Image getImages() {
		return images;
	}

	public void setImages(Image images) {
		this.images = images;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public boolean isOcpiflag() {
		return ocpiflag;
	}

	public void setOcpiflag(boolean ocpiflag) {
		this.ocpiflag = ocpiflag;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public double getSaleTexPerc() {
		return saleTexPerc;
	}

	public void setSaleTexPerc(double saleTexPerc) {
		this.saleTexPerc = saleTexPerc;
	}

	@Transient
	public List<String> getParkingRestricitions() {
		return parkingRestricitions;
	}

	public void setParkingRestricitions(List<String> parkingRestricitions) {
		this.parkingRestricitions = parkingRestricitions;
	}

	@Transient
	public List<String> getParkingTypes() {
		return parkingTypes;
	}

	public void setParkingTypes(List<String> parkingTypes) {
		this.parkingTypes = parkingTypes;
	}

	public String getUtility_Name() {
		return utility_Name;
	}

	public void setUtility_Name(String utility_Name) {
		this.utility_Name = utility_Name;
	}

	@Transient
	public List<String> getFacility() {
		return facility;
	}

	public void setFacility(List<String> facility) {
		this.facility = facility;
	}

	public String getLand_mark() {
		return land_mark;
	}

	public void setLand_mark(String land_mark) {
		this.land_mark = land_mark;
	}

	@Transient
	public List<String> getSiteAccess() {
		return siteAccess;
	}

	public void setSiteAccess(List<String> siteAccess) {
		this.siteAccess = siteAccess;
	}

	public boolean isOpen24X7() {
		return open24X7;
	}

	public void setOpen24X7(boolean open24x7) {
		open24X7 = open24x7;
	}

	

	@Column(name = "kiosk", columnDefinition = "bit default 0 not null")
	public boolean isKiosk() {
		return kiosk;
	}

	
	public void setKiosk(boolean kiosk) {
		this.kiosk = kiosk;
	}
	@Column(name = "scheduledMaintenance", columnDefinition = "bit default 0 not null")	
	public boolean isScheduledMaintenance() {
		return scheduledMaintenance;
	}

	public void setScheduledMaintenance(boolean scheduledMaintenance) {
		this.scheduledMaintenance = scheduledMaintenance;
	}

	

	@Column(name = "geofencing", columnDefinition = "bit default 0 not null")
	public boolean isGeofencing() {
		return geofencing;
	}

	public void setGeofencing(boolean geofencing) {
		this.geofencing = geofencing;
	}
	@Column(name = "powerOutage", columnDefinition = "bit default 0 not null")
	public boolean isPowerOutage() {
		return powerOutage;
	}

	public void setPowerOutage(boolean powerOutage) {
		this.powerOutage = powerOutage;
	}
	@Column(name = "dynamicPriceFlag", columnDefinition = "bit default 0 not null")
	public boolean isDynamicPriceFlag() {
		return dynamicPriceFlag;
	}

	public void setDynamicPriceFlag(boolean dynamicPriceFlag) {
		this.dynamicPriceFlag = dynamicPriceFlag;
	}

	

	

}
