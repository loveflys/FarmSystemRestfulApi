package com.cay.Dao;

import com.cay.Model.Market.vo.Market;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Box;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.cay.Model.Location.vo.Location;
/**
 * Created by 陈安一 on 2017/1/17.
 */
@Repository
public class MarketDao {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Market> findCircleNear(Point point, double maxDistance, PageRequest pageRequest) {    	
        return mongoTemplate.find(new Query(Criteria.where("location").near(point).maxDistance(maxDistance)).with(pageRequest),
                Market.class);
    }

    public List<Location> findBoxNear(Point lowerLeft, Point upperRight, PageRequest pageRequest) {
        return mongoTemplate.find(
                new Query(Criteria.where("location").within(new Box(lowerLeft, upperRight))).with(pageRequest),
                Location.class);
    }
}
