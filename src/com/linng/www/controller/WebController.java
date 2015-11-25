package com.linng.www.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 
 * <pre>
 *     林花谢了春红，
 *     太匆匆，
 *     无奈朝来寒雨晚来风。
 *  
 *     胭脂泪，
 *     相留醉，
 *     几时重，
 *     自是人生长恨水长东。
 * </pre>
 * 
 * @author LiNing
 * 
 */
@Controller
@RequestMapping("/view")
public class WebController {

	@RequestMapping("/hehe")
	public void index() { }

}
