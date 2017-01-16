package com.cay.Model.Advertisement.entity;

import com.cay.Model.BaseEntity;
import com.cay.Model.Advertisement.vo.Advertisement;
/**
 * 广告
 * @author 陈安一
 *
 */
public class AdEntity extends BaseEntity{
	private Advertisement ad;

	public Advertisement getAd() {
		return ad;
	}

	public void setAd(Advertisement ad) {
		this.ad = ad;
	}
}
