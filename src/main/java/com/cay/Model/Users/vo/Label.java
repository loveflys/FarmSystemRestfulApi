package com.cay.Model.Users.vo;

import org.springframework.data.annotation.Id;

public class Label {
	@Id
	private Long id;
	
	private String name;
	/**  
	 * 是否为热门标签 
	 * @param ishot 是否为热门标签  
	 */    
	private Boolean ishot;
	/**  
	 * @param usercount 多少人用过该标签
	 */    
	private Long usecount;
}
