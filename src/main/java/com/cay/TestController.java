package com.cay;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 陈安一 on 2017/1/4.
 */
@RestController
public class TestController {
    @RequestMapping(value="/fuck", method = RequestMethod.GET)
    public String fuck () {

        return "fuck you";
    }
}
