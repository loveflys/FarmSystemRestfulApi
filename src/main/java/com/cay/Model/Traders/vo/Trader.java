package com.cay.Model.Traders.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 商户
 * @author 陈安一
 *
 */
@Document(collection = "trader")
public class Trader {
	@Id
	private String id;
	//private String name;
}
