package cn.web.auction.mapper;

import java.util.List;

import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;

public interface AuctionCustomMapper {

	 //竞拍
	public Auction findAuctionAndAuctionRecords(int auctionid);
	//查看竞拍页面
	public List<AuctionCustom> findAuctionEndtimeList();
	//查看竞拍页面
	public List<Auction> findAuctionNoEndtimeList();
}
