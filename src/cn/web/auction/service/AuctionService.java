package cn.web.auction.service;

import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;
import cn.web.auction.pojo.Auctionrecord;

import java.util.List;

public interface AuctionService {

    public List<Auction> queryAuctions(Auction auction);

    public void  addAuction(Auction auction);

    public void updateAuction(Auction auction);

    public Auction findById(int auctionid);

    public void delete(int auctionid);

    public List<AuctionCustom> findAuctionEndtimeList();

    public List<Auction> findAuctionNoEndtimeList();

    public Auction findAuctionAndAuctionRecords(int auctionid);

    public void addAuctionRecord(Auctionrecord record) throws Exception;

}
