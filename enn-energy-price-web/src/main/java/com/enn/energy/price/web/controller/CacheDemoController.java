package com.enn.energy.price.web.controller;

import com.enn.energy.price.common.utils.PriceDateUtils;
import com.enn.energy.price.core.service.impl.CacheService;
import com.enn.energy.price.web.dto.CacheDemoDelKeyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.rdfa.framework.biz.ro.RdfaResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


@RestController
@RequestMapping("/redisCache")
public class CacheDemoController {


    @Autowired
    public CacheService cacheService;
    @Autowired
    public StringRedisTemplate redisTemplate;
    @PostMapping("/saveData/{funcPrefix}/{key}")
    public <T> T saveDataInCache(@PathVariable String key, @PathVariable String funcPrefix, @RequestBody T value) {
        this.cacheService.setData(key, funcPrefix, value);
        return (T) "success";
    }

    @PostMapping("/saveData/{key}")
    public String saveDataInCache(@PathVariable String key) {
        redisTemplate.opsForValue().set(key,"谢谢配合");
        return "success";
    }

    @GetMapping("/getData/{funcPrefix}/{key}")
    public <T> T getDataFromCache(@PathVariable  String key, @PathVariable String funcPrefix) {
        return this.cacheService.getData(key, funcPrefix);
    }

    @PostMapping("/redis/del/key")
    public RdfaResult delDataFromCache(@RequestBody CacheDemoDelKeyDto dto) {
        Boolean delete = redisTemplate.delete(dto.getKey());
        return delete.booleanValue()? RdfaResult.success(""):RdfaResult.fail("1111","失败");
    }

    @PostMapping("/demo/thread")
    public RdfaResult thread() throws InterruptedException {
        for (int i = 0; i<1000000;i++){
            new Thread(i+""){
                @Override
                public void run() {
                    System.out.println("线程名称 ： " +Thread.currentThread().getName());
                    String dateToStr = PriceDateUtils.dayDateToStr(new Date());
                    System.out.println("dateToStr ： " + dateToStr);
                }
            }.start();
        }
        while (Thread.activeCount()<3){
            break;
        }
        return RdfaResult.success("");
    }

    @GetMapping("/hello") // 所有的xxxMapping都是RequestMapping
    public String   sayHello(String name, //可以从请求参数中得到
                             @RequestParam(value = "user")String user, //可以从请求参数中得到
                             HttpSession session, HttpServletRequest request, //原生的session对象
                             @RequestHeader(value = "User-Agent",required = false) String  ua,
                             Model model,
                             Integer i,
                             RedirectAttributes ra){ //@RequestParam Map<String,Object> params：所有请求参数全封装进来
        int x =10/i;
//        if("abc".equals(user)){
//            //非法的用户信息
//            throw new InvalidUserException();
//        }
        // @RequestHeader("User-Agent") String  ua 获取指定请求头的值
        String header = request.getHeader("User-Agent");
        //方法的签名，到底能写那些？
        //详细参照 https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
        //https://www.bilibili.com/video/BV19K4y1L7MT?p=32
        //SpringMVC的目标方法能写哪些返回值
        //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types
        return "index.jsp";  //@PostMapping("/submit")  表单失败了  前一步，把表单中的数据放到ra中，  return  "redirect:form.jsp" //表单还能取到数据
    }
}
