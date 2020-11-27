package cn.web.auction.service.impl;

import cn.web.auction.mapper.AuctionCustomMapper;
import cn.web.auction.mapper.AuctionMapper;
import cn.web.auction.mapper.AuctionrecordMapper;
import cn.web.auction.pojo.*;
import cn.web.auction.service.AuctionService;
import cn.web.auction.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuctionServiceimpl implements AuctionService {


    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private AuctionrecordMapper auctionrecordMapper;

    @Autowired
    private AuctionCustomMapper auctionCustomMapper;
    @Override
    public List<Auction> queryAuctions(Auction auction) {


        //显示首页只需要
//        return auctionMapper.selectByExample(null);

        //组合查询
        AuctionExample example = new AuctionExample();
        AuctionExample.Criteria criteria = example.createCriteria();
        if (auction != null) {
            //1.商品名称
            if (auction.getAuctionname() != null && !"".equals(auction.getAuctionname())) {
                criteria.andAuctionnameLike("%" + auction.getAuctionname() + "%");
            }
            //2.商品描述
            if (auction.getAuctiondesc()!=null && !"".equals(auction.getAuctiondesc())){
                criteria.andAuctiondescLike("%"+auction.getAuctiondesc()+"%");
            }
            //3.起拍时间>
           if (auction.getAuctionstarttime()!=null){
               criteria.andAuctionstarttimeGreaterThan(auction.getAuctionstarttime());
           }
            //4.结束时间 <
            if (auction.getAuctionendtime() != null) {
                criteria.andAuctionendtimeLessThan(auction.getAuctionendtime());
            }
            //5.起拍价 >
            if (auction.getAuctionstartprice() != null) {
                criteria.andAuctionstartpriceGreaterThan(auction.getAuctionstartprice());
            }
        }
        //去mapper看属性
        //按日期时间降序排列
        example.setOrderByClause("auctionstarttime desc");
        List<Auction> list = auctionMapper.selectByExample(example);

        return list;
    }

    @Override
    public void addAuction(Auction auction) {
        auctionMapper.insert(auction);
    }

    @Override
    public void updateAuction(Auction auction) {
        auctionMapper.updateByPrimaryKey(auction);
    }

    @Override
    public Auction findById(int auctionid) {
        return auctionMapper.selectByPrimaryKey(auctionid);
    }

    @Override
    public void delete(int auctionid) {
        //先删除子表数据：auctionrecord
        AuctionrecordExample example = new AuctionrecordExample();
        AuctionrecordExample.Criteria criteria = example.createCriteria();
        criteria.andAuctionidEqualTo(auctionid);
        //删除子表数据
        auctionrecordMapper.deleteByExample(example);
        //再删除主表数据：Auction
        auctionMapper.deleteByPrimaryKey(auctionid);


    }

    @Override
    public List<AuctionCustom> findAuctionEndtimeList() {
        return auctionCustomMapper.findAuctionEndtimeList();
    }

    @Override
    public List<Auction> findAuctionNoEndtimeList() {
        return  auctionCustomMapper.findAuctionNoEndtimeList();
    }

    @Override
    public Auction findAuctionAndAuctionRecords(int auctionid) {
       return   auctionCustomMapper.findAuctionAndAuctionRecords(auctionid);

    }

    public void addAuctionRecord(Auctionrecord record) throws Exception {
        //先查询商品的详情(使用映射的方法来封装: List<AuctionRecord>)
        Auction auction = auctionCustomMapper.findAuctionAndAuctionRecords(record.getAuctionid());
        //1. 判断竞拍时间
        if (auction.getAuctionendtime().after(new Date()) == false) {
            throw new CustomException("该商品拍卖时间已经结束");
        }
        //判断是否有竞拍记录
        if (auction.getAuctionRecordList()!=null && auction.getAuctionRecordList().size()>0) {
            //3. 如果当前商品已经竞拍， 竞拍价格必须高于当前的最高竞价
            //集合的第一条记录就是最高竞价记录（排序后）
            Auctionrecord maxRecord = auction.getAuctionRecordList().get(0);
            if (record.getAuctionprice() <= maxRecord.getAuctionprice()) {
                throw new CustomException("竞拍价格必须高于当前的最高竞价");
            }

        } else { //第一次竞价: 2. 如果当前商品没有竞拍记录，竞拍价格必须高于起拍价
            if (record.getAuctionprice() <= auction.getAuctionstartprice()) {
                throw new CustomException("竞拍价格必须高于起拍价");
            }
        }
        auctionrecordMapper.insert(record);
    }
}
