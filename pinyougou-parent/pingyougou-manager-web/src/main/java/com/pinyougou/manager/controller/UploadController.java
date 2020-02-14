package com.pinyougou.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import utils.FastDFSClient;

@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
		//1.取文件的扩展名
		String fileName = file.getOriginalFilename();
		//判断文件是否为空
		if(fileName == null) {
			return new Result(false,"操作失败");
		}
		String extName = fileName.substring(fileName.lastIndexOf(".")+1);
		
		try {
			//2.获取FastDFSClient客户端对象
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			//3.上传文件
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			//4、拼接返回的 url 和 ip 地址，拼装成完整的 url
			String url = FILE_SERVER_URL + path;			
			return new Result(true,url);

		} catch (Exception e) {
			e.printStackTrace();
			
			return new Result(false,"上传失败");
		} 
		
	}

}
