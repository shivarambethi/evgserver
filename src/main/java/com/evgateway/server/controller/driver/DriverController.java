package com.evgateway.server.controller.driver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.FilterForm;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.form.VehicleForm;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.messages.ResponseMessage;
import com.evgateway.server.model.AppConfigSettingResponse;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.AutoReload;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Notification;
import com.evgateway.server.pojo.PreferredNotification;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.ReleaseNote;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.Vehicles;
import com.evgateway.server.services.DriverService;
import com.evgateway.server.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.ApiOperation;

@RestController
@Scope("request")

@RequestMapping(value = "/services/driver", produces = { "application/json" })
public class DriverController {

	private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

	@Autowired
	private DriverService driverService;

	// @ApiOperation(value = "For DashBord Report")
	@RequestMapping(value = "/dashboard/{useruid}/{id}/{period}", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<List<Map<String, Object>>> getDashboardReports(@PathVariable String useruid,
			@PathVariable int id, @PathVariable int period) throws ParseException, UserNotFoundException {

		logger.info("DriverController.getDashboardReports() -id [" + id + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getDashboardReports(useruid, id, period));
	}

	// @ApiOperation(value = "For Chart Report")
	@RequestMapping(value = "chart/{useruid}/{id}/{period}", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<List<Map<String, Object>>> getChartReports(@PathVariable String useruid, @PathVariable int id,
			@PathVariable int period) throws ParseException, UserNotFoundException {

		logger.info("DriverController.getChartReports() -id [" + id + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getChartReports(useruid, id, period));

	}

	// @ApiOperation(value = "For All Stations")
	@GetMapping("/station/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getStation(@PathVariable String useruid,
			@RequestParam(defaultValue = "null") String filter)
			throws ParseException, UserNotFoundException, ServerException {

		logger.info("DriverController.getStation() - []");
		Pair<String, String> pair = Utils.validateParmas(null, filter);

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getStation(useruid, pair.getSecond()));

	}

	// @ApiOperation(value = "For Get Recently Use Stations")
	@GetMapping("/recentlyUseStation/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getRecentlyUseStations(@PathVariable String useruid)
			throws ParseException, UserNotFoundException {

		logger.info("DriverController.getRecentlyUseStations() - []");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getRecentlyUseStations(useruid));

	}

	// @ApiOperation(value = "For Get Favorate Stations")
	@GetMapping("/favoriteStation/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getFavoriteStations(@PathVariable String useruid)
			throws ParseException, UserNotFoundException {

		logger.info("DriverController.getFavoriteStations() - []");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getFavoriteStations(useruid));

	}

	// @ApiOperation(value = "For Add Favorate Station")
	@PostMapping("/favoriteStation/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> addFavoriteStations(@PathVariable String useruid,
			@RequestBody long id) throws UserNotFoundException, ServerException {

		logger.info("DriverController.addFavoriteStations() -with id [" + id + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.addFavoriteStations(useruid, id));

	}

	// @ApiOperation(value = "For Add GridKet Request")
	@PostMapping("/requestrfids/{useruid}")
	public ResponseEntity<ResponseMessage> addGridKeyRequest(@PathVariable String useruid, @RequestBody long count)
			throws UserNotFoundException, ServerException, IOException {

		logger.info("DriverController.addGridKeyRequest() - count [" + count + "]");

		driverService.addGridKeyRequest(useruid, count);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("RFID Request Successfully Requested"));
	}

	// @ApiOperation(value = "For Download File")
	@PostMapping("/download/{useruid}/{range}/{type}")
	public ResponseEntity<InputStreamResource> downloadReport(@PathVariable String useruid, @PathVariable int range,
			@PathVariable String type) throws UserNotFoundException, ServerException, IOException {

		logger.info("DriverController.downloadReport() - with [" + type + "]");

		ByteArrayInputStream in = driverService.getPDFReports(useruid, range, type);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

	}

	// @ApiOperation(value = "For TableData Transaction Based On Driver")
	@GetMapping(value = "/table_data/transaction", params = { "page", "size", "uid" })
	public ResponseEntity<PagedResult<Map<String, String>>> getTransactionByAccount(@RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam("uid") String uid) throws UserNotFoundException {

		logger.info("DriverController.getTransactionByAccount() - with [" + page + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getTransactionByAccount(uid, page, size));

	}

	// @ApiOperation(value = "For Station Info")
	@RequestMapping(value = "/station/info/{useruid}/{uid}", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<List<Map<String, Object>>> getStationInfo(@PathVariable String useruid,
			@PathVariable String uid) throws ParseException, UserNotFoundException, JsonMappingException,
			JsonProcessingException, ServerException {

		logger.info("DriverController.getStationInfo() -uid [" + uid + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getStationInfo(uid, useruid));
	}

	// @ApiOperation(value = "set Favourite Station To User")
	@RequestMapping(value = "/station/setFavouriteStationToUser/{uid}/{useruid}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseEntity<ResponseMessage> setFavouriteStationToUser(@PathVariable String uid,
			@PathVariable String useruid) throws ParseException, UserNotFoundException, ServerException, IOException {

		logger.info("DriverController.setFavouriteStationToUser() -uid [" + uid + "]");
		String results = driverService.setFavouriteStationToUser(uid, useruid);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(results));
	}

	// @ApiOperation(value = "For All Recent Stations based On userUID")
	@GetMapping("/station/recent")
	public ResponseEntity<List<Map<String, Object>>> getRecentUsedStationByUser(
			@RequestParam(defaultValue = "null") String userUID)
			throws ParseException, UserNotFoundException, ServerException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getRecentUsedStationByUser(userUID));

	}

	// @ApiOperation(value = "For All Recent Stations based On userUID")
	@GetMapping("/station/favourite")
	public ResponseEntity<List<Map<String, Object>>> getFavouriteStationByUser(
			@RequestParam(defaultValue = "null") String userUID)
			throws ParseException, UserNotFoundException, ServerException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getFavouriteStationByUser(userUID));

	}

	// @ApiOperation(value = "Get RFIDs of Users By userUId")
	@GetMapping("/rfidlist/{uid}")
	public ResponseEntity<List<Map<String, Object>>> getRFIDCardsByUId(@PathVariable String uid)
			throws UserNotFoundException, ServerException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getRFIDCardsByUId(uid));
	}

	@GetMapping("/getcurrtentVersion")
	public ResponseEntity<ReleaseNote> getcurrtentVersion() throws ServerException, UserNotFoundException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getcurrtentVersion());

	}

	// @ApiOperation(value = "Get Search Data")
	@RequestMapping(value = "/chargingactivity/{useruid}", params = { "page", "size" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<PagedResult<List<Map<String, Object>>>> getChargingActivityData(@PathVariable String useruid,
			@RequestParam("page") int page, @RequestParam("size") int size, @RequestBody FilterForm filerForm)
			throws ParseException, UserNotFoundException, ServerException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getData(useruid, filerForm, page, size));
	}

	// @ApiOperation(value = "Get chargingactivity Info By Session uuid")
	@GetMapping("/chargingactivity/info/{uuid}/{timeZone}")
	public ResponseEntity<List<Map<String, Object>>> getChargingActivityInfoBySessionUId(@PathVariable String uuid,
			@PathVariable String timeZone) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.getChargingActivityInfoBySessionUId(uuid, timeZone));
	}

	// @ApiOperation(value = "add AutoReload")

	// @ApiOperation(value = "update AutoReload")
	@PutMapping("/payment/updateAutoReload")
	public ResponseEntity<AutoReload> updateAutoReload(@RequestBody AutoReload form)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.updateAutoReload(form));
	}

	// @ApiOperation(value = "update AutoReload")
	@GetMapping("/payment/getAutoReload/{userUID}")
	public ResponseEntity<AutoReload> updateAutoReload(@PathVariable String userUID)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getAutoReload(userUID));
	}

	// @ApiOperation(value = "set default card")
	@PutMapping("/payment/setdefaultcard/{useruid}")
	public ResponseEntity<ResponseMessage> setDefulatCard(
			@RequestParam(name = "cardId", required = true, defaultValue = "") String cardId,
			@RequestParam(name = "customerId", required = true, defaultValue = "") String customerId,
			@RequestParam(name = "defaultCard", required = true, defaultValue = "") boolean defaultCard,
			@RequestParam long id, @RequestParam String header, @PathVariable String useruid) throws Exception {
		String msg = driverService.setDefulatCard(cardId, customerId, defaultCard, id, header, useruid);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
	}

	// @ApiOperation(value = "search station payasyougo")
	@GetMapping("/payasgo/station/{stationName}")
	public ResponseEntity<Boolean> searchStationForPayasGo(@PathVariable String stationName)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.searchStationForPayasGo(stationName));
	}

	// @ApiOperation(value = "Export Report")
	@PostMapping("/chargingactivity/export/{sessionId}/{timeZone}/{timeZonestatndard}")
	public ResponseEntity<InputStreamResource> getExport(@PathVariable String sessionId, @PathVariable String timeZone,
			@PathVariable String timeZonestatndard) throws Exception {

		logger.info("ChargingActivityController.getReport() - by []");

		ByteArrayInputStream in;
		try {
			in = driverService.getSessionFile(sessionId, timeZone, timeZonestatndard);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=customers.pdf");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			throw new ServerException("Something Wnet Wrong!");
		}

	}

	// @ApiOperation(value = "Disable AutoReload")
	@GetMapping("/payment/autoreload/{useruid}")
	public ResponseEntity<ResponseMessage> setAutoReloadToUser(@PathVariable String useruid)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		driverService.setAutoReloadToUser(useruid);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("AutoReload Disabled Successfully"));
	}

	// @ApiOperation(value = "Get vehicle By account Id")
	@GetMapping("/vehicles/{id}")
	public ResponseEntity<PagedResult<Map<String, String>>> getVechileById(@PathVariable long id,
			@RequestParam("page") int page, @RequestParam("size") int size,
			@RequestParam(name = "filter", required = false, defaultValue = "") String filter)
			throws UserNotFoundException, ServerException, IOException, ParseException {
		logger.debug("DriverController.getVechileById() - by [" + id + "]");
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getVehicles(id, page, size, filter));

	}

	// @ApiOperation(value = "Add vehicle")
	@PostMapping("/vehicle/add")
	public ResponseEntity<ResponseMessage> addVechile(@RequestBody VehicleForm vehicleForm) throws Exception {
		logger.info("DriverController.addVechile() - by []");
		driverService.addVechile(vehicleForm);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Vehicle Added Successfully"));
	}

	// @ApiOperation(value = "update vehicle")
	@PutMapping("/vehicle/update")
	public ResponseEntity<ResponseMessage> updateVechile(@RequestBody VehicleForm vehicleForm) throws Exception {
		logger.info("DriverController.addVechile() - by []");
		driverService.addVechile(vehicleForm);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Vehicle Updated Successfully"));
	}

	// @ApiOperation(value = "Get VehicleBrands")
	@GetMapping("/vehicle/brands")
	public ResponseEntity<List<Map<String, String>>> getVehicleBrands()
			throws UserNotFoundException, ServerException, IOException, ParseException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getVehicleBrands());
	}

	// @ApiOperation(value = "Get VehicleBrands")
	@GetMapping("/vehicle/make/{brandID}")
	public ResponseEntity<List<Map<String, String>>> getVehicleMake(@PathVariable String brandID)
			throws UserNotFoundException, ServerException, IOException, ParseException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getVehicleMake(brandID));
	}

	// @ApiOperation(value = "Get VehicleYearBasedOnMakeId")
	@GetMapping("/vehicle/year/{makeID}")
	public ResponseEntity<List<Map<String, Object>>> getVehicleYearBasedOnMakeId(@PathVariable String makeID)
			throws UserNotFoundException, ServerException, IOException, ParseException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getVehicleYearBasedOnMakeId(makeID));
	}

	// @ApiOperation(value = "Get VehiclebatterycapacityBasedOnyear")
	@GetMapping("/vehicle/batterycapacity/{makeID}/{year}/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getVehicleCapacityBasedOnyear(@PathVariable String makeID,
			@PathVariable String year, @PathVariable String useruid)
			throws UserNotFoundException, ServerException, IOException, ParseException {

		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.getVehicleCapacityBasedOnyear(makeID, year, useruid));
	}

	// @ApiOperation(value = "Get Vehicle BasedOn UId")
	@GetMapping("/vehicle/{UID}")
	public ResponseEntity<Vehicles> getVehicleBasedOnUId(@PathVariable String UID)
			throws UserNotFoundException, ServerException, IOException, ParseException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getVehicleBasedOnUId(UID));
	}

	// @ApiOperation(value = "Delete Vehicle BasedOn UId")
	@DeleteMapping("/vehicle/delete/{UID}")
	public ResponseEntity<ResponseMessage> deleteVehicleBasedOnUId(@PathVariable String UID)
			throws UserNotFoundException, ServerException, IOException, ParseException {
		driverService.deleteVehicleBasedOnUId(UID);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Vehicle Deleted Successfully"));
	}

	// @ApiOperation(value = "Get UserCards")
	@GetMapping("/payment/usercards")
	public ResponseEntity<List<Map<String, Object>>> getUserCards(
			@RequestParam(defaultValue = "", required = true) String userUID)
			throws UserNotFoundException, ServerException, IOException, ParseException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getCardDetails(userUID));
	}

	// @ApiOperation(value = "add Balance To User")

	// @ApiOperation("Export Report And Stats")
	@PostMapping({ "/chargingactivity/report/{useruid}/{userstandardTimezone}" })
	public ResponseEntity<InputStreamResource> downloadFile(@RequestBody ReportsAndStatsForm reportsAndStatsForm,
			@PathVariable String useruid, @PathVariable String userstandardTimezone)
			throws IOException, ServerException, UserNotFoundException, ParseException {
		logger.info("DriverController.downloadFile() - basedOn [" + reportsAndStatsForm.getBasedOn() + "]  data ["
				+ reportsAndStatsForm.getData() + "] type : [" + reportsAndStatsForm.getData() + "] rangeValue,range ["
				+ reportsAndStatsForm.getRange() + "]");
		ByteArrayInputStream in = null;
		in = driverService.getFile(reportsAndStatsForm, useruid, userstandardTimezone);
		final HttpHeaders headers = new HttpHeaders();
		final String name = "Reports";
		headers.add("Content-Disposition", "attachment; filename=\"" + name + "\"");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	// @ApiOperation(value = "Search For Report And Stats")
	@PostMapping(value = "/chargingDetails/{useruid}", params = { "page", "size" })
	public ResponseEntity<PagedResult<List<Map<String, Object>>>> getChagringSessionDetails(
			@RequestParam("page") int page, @RequestParam("size") int size, @RequestBody ReportsAndStatsForm filerForm,
			@PathVariable String useruid) throws ParseException, UserNotFoundException, ServerException {

		logger.debug("DriverController.getChagringSessionDetails() - with [" + filerForm.getType() + "]");

		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.getChagringSessionDetails(filerForm, page, size, useruid));
	}

	// @ApiOperation(value = "get tou data")
	@GetMapping("/tou/getdata")
	public ResponseEntity<List<Map<String, Object>>> getTOUDataBasedOnStationUID(
			@RequestParam(defaultValue = "null") String stationUID)
			throws ParseException, UserNotFoundException, ServerException {

		logger.info("DriverController.getTOUDataBasedOnStationUID() - []");
		Pair<String, String> pair = Utils.validateParmas(null, stationUID);

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getTOUDataBasedOnStationUID(pair.getSecond()));

	}

	// @ApiOperation(value = "Get rfid price details ")
	@GetMapping("/rfid/price/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getRFIDPriceDetails(@PathVariable String useruid)
			throws UserNotFoundException, ServerException, IOException, ParseException {
		return ResponseEntity.status(HttpStatus.OK).body(driverService.getRFIDPriceDetails(useruid));
	}

	// @ApiOperation(value = "get Promo Code Reward")
	@GetMapping("/promocode/reward/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getPromoCodeReward(@PathVariable String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		logger.info("DriverController.getPromoCodeReward()");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getPromoCodeReward(useruid));
	}

	// @ApiOperation(value = "get PromoCode Reward History")
	@GetMapping(value = "/promocode/history/{useruid}", params = { "page", "size" })
	public ResponseEntity<PagedResult<List<Map<String, Object>>>> getPromoCodeRewardHistory(
			@RequestParam("page") int page, @RequestParam("size") int size, @PathVariable String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		logger.debug("DriverController.getPromoCodeRewardHistory()");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getPromoCodeRewardHistory(page, size, useruid));
	}

	@GetMapping("/getLanguages")
	public List<Language> getLanguages() {
		return driverService.getLanguages();
	}

	@GetMapping("/user/{userId}")
	public Profile getByUserId(@PathVariable String userId) throws UserNotFoundException {
		// logger.info("userId in getByUserId is--------------------"+userId);

		return driverService.getByUserId(userId);
	}

	// @ApiOperation(value = "Get User By Id")
	@GetMapping("/users/uid/{id}")
	public ResponseEntity<User> getById(@PathVariable String id) throws UserNotFoundException {

		logger.debug("UserController.getById() - by [" + id + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getUserByUID(id));

	}

	@GetMapping("notification/{useruid}")
	public ResponseEntity<List<Notification>> getNotification(@PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.info("DriverController.getNotification() - with limit  []");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getNotifications(useruid));
	}

	// @ApiOperation(value = "Get Account By User Id")
	@GetMapping("/accounts/{uid}")
	public ResponseEntity<Accounts> getAccountByUser(@PathVariable String uid) throws Exception {

		logger.debug("invoked getAccountByUser().....");

		return new ResponseEntity<Accounts>(driverService.getAccountByUser(uid), HttpStatus.OK);
	}

	// @ApiOperation(value = "update notification ")
	@PutMapping("/notification/update/{useruid}")
	public ResponseEntity<PreferredNotification> updateNotification(
			@RequestBody PreferredNotification preferredNotification, @PathVariable String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		logger.debug("DriverController.updateNotification()");

		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.updateNotification(preferredNotification, useruid));
	}

	// @ApiOperation(value = "update notification ")
	@GetMapping("/notification/get/{useruid}")
	public ResponseEntity<PreferredNotification> getNotificationByUser(@PathVariable String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		logger.debug("DriverController.getNotificationByUser()");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getNotification(useruid));
	}

	// @ApiOperation(value = "is Two Factor Authentication")
	@GetMapping("/istwofa/{userId}")
	public ResponseEntity<Boolean> istwofa(@PathVariable String userId) throws UserNotFoundException, ServerException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.istwofa(userId));
	}

	// @ApiOperation(value = "For TableData Transaction Based On Driver")
	@GetMapping(value = "/table_data/generatedreports/{useruid}", params = { "page", "size" })
	public ResponseEntity<PagedResult<Map<String, String>>> getGeneratedReport(@RequestParam("page") int page,
			@RequestParam("size") int size, @PathVariable String useruid) throws UserNotFoundException {

		logger.info("DriverController.getGeneratedReport() - with [" + page + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getGeneratedReport(page, size, useruid));

	}

	// @ApiOperation(value = "Export Report")
	@PostMapping("/generatedreports/export/{reportId}/{userUID}")
	ResponseEntity<InputStreamResource> getGeneratedReportsExport(@PathVariable long reportId,
			@PathVariable String userUID) throws Exception {

		logger.info("ChargingActivityController.getGeneratedReportsExport() - by userUID" + userUID);

		ByteArrayInputStream in;
		try {
			in = driverService.getGeneratedReportsExport(reportId, userUID);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=customers.pdf");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			throw new ServerException("Something Went Wrong!");
		}

	}

	// @ApiOperation(value = "info edit By Session Id")
	@GetMapping("/infoedit/{sessionId}")
	public ResponseEntity<List<Map<String, Object>>> getinfoeditBySessionId(@PathVariable String sessionId)
			throws JsonMappingException, JsonProcessingException {

		logger.debug("ChargingActivityController.getChartDataBySessionId() -[" + sessionId + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getinfoeditBySessionIds(sessionId));
	}

	// @ApiOperation(value = "Update User by UserUId")
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody Map<String, Object> user)
			throws Exception {

		logger.debug("UserController.updateUser() - with [" + user + "]");

		driverService.updateUser(id, user);

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getUserByUID(id));
	}

	// @ApiOperation(value = "Update Address By User Id")
	@PutMapping("/address/{id}")
	public ResponseEntity<User> updateAddress(@PathVariable String id, @RequestBody Address address)
			throws ParseException, UserNotFoundException, ServerException {

		logger.debug("AddressController.updateAddress() - by User id  [" + id + "," + address + "]");

		driverService.updateUserAddress(id, address);

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getUserByUID(id));
	}

	// @ApiOperation(value = "Update Password Of User")
	@PutMapping("/users/password/{id}")
	public ResponseEntity<User> updatePassword(@PathVariable String id, @RequestBody Map<String, Object> passwordData)
			throws Exception {

		driverService.updatePassword(id, passwordData, true);

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getUserByUID(id));
	}

	@PutMapping("/profile/{id}")
	public Profile doUpdate(@PathVariable String id, @RequestBody Profile profile)
			throws UserNotFoundException, DuplicateUserException {
		logger.info("doUpdate:" + id);
		driverService.updateProfile(id, profile);

		return driverService.getByUserId(id);
	}

	// @ApiOperation(value = "is RFID Already exists")
	@GetMapping("isAmount/{amount}/{useruid}")
	public ResponseEntity<Boolean> isAmount(@PathVariable double amount, @PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.debug("DriverController.isAmount() - amount [" + amount + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.isAmount(amount, useruid));
	}

	// @ApiOperation(value = "is RFID Already exists")
	@GetMapping("isAmountForautoReload/{amount}/{useruid}")
	public ResponseEntity<Boolean> isAmountForautoReload(@PathVariable double amount, @PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.debug("DriverController.isAmountForautoReload() - amount [" + amount + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.isAmountForautoReload(amount, useruid));
	}

	@ApiOperation(value = "is RFID Already exists")
	@GetMapping("isAmountlowbalance/{amount}/{useruid}")
	public ResponseEntity<Boolean> isAmountlowbalance(@PathVariable double amount, @PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.debug("DriverController.isAmount() - amount [" + amount + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.isAmountlowbalance(amount, useruid));
	}

	// @ApiOperation(value = "Check emailPromotional")
	@GetMapping("/emailPromotional/{userId}")
	public ResponseEntity<Boolean> getemailPromotional(@PathVariable long userId)
			throws UserNotFoundException, ServerException {

		return ResponseEntity.status(HttpStatus.OK).body(driverService.emailPromotional(userId));
	}

	// @ApiOperation(value = "Get Session dynamicProfile Info By Session Id")
	@RequestMapping(value = "dynamicProfile/info/{sessionId}/{timeZone}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getDynamicProfileInfoBySessionId(@PathVariable String sessionId,
			@PathVariable String timeZone) {

		logger.debug("ChargingActivityController.getSessionDetails() -[" + sessionId + "]");

		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.getDynamicProfileInfoBySessionId(sessionId, timeZone));
	}

	@PutMapping("/profile/update/{userId}/{istwofa}")
	public ResponseEntity<ResponseMessage> updateistwofa(@PathVariable Long userId, @PathVariable boolean istwofa)
			throws DuplicateUserException, UserNotFoundException {

		driverService.updateistwofa(userId, istwofa);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Two factor Authunication Enable"));
	}

	@RequestMapping(value = "/getStripeKey/{orgId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getStripeKeys(@PathVariable long orgId) throws UserNotFoundException {

		logger.info("getStripeKeys.....");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getStripeKeys(orgId));
	}

	@GetMapping("/rfidupdate/{id}/{name}")
	public ResponseEntity<ResponseMessage> updateRfid(@PathVariable long id, @PathVariable String name)
			throws UserNotFoundException, ServerException {
		logger.debug("RfidController.updateRfid() - with []");

		driverService.updateRfidData(id, name);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Rfid name updated successfully"));

	}

	@RequestMapping(value = "/isrfidnameExist/{rfidname}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isRFIDNameExists(@PathVariable String rfidname)
			throws UserNotFoundException, ServerException {

		logger.debug("RfidController.isRFIDNameExists() - rfid [" + rfidname + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.isRFIDNameExists(rfidname));
	}

	@ApiOperation(value = "info edit By Session Id")
	@GetMapping("/appconfig/{uid}")
	public ResponseEntity<AppConfigSettingResponse> getappconfig(@PathVariable String uid)
			throws JsonMappingException, JsonProcessingException, UserNotFoundException {

		logger.debug("ChargingActivityController.getappconfig() -[" + uid + "]");

		return ResponseEntity.status(HttpStatus.OK).body(driverService.getappconfig(uid));
	}

	@ApiOperation(value = "add Balance To User")
	@PostMapping(value = "/payment/addBalance")
	public ResponseEntity<List<Map<String, Object>>> addBalanceToUser(@RequestBody Map<String, Object> mapData)
			throws Exception {

		return ResponseEntity.status(HttpStatus.OK)
				.body(driverService.addBalanceToUser(mapData.get("cardId").toString(),
						mapData.get("customerId").toString(), Double.valueOf(mapData.get("amount").toString()),
						mapData.get("userUid").toString(), mapData.get("bearerToken").toString()));

	}

	@ApiOperation(value = "add AutoReload")
	@PostMapping("/payment/addAutoReload")
	public ResponseEntity<ResponseMessage> addAutoReload(@RequestParam("amount") double amount,
			@RequestParam("lowBalance") double lowBalance,
			@RequestParam(name = "cardId", required = false, defaultValue = "") String cardId,
			@RequestParam(name = "cardNo", required = false, defaultValue = "") String cardNo,
			@RequestParam(name = "cardType", required = false, defaultValue = "") String cardType,
			@RequestParam(name = "expiryYear", required = false, defaultValue = "") String expiryYear,
			@RequestParam(name = "expiryMonth", required = false, defaultValue = "") String expiryMonth,
			@RequestParam(name = "userUid", required = false, defaultValue = "") String userUid,
			@RequestParam(name = "token", required = true, defaultValue = "") String token,
			@RequestParam(name = "customerId", required = false, defaultValue = "") String customerId)
			throws Exception {
		driverService.addAutoReload(amount, lowBalance, cardId, cardNo, cardType, expiryYear, expiryMonth, userUid,
				token, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("AutoReload Update SuccessFully"));
	}
}
