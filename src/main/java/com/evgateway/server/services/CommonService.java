package com.evgateway.server.services;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.form.SiteTimingform;
import com.evgateway.server.pojo.Country;
import com.evgateway.server.pojo.CustomSetting;
import com.evgateway.server.pojo.Hours;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Minutes;
import com.evgateway.server.pojo.SiteTiming;
import com.evgateway.server.pojo.VantivCeditCardKeys;
import com.evgateway.server.pojo.Zone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Transactional(propagation = Propagation.REQUIRED)
public interface CommonService {

	public List<Zone> getTimeZones();

	public List<Language> getLangs();

	public List<Map<String, Object>> getDriverProfileGroups(String uesruid)
			throws UserNotFoundException, ServerException;

	public List<Minutes> getMinutes() throws UserNotFoundException;

	public List<Hours> getHours() throws UserNotFoundException;

	public List<Map<String, Object>> getDriverUser(String uesruid) throws UserNotFoundException, ServerException;

	public List<VantivCeditCardKeys> getVantivCeditCardKeys() throws UserNotFoundException;

	public String getStationImage(Long id) throws UserNotFoundException, IOException;

	// public List<Country> getCountries() throws UserNotFoundException;

	public List<Map<String, Object>> getCurrency() throws UserNotFoundException;

	public List<Map<String, Object>> getcountryCode() throws UserNotFoundException;

	public ByteArrayInputStream getReport(String sessionId, String timeZone) throws IOException, UserNotFoundException;

	public List<Country> getCountries();

	public void generateOTPForPassword(String pid, String did)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException;

	public void validateOTPForSetPassword(String pid, String did, String otp)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException;

	public void setPasswordForUser(Map<String, Object> passwordData)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException;

	public void getFile(ReportsAndStatsForm reportsAndStatsForm)
			throws IOException, UserNotFoundException, ServerException;

	List<Map<String, Object>> getLogoImage(Long orgId)
			throws ServerException, UserNotFoundException, FileNotFoundException;

	CustomSetting getColor(Long orgId) throws UserNotFoundException, ServerException;

	List<Map<String, Object>> getLogoImagebasedonorg(Long orgId) throws UserNotFoundException;

	public List<Map<String, Object>> getMap();

	public List<Map<String, Object>> getMapFilter(String stationId)
			throws UserNotFoundException, ServerException, JsonMappingException, JsonProcessingException;

	Map<String, Object> getPortinfoMethod(String userTimeZone, String id) throws UserNotFoundException;

	public List<SiteTiming> getSiteTimingBySiteId(long uuid, String timeZone) throws UserNotFoundException;

	public List<Map<String, Object>> getSitebylatlang(double lat, double lang);

	List<Map<String, Object>> getMapFilterByUid(String uid, String userTimeZone, String userstandardTimezone)
			throws JsonMappingException, JsonProcessingException, UserNotFoundException;

}
