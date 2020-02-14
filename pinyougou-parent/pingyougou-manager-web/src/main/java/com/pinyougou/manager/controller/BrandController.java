package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	/**
	 * 查询品牌列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll() {
		return brandService.findAll();
	}
	
	/**
	 * 查询分页结果
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page,int rows){			
		return brandService.findPages(page, rows);
	}
	
	/**
	 * 添加品牌
	 * @param tbBrand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand tbBrand) {
		try {
			brandService.add(tbBrand);
			return new Result(true,"添加成功");
		} catch(Exception e) {
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
		
	}
	
	/**
	 * 修改品牌信息
	 * @param tbBrand
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand tbBrand) {		
		try {
			brandService.update(tbBrand);
			return new Result(true,"修改成功");
		} catch(Exception e) {
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}
	
	/**
	 * 根据id获取实体类对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbBrand findOne(long id) {
		return brandService.findOne(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			brandService.delete(ids);
			return new Result(true,"删除成功");
		} catch(Exception e) {
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
	}
	
	/**
	 * 条件查询
	 * @param tbBrand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand tbBrand,int page,int rows) {
		return brandService.findPage(tbBrand, page, rows);
	}
	
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}


}
