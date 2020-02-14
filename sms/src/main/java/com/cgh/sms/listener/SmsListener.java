package com.cgh.sms.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.CommonResponse;
import com.cgh.sms.utils.SmsUtil;
/**
 * 消息监听类
 * @author ASUS
 *
 */
@Component
public class SmsListener {
	
	@Autowired
	private SmsUtil smsUtil;
	
	@JmsListener(destination="sms")
	public void sendMessage(Map<String,String> map) {
		CommonResponse response = smsUtil.sendSms(map.get("mobile"),
						map.get("signName"),
						map.get("templateCode"),
						map.get("param"));
		System.out.println("data"+response.getData());
	}

}
