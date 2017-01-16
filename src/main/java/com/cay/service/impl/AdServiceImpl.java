package com.cay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cay.Model.Advertisement.vo.Advertisement;
import com.cay.Model.Classification.vo.Classification;

@Service
public class AdServiceImpl {
	@Autowired
    private com.cay.repository.AdRepository adRepository;
	
	public void save(Advertisement advertisement) {
		adRepository.save(advertisement);
    }
	
	public Advertisement findById(String id) {
		return this.adRepository.findById(id);
	};
	
	public List<Advertisement> findByType(int type) {
		return this.adRepository.findByType(type);
	};
	
	public List<Advertisement> findByResponseType(int responseType) {
		return this.adRepository.findByResponseType(responseType);
	};
	
	public List<Advertisement> findByHyperlink(Boolean hyperlink) {
		return this.adRepository.findByHyperlink(hyperlink);
	};
	
	public List<Advertisement> findByPushed(Boolean pushed) {
		return this.adRepository.findByPushed(pushed);
	};
	
	public List<Advertisement> findByShowType(int showType) {
		return this.adRepository.findByShowType(showType);
	};
}
