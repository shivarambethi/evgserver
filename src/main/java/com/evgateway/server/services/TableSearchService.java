package com.evgateway.server.services;

import com.evgateway.server.messages.PagedResult;

public interface TableSearchService {

	<T> PagedResult<T> filter(String text, int page, int size) throws InterruptedException;
}
