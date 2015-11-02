package com.linng.www.dao;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.linng.www.util.ESUtil;
import com.linng.www.util.Page;
import com.linng.www.vo.Account;

@Repository
public class EsDao {
	
	@Autowired
	private Client client;

	public Page<Account> list(Page<Account> page) {
		return ESUtil.searchList(page,client);
	}

	public boolean save(Account account) throws ElasticsearchException, Exception {
		return ESUtil.save(account,client);
	}

	public Account get(String accountNumber) {
		return ESUtil.get(accountNumber,client);
	}
	
	public void delete(String[] ids) {
		ESUtil.delete(ids,client);
	}
	
	public Page<Account> search(Page<Account> page) {
		return ESUtil.search(page,client);
	}
	
}
