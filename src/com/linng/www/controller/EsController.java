package com.linng.www.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.linng.www.service.EsService;
import com.linng.www.util.Page;
import com.linng.www.vo.Account;

@Controller
@RequestMapping("es")
public class EsController {

	@Autowired
	private EsService esService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(
			@RequestParam(value = "pageNo", required = false) String pageStr,
			@RequestParam(value = "pageSize", required = false) String pageSizeStr) {
		
		Page<Account> page = new Page<Account>();
		int pageNo = 1;
		int pageSize = page.getPageSize();
		if (StringUtils.isNumeric(pageStr)) {
			pageNo = Integer.parseInt(pageStr);
		}
		if (StringUtils.isNumeric(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		page.setPageSize(pageSize);
		page.setCurrentPage(pageNo);
		page = esService.list(page);

		return new ModelAndView("es/index", "page", page);
	}

	@RequestMapping("/add")
	public ModelAndView addPage() {
		return new ModelAndView("es/add");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute Account account)
			throws ElasticsearchException, Exception {
		boolean result = esService.save(account);
		System.out.println("操作 ： " + result);
		return new ModelAndView("redirect:/es");
	}

	@RequestMapping(value = "/update/{accountNumber}")
	public ModelAndView update(
			@PathVariable("accountNumber") String accountNumber) {
		Account account = esService.get(accountNumber);
		return new ModelAndView("es/add", "account", account);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam(value = "ids[]") String[] ids) {
		esService.delete(ids);
		return new ModelAndView("redirect:/es");
	}

	@RequestMapping(value = "/search" , method = RequestMethod.POST)
	public ModelAndView search (HttpServletRequest request) {
		
		Page<Account> page = new Page<>();
		page.getParams().put("address", request.getParameter("address"));
		page.getParams().put("firstname", request.getParameter("firstname"));
		page.getParams().put("gteAge", request.getParameter("gteAge"));
		page.getParams().put("lteAge", request.getParameter("lteAge"));
		page = esService.search(page);
		
		return new ModelAndView("es/index", "page", page);
		
	}
}
