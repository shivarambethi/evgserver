package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Credentials;

public interface AccountsDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Accounts> getAccounts();

	public Accounts getAccounts(long id) throws UserNotFoundException;

	public Accounts addAccounts(Accounts accounts);

	public void updateAccounts(Accounts accounts) throws UserNotFoundException;

	public void deleteAccounts(long id) throws UserNotFoundException;

	public Credentials getCredentials(String rfid) throws UserNotFoundException;

	public Credentials addCredentials(Credentials credentials);

	public void updateCredentials(Credentials credentials) throws UserNotFoundException;

	public void deleteCredentials(String rfid) throws UserNotFoundException;

	public List<AccountTransactions> getAccountTransactionByAccountId(Long id) throws UserNotFoundException;

}
