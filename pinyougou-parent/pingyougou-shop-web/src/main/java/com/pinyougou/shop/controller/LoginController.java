package com.pinyougou.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	/*@Reference
	private SellerService sellerService;*/
	
	@RequestMapping("/name")
	public Map<String,String> name() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String,String> map = new HashMap<>();
		map.put("loginName", name);
		return map;
	}
	

}
