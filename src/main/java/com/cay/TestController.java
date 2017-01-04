package com.cay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 陈安一 on 2017/1/4.
 */
@RestController
public class TestController {

    @Autowired
    private MongoConfig mongoConfig;

    @RequestMapping(value="/fuck", method = RequestMethod.GET)
    public String fuck () {
        return "fuck you";
    }

    @RequestMapping(value="/getMongoConfig", method = RequestMethod.GET)
    public MongoConfig GetMongoConfig () {
        return mongoConfig;
    }
}
