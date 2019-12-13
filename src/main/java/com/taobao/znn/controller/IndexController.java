package com.taobao.znn.controller;

import com.taobao.znn.bean.OrderDO;
import com.taobao.znn.service.Orderservice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Author guoxiaoyu
 * @Date 2019/12/1314:18
 **/
@Controller
public class IndexController {
    @Resource
    private Orderservice orderservice;
    @GetMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<OrderDO> list = orderservice.getList(null);
            modelAndView.getModel().put("list",list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelAndView.setViewName("index.html");
        return modelAndView;
    }
    @GetMapping("/toAdd")
    public ModelAndView toAdd(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add.html");
        return modelAndView;
    }
    @ResponseBody
    @PostMapping("/addOrUpdate")
    public Map<String,Object> add(@RequestBody OrderDO orderDO){
        Map<String,Object> map = new HashMap<>();
        try {
            Long add = orderservice.add(orderDO);
            map.put("code",add);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }
}
