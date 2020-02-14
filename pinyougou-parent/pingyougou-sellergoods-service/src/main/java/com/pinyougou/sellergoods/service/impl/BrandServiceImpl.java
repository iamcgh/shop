package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private TbBrandMapper brandMapper;

	//获取所有品牌信息
	@Override
	public List<TbBrand> findAll() {
		
		return brandMapper.selectByExample(null);
	}

	//获取分页结果
	@Override
	public PageResult findPages(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> pages = (Page<TbBrand>) brandMapper.selectByExample(null);
		return new PageResult(pages.getTotal(), pages.getResult());
	}

	//新增品牌
	@Override
	public void add(TbBrand tbBrand) {
		brandMapper.insert(tbBrand);
		
	}

	//更新品牌
	@Override
	public void update(TbBrand tbBrand) {
		brandMapper.updateByPrimaryKey(tbBrand);
	}

	//根据id获取实体
	@Override
	public TbBrand findOne(long id) {
		TbBrand tbBrand = brandMapper.selectByPrimaryKey(id);
		return tbBrand;
	}

	@Override
	public void delete(Long[] ids) {
		for(Long id:ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}

	//品牌条件查询
	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		TbBrandExample example=new TbBrandExample();
		Criteria criteria = example.createCriteria();		
		if(brand!=null){
			if(brand.getName()!=null && brand.getName().length()>0){
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
				criteria.andFirstCharEqualTo(brand.getFirstChar());
			}		
		}		
		Page<TbBrand> page= (Page<TbBrand>)brandMapper.selectByExample(example);	
		return new PageResult(page.getTotal(), page.getResult());

	}

	//获取下拉列表
	@Override
	public List<Map> selectOptionList() {
		return brandMapper.selectOptionList();
	}

}
