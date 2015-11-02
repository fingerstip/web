package com.linng.www.service;

import org.elasticsearch.ElasticsearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linng.www.dao.EsDao;
import com.linng.www.util.Page;
import com.linng.www.vo.Account;

@Service
public class EsService {
	@Autowired
	private EsDao esDao;
	
	public Page<Account> list(Page<Account> page){
		return esDao.list(page);
		
	}

	public boolean save(Account account) throws ElasticsearchException, Exception {
		return esDao.save(account);
	}

	public Account get(String accountNumber) {
		return esDao.get(accountNumber);
	}
	
	public void delete (String[] ids ) {
		esDao.delete(ids);
	}

	public Page<Account> search(Page<Account> page) {
		return esDao.search(page);
	}

}
