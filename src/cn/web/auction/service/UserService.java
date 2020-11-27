package cn.web.auction.service;

import cn.web.auction.pojo.Auctionuser;


public interface UserService {

    Auctionuser login(String username, String password);

    public void addUser(Auctionuser auctionuser);



}
