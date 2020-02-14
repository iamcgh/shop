package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌类接口
 * @author ASUS
 *
 */
public interface BrandService {

	//获取所有品牌信息
	public List<TbBrand> findAll();
	
	//获取分页结果
	public PageResult findPages(int pageNum,int pageSize);
	
	//新增品牌
	public void add(TbBrand tbBrand);
	
	//修改品牌
	public void update(TbBrand tbBrand);
	
	//根据id获取实体
	public TbBrand findOne(long id);
	
	//批量删除
	public void delete(Long[] ids);
	
	//品牌条件查询
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
	
	
	/**
	 * 品牌下拉框数据
	 */
	List<Map> selectOptionList();

}
