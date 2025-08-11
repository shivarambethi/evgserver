package com.evgateway.server.dao;

import java.util.List;
import java.util.Map;

import com.evgateway.server.exception.UserNotFoundException;

public interface ReportDao {

	List<Map<String, Object>> getDashboardReports(int id, int period, String type, String useruid)
			throws NumberFormatException, UserNotFoundException;

	List<Map<String, Object>> getMapStations(String type, String id, String useruid) throws UserNotFoundException;

	List<Map<String, Object>> getSiteOnMap(String string, String id, String useruid) throws UserNotFoundException;
}
