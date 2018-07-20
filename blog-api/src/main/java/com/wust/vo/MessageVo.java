package com.wust.vo;

import com.wust.utils.StringUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class MessageVo {

	private static final Logger logger = LoggerFactory.getLogger(MessageVo.class);

	Integer code;
	String info;
	String errorCode;
	Object data;

	public MessageVo(){
		this.errorCode = StringUtil.getStringRandom(12);
		logger.info("---------------errorCode:" + errorCode);
		System.out.println(errorCode);
	}
}
