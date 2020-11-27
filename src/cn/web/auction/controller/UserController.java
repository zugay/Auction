package cn.web.auction.controller;


import cn.web.auction.pojo.Auctionuser;
import cn.web.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {


    @Autowired
    private UserService userService;


    @RequestMapping("/doLogin")
    private String doLogin(String username, String userpassword, String inputCode, HttpSession session, Model model) {
        //1.判断验证码
        //1.1从session中获取验证吗
        String numrand = (String) session.getAttribute("numrand");
        if (!inputCode.equals(numrand)) {
            model.addAttribute("errorMsg", "验证码错误！");
            return "login";
        }

        //2.判断业务
        Auctionuser user = userService.login(username, userpassword);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/queryAuctions";
        } else {
            model.addAttribute("errorMsg", user);
            return "login";

        }

    }

    @RequestMapping("/doLogout")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "login";
    }

 //BindingResult bindingResult  定义在紧跟要校验的pojo对象之后，存放错误消息的容器
 // 在要验证的参数上添加@Validated注解即可
    @RequestMapping("/doRegister")//定义Model的作用域
    public String doRegister(Model model, @ModelAttribute("registerUser") @Validated Auctionuser auctionuser, BindingResult bindingResult) {
       //1.判断是否校验通过
        if (bindingResult.hasErrors()){
            List<FieldError> errors =  bindingResult.getFieldErrors();
            for (FieldError error: errors){
                //返回错误信息
            model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "register";
        }
  //2.保存到mysql数据库
        userService.addUser(auctionuser);
        return "login";

    }
}
