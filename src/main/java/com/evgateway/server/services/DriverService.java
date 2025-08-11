package com.evgateway.server.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.FilterForm;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.form.VehicleForm;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.model.AppConfigSettingResponse;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.AutoReload;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Notification;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.PreferredNotification;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.ReleaseNote;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.Vehicles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Transactional(propagation = Propagation.REQUIRED)
public interface DriverService {

	List<Map<String, Object>> getRecentlyUseStations(String useruid) throws UserNotFoundException;

	List<Map<String, Object>> getStation(String useruid, String filter) throws UserNotFoundException;

	List<Map<String, Object>> getFavoriteStations(String useruid) throws UserNotFoundException;

	List<Map<String, Object>> getDashboardReports(String useruid, int id, int period) throws UserNotFoundException;

	List<Map<String, Object>> getChartReports(String useruid, int id, int period) throws UserNotFoundException;

	ByteArrayInputStream getPDFReports(String useruid, int period, String type)
			throws IOException, UserNotFoundException;

	List<Map<String, Object>> addFavoriteStations(String useruid, long id)
			throws UserNotFoundException, ServerException;

	void addGridKeyRequest(String useruid, long count) throws ServerException, UserNotFoundException, IOException;

	PagedResult<Map<String, String>> getTransactionByAccount(String useruid, int page, int size)
			throws UserNotFoundException;

	List<Map<String, Object>> getStationInfo(String uid, String useruid)
			throws UserNotFoundException, JsonMappingException, JsonProcessingException, ServerException;

	String setFavouriteStationToUser(String uid, String useruid)
			throws ServerException, UserNotFoundException, IOException;

	List<Map<String, Object>> getRecentUsedStationByUser(String userUID)
			throws ServerException, UserNotFoundException, IOException;

	List<Map<String, Object>> getFavouriteStationByUser(String userUID)
			throws ServerException, UserNotFoundException, IOException;

	List<Map<String, Object>> getRFIDCardsByUId(String uid) throws ServerException, UserNotFoundException, IOException;

//	void addRfid(GridKeyReqForm gridKeyRequestsForm) throws UserNotFoundException, ServerException;

	PagedResult<List<Map<String, Object>>> getData(String useruid, FilterForm filerForm, int page, int size)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	void setAutoReloadToUser(String useruid) throws ServerException, UserNotFoundException, IOException, ParseException;

	PagedResult<Map<String, String>> getVehicles(long id, int page, int size, String search)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	void addVechile(VehicleForm vehicleForm) throws ServerException, UserNotFoundException, IOException, ParseException;

	List<Map<String, String>> getVehicleBrands()
			throws ServerException, UserNotFoundException, IOException, ParseException;

	List<Map<String, String>> getVehicleMake(String id)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	List<Map<String, Object>> getVehicleYearBasedOnMakeId(String makeID)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	List<Map<String, Object>> getUserCards(String userUID)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	Vehicles getVehicleBasedOnUId(String uID)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	void deleteVehicleBasedOnUId(String uID);

	void addAutoReload(double amount, double lowBalance, String cardId, String cardNo, String cardType, String useruid, String expiryMonth, String userUid2, String token, String customerId)
			throws UserNotFoundException, ServerException, ParseException, Exception;

	AutoReload getAutoReload(String userUID) throws UserNotFoundException;

	List<Map<String, Object>> getChargingActivityInfoBySessionUId(String sessionUId, String timeZone);

	
	AutoReload updateAutoReload(AutoReload form) throws UserNotFoundException;

	Boolean searchStationForPayasGo(String stationName) throws UserNotFoundException, ServerException;

	String setDefulatCard(String cardId, String customerId, boolean defaultCard, long id, String header, String useruid)
			throws UserNotFoundException, Exception;

	PagedResult<List<Map<String, Object>>> getChagringSessionDetails(ReportsAndStatsForm filter, int page, int size,
			String useruid) throws ParseException, UserNotFoundException, ServerException;

	ByteArrayInputStream getFile(ReportsAndStatsForm filterform, String useruid, String userstandardTimezone)
			throws IOException, UserNotFoundException, ServerException;

	ByteArrayInputStream getSessionFile(String sessionId, String timeZone, String timeZonestatndard)
			throws IOException, UserNotFoundException;

	ReleaseNote getcurrtentVersion() throws UserNotFoundException;

	List<Map<String, Object>> getTOUDataBasedOnStationUID(String second)
			throws NumberFormatException, UserNotFoundException;

	List<Map<String, Object>> getRFIDPriceDetails(String useruid) throws UserNotFoundException, ServerException;

	List<Map<String, Object>> getPromoCodeReward(String useruid) throws UserNotFoundException, ServerException;

	PagedResult<List<Map<String, Object>>> getPromoCodeRewardHistory(int page, int size, String useruid)
			throws ParseException, UserNotFoundException, ServerException;

	List<Language> getLanguages();

	Profile getByUserId(String uid) throws UserNotFoundException;

	User getUserByUID(String uid) throws UsernameNotFoundException;

	List<Notification> getNotifications(String useruid) throws UserNotFoundException, ServerException;

	Accounts getAccountByUser(String uid) throws UserNotFoundException;

	PreferredNotification updateNotification(PreferredNotification preferredNotification, String useruid)
			throws UserNotFoundException, ServerException;

	PreferredNotification getNotification(String useruid) throws UserNotFoundException, ServerException;

	boolean istwofa(String uid) throws UserNotFoundException;

	PagedResult<Map<String, String>> getGeneratedReport(int page, int size, String useruid)
			throws UserNotFoundException;

	ByteArrayInputStream getGeneratedReportsExport(long reportId, String userUID)
			throws IOException, UserNotFoundException, ServerException;

	void updateUser(String id, Map<String, Object> user) throws UserNotFoundException, ServerException;

	void updateUserAddress(String id, Address address) throws UserNotFoundException, ServerException;

	void updatePassword(String id, Map<String, Object> password, boolean check)
			throws UserNotFoundException, ServerException;

	Password changeUserPassword1(User user, String password) throws ServerException, UserNotFoundException;

	void updateProfile(String uid, Profile profile)
			throws UserNotFoundException, DuplicateUserException, DuplicateUserException;

	List<Map<String, Object>> getinfoeditBySessionIds(String sessionId)
			throws JsonMappingException, JsonProcessingException;

	boolean isAmount(double amount, String useruid) throws UserNotFoundException;

	boolean isAmountForautoReload(double amount, String useruid) throws UserNotFoundException, ServerException;

	List<Map<String, Object>> getVehicleCapacityBasedOnyear(String makeID, String year, String useruid)
			throws ServerException, UserNotFoundException, IOException, ParseException;

	boolean emailPromotional(long userId) throws UserNotFoundException;

	List<Map<String, Object>> getDynamicProfileInfoBySessionId(String sessionId, String timeZone);

	List<Map<String, Object>> getCardDetails(String userUID) throws UserNotFoundException;

	public void updateistwofa(Long userId, boolean isTwofa);

	Map<String, Object> getStripeKeys(long orgId);

	public void updateRfidData(long id, String name) throws UserNotFoundException;

	Boolean isRFIDNameExists(String rfidName) throws UserNotFoundException, ServerException;

	AppConfigSettingResponse getappconfig(String uid) throws UserNotFoundException;

	boolean isAmountlowbalance(double amount, String useruid) throws UserNotFoundException, ServerException;

	List<Map<String, Object>> addBalanceToUser(String cardId, String customerId, double amount, String useruid,
			String token) throws Exception;

}
