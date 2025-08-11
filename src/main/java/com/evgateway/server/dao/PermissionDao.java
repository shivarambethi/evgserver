package com.evgateway.server.dao;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Permission;

public interface PermissionDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public Permission getPermission(String permissionName) throws UserNotFoundException;

}
