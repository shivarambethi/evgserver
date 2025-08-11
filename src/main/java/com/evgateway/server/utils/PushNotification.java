//package com.evgateway.server.utils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.evgateway.server.dao.GeneralDao;
//import com.evgateway.server.pojo.ConfigurationSettings;
//import com.evgateway.server.pojo.User;
//
//@Service
//public class PushNotification {
//
//	Logger logger = LoggerFactory.getLogger(PushNotification.class);
//
//	final String apiURL = "https://fcm.googleapis.com/fcm/send";
//
//	/*
//	 * @Value("${android.serverkey}") private String androidServerKey;
//	 * 
//	 * @Value("${ios.serverkey}") private String iosServerKey;
//	 */
//
//	/*
//	 * @Value("${francis.iosServerKey}") private String francisserver;
//	 */
//
//	@Autowired
//	private GeneralDao<?, ?> generalDao;
//
//	@SuppressWarnings({ "unchecked" })
//	public void sendPushNotification(User usersObj, String orgName, String category, String body, String refId,
//			boolean systemNotification) throws Exception {
//		JSONArray androidRecipients = new JSONArray();
//		JSONArray iOSRecipients = new JSONArray();
//		JSONArray francisserverKey = new JSONArray();
//
//		Boolean notify = false;
//		if (systemNotification == false) {
//			String query = "From PreferredNotification p where p.userId  = " + usersObj.getId();
//			com.evgateway.server.pojo.PreferredNotification notification = generalDao
//					.findOneHQLQuery(new com.evgateway.server.pojo.PreferredNotification(), query);
//			if (notification != null) {
//				if (category.equalsIgnoreCase("RFID Activated")) {
//					if (notification.isRfidStatus() == true) {
//						notify = true;
//					} else
//						return;
//				} else if (category.equalsIgnoreCase("Charging Session")) {
//					// RFID Activated/Charging Session/Low Balance/Transaction/Notify Me/Station
//					// Status/User Account Deleted/User Account Updated
//				}
//			} else
//				notify = true;
//		} else {
//			notify = true;
//		}
//		if (notify == true) {
//			String query1 = "From DeviceDetails d  where d.userId ='" + usersObj.getId() + "'";
//			List<com.evgateway.server.pojo.DeviceDetails> deviceDetails = generalDao
//					.findAllHQLQuery(new com.evgateway.server.pojo.DeviceDetails(), query1);
//
//			if (deviceDetails != null) {
//				deviceDetails.forEach(device -> {
//					if (device.getDeviceType().equalsIgnoreCase("Android")) {
//						androidRecipients.add(device.getDeviceToken());
//					} else if (device.getDeviceType().equalsIgnoreCase("iOS")) {
//						logger.info("<<deviceDetails<>>>");
//						iOSRecipients.add(device.getDeviceToken());
//						francisserverKey.add(device.getDeviceToken());
//						logger.info("device.getDeviceType() : " + device.getDeviceType());
//					}
//				});
//			}
//			ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//					"From ConfigurationSettings Where orgName like '%" + orgName + "%'");
//
//			if (androidRecipients.size() > 0) {
//				logger.info("<<androidRecipients<>>>");
//				JSONObject info = new JSONObject();
//				info.put("title", orgName);
//				info.put("body", body);
//				info.put("action", category);
//				info.put("userId", usersObj.getId());
//				info.put("refId", refId);
//
//				JSONObject json = new JSONObject();
//
//				json.put("registration_ids", androidRecipients);
//				json.put("data", info);
//				logger.info("Final Response For Android : " + json);
//				pushNotification(config.getLegacykey(), androidRecipients, json);
//			}
//			if (iOSRecipients.size() > 0) {
//				/*
//				 * logger.info("<<iOSRecipients<>>>"+iOSRecipients.size()); JSONObject info =
//				 * new JSONObject(); info.put("title", orgName); // info.put("body", body);
//				 * //info.put("type", category);
//				 * 
//				 * JSONObject json = new JSONObject(); json.put("registration_ids",
//				 * iOSRecipients); json.put("notification", info);
//				 * logger.info("Final Response For IOS : "+ json);
//				 */
//				JSONObject obj2 = new JSONObject();
//				obj2.put("title", orgName);
//				obj2.put("type", category);
//				obj2.put("body", body);
//
//				JSONObject json = new JSONObject();
//				if (orgName.equalsIgnoreCase("Evgateway")) {
//					json.put("registration_ids", iOSRecipients);
//					json.put("notification", obj2);
//					logger.info("Final Response For IOS : " + json);
//					pushNotification(config.getServerKey(), iOSRecipients, json);
//				} else {
//					json.put("registration_ids", francisserverKey);
//					json.put("notification", obj2);
//					logger.info("Final Response For IOS : " + json);
//					pushNotification(config.getServerKey(), francisserverKey, json);
//				}
//
//			}
//		}
//	}
//
//	@SuppressWarnings({ "unchecked" })
//	public void sendNotificationForFailed(User usersObj, String orgName, String category, String body)
//			throws Exception {
//		JSONArray androidRecipients = new JSONArray();
//		JSONArray iOSRecipients = new JSONArray();
//
//		String query1 = "From DeviceDetails d  where d.userId ='" + usersObj.getId() + "'";
//		List<com.evgateway.server.pojo.DeviceDetails> deviceDetails = generalDao
//				.findAllHQLQuery(new com.evgateway.server.pojo.DeviceDetails(), query1);
//
//		if (deviceDetails != null) {
//			deviceDetails.forEach(device -> {
//				if (device.getDeviceType().equalsIgnoreCase("Android")) {
//					androidRecipients.add(device.getDeviceToken());
//				} else if (device.getDeviceType().equalsIgnoreCase("iOS")) {
//					logger.info("<<deviceDetails<>>>" + device.getDeviceToken());
//					iOSRecipients.add(device.getDeviceToken());
//				}
//			});
//		}
//		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//				"From ConfigurationSettings Where orgName like '%" + orgName + "%'");
//
//		if (androidRecipients.size() > 0) {
//			logger.info("<<androidRecipients<>>>");
//			JSONObject info = new JSONObject();
//			info.put("title", orgName);
//			info.put("body", body);
//			info.put("action", category);
//			info.put("userId", usersObj.getId());
//			info.put("refId", "");
//
//			JSONObject json = new JSONObject();
//
//			json.put("registration_ids", androidRecipients);
//			json.put("data", info);
//			logger.info("Final Response For Android : " + json);
//			pushNotification(config.getLegacykey(), androidRecipients, json);
//		}
//		if (iOSRecipients.size() > 0) {
//			JSONObject obj2 = new JSONObject();
//			obj2.put("title", orgName);
//			obj2.put("type", category);
//			obj2.put("body", body);
//
//			JSONObject json = new JSONObject();
//			if (orgName.equalsIgnoreCase("Evgateway")) {
//				json.put("registration_ids", iOSRecipients);
//				json.put("notification", obj2);
//				logger.info("Final Response For IOS : " + json);
//				pushNotification(config.getServerKey(), iOSRecipients, json);
//			}
//
//		}
//	}
//
//	@SuppressWarnings({ "unchecked" })
//	public void sendPushNotificationforsite(String orgName, String category, String body, Map<String, Object> refid,
//			boolean systemNotification, String devicetoken, String deviceType,String siteName) throws Exception {
//		JSONArray androidRecipients = new JSONArray();
//		JSONArray iOSRecipients = new JSONArray();
//		JSONArray francisserverKey = new JSONArray();
//
//		System.out.println("200000000000000000000000000000000000000000");
//
//		if (deviceType.equalsIgnoreCase("Android")) {
//			androidRecipients.add(devicetoken);
//		} else if (deviceType.equalsIgnoreCase("iOS")) {
//			logger.info("<<deviceDetails<>>>");
//			iOSRecipients.add(devicetoken);
//			francisserverKey.add(devicetoken);
//
//		}
//
//		System.out.println("androidRecipients" + androidRecipients);
//
//		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//				"From ConfigurationSettings Where orgName like '%" + orgName + "%'");
//
//		if (androidRecipients.size() > 0) {
//			logger.info("<<androidRecipients<>>>");
//			JSONObject info = new JSONObject();
//			info.put("title", orgName);
//			info.put("body", body);
//			info.put("action", category);
//			info.put("userId", 0);
//			info.put("refId", refid);
//
//			JSONObject json = new JSONObject();
//
//			json.put("registration_ids", androidRecipients);
//			json.put("data", info);
//			logger.info("Final Response For Android : " + json);
//			pushNotification(config.getLegacykey(), androidRecipients, json);
//		}
//		if (iOSRecipients.size() > 0) {
//			/*
//			 * logger.info("<<iOSRecipients<>>>"+iOSRecipients.size()); JSONObject info =
//			 * new JSONObject(); info.put("title", orgName); // info.put("body", body);
//			 * //info.put("type", category);
//			 * 
//			 * JSONObject json = new JSONObject(); json.put("registration_ids",
//			 * iOSRecipients); json.put("notification", info);
//			 * logger.info("Final Response For IOS : "+ json);
//			 */
//			JSONObject obj2 = new JSONObject();
//			obj2.put("title", orgName);
//			obj2.put("type", category);
//			obj2.put("body", body);
//			obj2.put("userId", 0);
//			obj2.put("refId", siteName);
//			obj2.put("categoryIdentifier", "notification");
//			obj2.put("mutable_content", true);
//			obj2.put("sound", "default");
//			obj2.put("content-available", 1);
//			JSONObject json = new JSONObject();
//			if (orgName.equalsIgnoreCase("Evgateway")) {
//				json.put("registration_ids", iOSRecipients);
//				json.put("notification", obj2);
//				logger.info("Final Response For IOS : " + json);
//				pushNotification(config.getServerKey(), iOSRecipients, json);
//			} else {
//				json.put("registration_ids", francisserverKey);
//				json.put("notification", obj2);
//				logger.info("Final Response For IOS : " + json);
//				pushNotification(config.getServerKey(), francisserverKey, json);
//			}
//
//		}
//
//	}
//
//	public void pushNotification(String serverKey, JSONArray recipients, JSONObject json) throws IOException {
//		logger.info("serverKey:" + serverKey);
//		Thread emailThread = new Thread() {
//			public void run() {
//				try {
//					URL url = new URL(apiURL);
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//					conn.setUseCaches(false);
//					conn.setDoInput(true);
//					conn.setDoOutput(true);
//					conn.setRequestMethod("POST");
//					conn.setRequestProperty("Authorization", "key=" + serverKey);
//					conn.setRequestProperty("Content-Type", "application/json");
//
//					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//					wr.write(json.toString());
//					wr.flush();
//
//					int responseCode = conn.getResponseCode();
//					logger.info("responseCode >>" + responseCode);
//					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//					String inputLine;
//					StringBuffer response = new StringBuffer();
//					while ((inputLine = in.readLine()) != null) {
//						response.append(inputLine);
//					}
//					logger.info("getResponseMess111age >>" + response);
//
//					in.close();
//				} catch (Exception e) {
//					logger.info("PushNotification >>" + e.getMessage());
//
//				}
//			}
//		};
//		emailThread.start();
//	}
//
//	@SuppressWarnings({ "unchecked" })
//	public void sendPushNotificationforDrivergroupRequest(String orgName, String category, String body,
//			Map<String, Object> refid, long userid,String status) throws Exception {
//		JSONArray androidRecipients = new JSONArray();
//		JSONArray iOSRecipients = new JSONArray();
//		JSONArray francisserverKey = new JSONArray();
//
//		String query1 = "From DeviceDetails d  where d.userId ='" + userid + "'";
//		List<com.evgateway.server.pojo.DeviceDetails> deviceDetails = generalDao
//				.findAllHQLQuery(new com.evgateway.server.pojo.DeviceDetails(), query1);
//
//		if (deviceDetails != null) {
//			deviceDetails.forEach(device -> {
//				if (device.getDeviceType().equalsIgnoreCase("Android")) {
//					androidRecipients.add(device.getDeviceToken());
//				} else if (device.getDeviceType().equalsIgnoreCase("iOS")) {
//					logger.info("<<deviceDetails<>>>");
//					iOSRecipients.add(device.getDeviceToken());
//					francisserverKey.add(device.getDeviceToken());
//					logger.info("device.getDeviceType() : " + device.getDeviceType());
//				}
//			});
//		}
//
//		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//				"From ConfigurationSettings Where orgName like '%" + orgName + "%'");
//
//		if (androidRecipients.size() > 0) {
//			logger.info("<<androidRecipients<>>>");
//			JSONObject info = new JSONObject();
//			info.put("title", orgName);
//			info.put("body", body);
//			info.put("action", "Driver Group");
//			info.put("userId", userid);
//			info.put("refId", refid);
//
//			JSONObject json = new JSONObject();
//
//			json.put("registration_ids", androidRecipients);
//			json.put("data", info);
//			logger.info("Final Response For Android : " + json);
//			pushNotification(config.getLegacykey(), androidRecipients, json);
//		}
//		if (iOSRecipients.size() > 0) {
//			/*
//			 * logger.info("<<iOSRecipients<>>>"+iOSRecipients.size()); JSONObject info =
//			 * new JSONObject(); info.put("title", orgName); // info.put("body", body);
//			 * //info.put("type", category);
//			 * 
//			 * JSONObject json = new JSONObject(); json.put("registration_ids",
//			 * iOSRecipients); json.put("notification", info);
//			 * logger.info("Final Response For IOS : "+ json);
//			 */
//			JSONObject obj2 = new JSONObject();
//			obj2.put("title", orgName);
//			obj2.put("type", "Driver Group");
//			obj2.put("body", body);
//			obj2.put("userId", userid);
//			obj2.put("status", status);
//			obj2.put("categoryIdentifier", "notification");
//			obj2.put("mutable_content", true);
//			obj2.put("sound", "default");
//			obj2.put("content-available", 1);
//			JSONObject json = new JSONObject();
//			if (orgName.equalsIgnoreCase("Evgateway")) {
//				json.put("registration_ids", iOSRecipients);
//				json.put("notification", obj2);
//				logger.info("Final Response For IOS : " + json);
//				pushNotification(config.getServerKey(), iOSRecipients, json);
//			} else {
//				json.put("registration_ids", francisserverKey);
//				json.put("notification", obj2);
//				logger.info("Final Response For IOS : " + json);
//				pushNotification(config.getServerKey(), francisserverKey, json);
//			}
//
//		}
//
//	}
//
//	/*
//	 * @SuppressWarnings("unchecked") public void
//	 * pushNotificationForIosAndroid(JSONArray recipients,String message,String
//	 * title,String deviceName) throws IOException {
//	 * 
//	 * URL url = new URL(apiURL); HttpURLConnection conn = (HttpURLConnection)
//	 * url.openConnection();
//	 * 
//	 * conn.setUseCaches(false); conn.setDoInput(true); conn.setDoOutput(true);
//	 * conn.setRequestMethod("POST"); if(deviceName.equalsIgnoreCase("IOS")) {
//	 * conn.setRequestProperty("Authorization", "key=" + iosServerKey); }else {
//	 * conn.setRequestProperty("Authorization", "key=" + androidServerKey); }
//	 * 
//	 * conn.setRequestProperty("Content-Type","application/json");
//	 * 
//	 * JSONObject info = new JSONObject(); try { //info.put("registration_ids",
//	 * recipients); info.put("title", title); info.put("body", message); } catch
//	 * (Exception e) { e.printStackTrace(); }
//	 * 
//	 * JSONObject json = new JSONObject(); json.put("registration_ids", recipients);
//	 * json.put("notification", info);
//	 * 
//	 * OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//	 * wr.write(json.toString()); wr.flush();
//	 * 
//	 * int responseCode = conn.getResponseCode();
//	 * 
//	 * BufferedReader in = new BufferedReader(new
//	 * InputStreamReader(conn.getInputStream())); String inputLine; StringBuffer
//	 * response = new StringBuffer(); while ((inputLine = in.readLine()) != null) {
//	 * response.append(inputLine); }
//	 * 
//	 * if (responseCode == 200)
//	 * logger.info("PushNotification.iosSend() -  [Notification sent successfully]"
//	 * ); in.close();
//	 * 
//	 * }
//	 */
//
//}
