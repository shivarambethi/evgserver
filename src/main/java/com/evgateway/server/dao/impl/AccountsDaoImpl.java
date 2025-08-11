
package com.evgateway.server.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.dao.AccountsDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Credentials;

@Repository
@Component
public class AccountsDaoImpl<I> extends DaoImpl<BaseEntity, I> implements AccountsDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(AccountsDaoImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAccounts(long id) throws UserNotFoundException {

		logger.info("AccountsDaoImpl.deleteAccounts() - [" + id + "]");
		Accounts accounts = getAccounts(id);
		if (accounts != null) {
			getCurrentSession().delete(accounts);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Accounts> getAccounts() {
		logger.info("AccountsDaoImpl.getAccounts() - []");

		String hql = "FROM Accounts a ORDER BY a.id";
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Accounts getAccounts(long id) throws UserNotFoundException {
		logger.info("AccountsDaoImpl.getAccounts() - [" + id + "]");
		Accounts accountsObject = (Accounts) getCurrentSession().get(Accounts.class, id);

		if (accountsObject == null) {
			throw new UserNotFoundException("Accounts id [" + id + "] not found");
		} else {
			return accountsObject;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Accounts addAccounts(Accounts accounts) {

		logger.info("AccountsDaoImpl.addAccounts() - [" + accounts + "]");

		getCurrentSession().save(accounts);
		return accounts;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateAccounts(Accounts accounts) throws UserNotFoundException {

		logger.info("AccountsDaoImpl.updateAccounts() - [" + accounts + "]");

		getCurrentSession().update(accounts);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Credentials getCredentials(String rfid) throws UserNotFoundException {
		// TODO Auto-generated method stub
		logger.info("AccountsDaoImpl.getCredentials() - [" + rfid + "]");

		String hql = "FROM Credentials where rfId:rfid";
		return (Credentials) getCurrentSession().createQuery(hql).setParameter("rfid", rfid).uniqueResult();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Credentials addCredentials(Credentials credentials) {
		// TODO Auto-generated method stub
		logger.info("AccountsDaoImpl.addCredentials() - [" + credentials + "]");

		getCurrentSession().save(credentials);

		return credentials;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCredentials(Credentials credentials) throws UserNotFoundException {
		// TODO Auto-generated method stub
		logger.debug("AccountsDaoImpl.updateCredentials() - [" + credentials + "]");

		getCurrentSession().update(credentials);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCredentials(String rfid) throws UserNotFoundException {
		// TODO Auto-generated method stub

		logger.debug("AccountsDaoImpl.deleteCredentials() - [" + rfid + "]");

		Credentials credentials = getCredentials(rfid);
		if (credentials != null) {
			getCurrentSession().delete(credentials);
		}
	}

	@Override
	public List<AccountTransactions> getAccountTransactionByAccountId(Long id) throws UserNotFoundException {
		// TODO Auto-generated method stub

		logger.debug("AccountsDaoImpl.getAccountTransactionByAccountId() - [" + id + "]");

		List<AccountTransactions> acountTrans = new ArrayList<AccountTransactions>(
				getAccounts(id).getAccountTransactions());

		return acountTrans;
	}

}
