package cn.web.auction.pojo;

/**
 * 扩展商品的pojo类
 * @author Administrator
 *
 */
public class AuctionCustom extends Auction {

	private String auctionprice;
	private String username;
	
	
	public String getAuctionprice() {
		return auctionprice;
	}
	public void setAuctionprice(String auctionprice) {
		this.auctionprice = auctionprice;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
