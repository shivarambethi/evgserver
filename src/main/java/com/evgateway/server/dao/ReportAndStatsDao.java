package com.evgateway.server.dao;

import java.util.List;
import java.util.Map;

import com.evgateway.server.pojo.BaseEntity;

public interface ReportAndStatsDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Map<String, Object>> getTotalDataForReportAnalytics(String query);

}
