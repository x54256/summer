package cn.x5456.testmvc.controller;

import cn.x5456.summer.stereotype.Controller;
import cn.x5456.summer.web.bind.annotation.RequestMapping;

/**
 * @author yujx
 * @date 2020/04/22 16:35
 */
@Controller
public class UserController {

    @RequestMapping("/user/all")
    public User findAll() {
        return new User("张三", 18);
    }
}
