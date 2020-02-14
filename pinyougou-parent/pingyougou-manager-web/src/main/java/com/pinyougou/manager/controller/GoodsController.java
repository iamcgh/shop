package com.pinyougou.manager.controller;

import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	//@Reference
	//private ItemSearchService itemSearchService;
	
	@Autowired
	private Destination queueSolrDestination;//用于导入索引库的队列
	
	@Autowired
	private Destination queueSolrDeleteDestination;//用于从索引库删除记录的队列
	
	@Autowired
	private Destination topicPageDestination;//用于发送生成静态页面的信息
	
	@Autowired
	private Destination topicPageDeleteDestination;//用于发送删除页面信息
	
	
	
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * 修改
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods) {
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id) {
		return goodsService.findOne(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long[] ids) {
		try {
			goodsService.delete(ids);
			//修改了
			//itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			//发送消息给activemq
			//1.删除索引库数据
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			//2.删除静态页面
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 * 
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		return goodsService.findPage(goods, page, rows);
	}

	/**
	 * 更新状态
	 * 
	 * @param ids
	 * @param status
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status) {
		try {
			goodsService.updateStatus(ids, status);
			if (status.equals("1")) {// 审核通过
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);
				// 调用搜索接口实现数据批量导入
				if (itemList.size() > 0) {
					//导入到solr
					
					//将消息发送到activemq队列中
					final String jsonString = JSON.toJSONString(itemList);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(jsonString);
						}
					});
					
					
					for(final Long goodsId:ids) {
						jmsTemplate.send(topicPageDestination, new MessageCreator() {
							
							@Override
							public Message createMessage(Session session) throws JMSException {
								System.out.println("发送消息"+goodsId);
								return session.createTextMessage(goodsId+"");
							}
						});
					}
					
				} else {
					System.out.println("没有明细数据");
				}
				
				//静态页生成
				/*for(Long goodsId:ids){
					itemPageService.genItemHtml(goodsId);
				}	*/
				

			}

			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}
	}
	
	//@Reference(timeout=5000)
	//private ItemPageService itemPageService;
	
	/**
	 * 生成静态页（测试）
	 * @param goodsId
	 */
	//@RequestMapping("/genHtml")
	//public void genHtml(Long goodsId){
		
		//itemPageService.genItemHtml(goodsId);	
	//}


}
