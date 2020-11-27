package cn.web.auction.service.impl;


import cn.web.auction.mapper.AuctionuserMapper;
import cn.web.auction.pojo.Auctionuser;
import cn.web.auction.pojo.AuctionuserExample;
import cn.web.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//表示给当前类命名一个别名，方便注入到其他需要用到的类中
// @Component把普通pojo实例化到spring容器中
public class UserServiceimpl implements UserService {


    @Autowired //，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。
    private AuctionuserMapper auctionuserMapper; //代理接口 ,对功能进行增强


    @Override  //假设条件：账号唯一性
    public Auctionuser login(String username, String password) {
        AuctionuserExample example = new AuctionuserExample();
        //创建内部类
        AuctionuserExample.Criteria criteria = example.createCriteria();
        //查询账号
        criteria.andUsernameEqualTo(username);
        //查询密码
        criteria.andUserpasswordEqualTo(password);
        //将数据存入list中
        List<Auctionuser> list = auctionuserMapper.selectByExample(example);

        //判断参数是否传入
        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public void addUser(Auctionuser auctionuser) {
        //不被拦截
        auctionuser.setUserisadmin(0);
        auctionuserMapper.insert(auctionuser);
    }
}
