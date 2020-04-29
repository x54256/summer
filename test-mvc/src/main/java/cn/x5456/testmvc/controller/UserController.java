package cn.x5456.testmvc.controller;

import cn.x5456.summer.stereotype.Controller;
import cn.x5456.summer.web.Model;
import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.bind.annotation.*;

/**
 * @author yujx
 * @date 2020/04/22 16:35
 */
@Controller
public class UserController {

    @ModelAttribute("name")
    public String modelAttr(@RequestParam("id") Integer id) {
        System.out.println("@ModelAttribute" + id);
        return "张三";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        System.out.println("@InitBinder" + binder);
    }

    @ResponseBody
    @RequestMapping("/user/all")
    public User findAll(@RequestParam("id") Integer id) {
        return new User("张三", 18);
    }

    @RequestMapping("/user/all2")
    public String findAll(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("id", id);
        return "/WEB-INF/user.jsp";
    }
}
