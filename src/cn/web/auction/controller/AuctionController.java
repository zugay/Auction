package cn.web.auction.controller;


import cn.web.auction.pojo.Auction;
import cn.web.auction.pojo.AuctionCustom;
import cn.web.auction.pojo.Auctionrecord;
import cn.web.auction.pojo.Auctionuser;
import cn.web.auction.service.AuctionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class AuctionController {

    private static final  int PAGE_SIZE = 10;

    @Autowired
    private AuctionService auctionService;


    @RequestMapping("/queryAuctions")
    public ModelAndView queryAuctions(//做组合查询时添加
                                      // @ModelAttribute 做数据的回显
           @ModelAttribute("condition") Auction auction,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum ) {
        ModelAndView mv = new ModelAndView();
        //分页的拦截，在查询之前做，重构sql
        PageHelper.startPage(pageNum,PAGE_SIZE);
        List<Auction> list = auctionService.queryAuctions(auction);
        //构建分页bean
        PageInfo pageInfo = new PageInfo<>(list);
        mv.addObject("auctionList",list);
        mv.addObject("page",pageInfo);

        mv.setViewName("index");
        //postHandle
        return mv;  //afterCompletion
    }
    //发布商品
    @RequestMapping("/publishAuctions")
    public String publishAuctions(Auction auction , MultipartFile pic, HttpSession session){
        //pic(二进制文件数据) 1.存到指定的目录下，2.保存到数据库中
        //pic不会为null
            try {
                if(pic.getSize()!=0){
                    //把图片的路径存放到tomcat下
                    String path = session.getServletContext().getRealPath("upload");
                    String filename = pic.getOriginalFilename();
                    System.out.println(path);
                    System.out.println(filename);
                    File file = new File(path, filename);
                    pic.transferTo(file);
                    //設置图片的文件路径和文件类型
                    auction.setAuctionpic(filename);
                    auction.setAuctionpictype(pic.getContentType());
                }
                auctionService.addAuction(auction);
            } catch (IOException e) {
                e.printStackTrace();
                //错误返回页面
                return "addAuction";
            }

        return "redirect:/queryAuctions";
    }
     //修改商品
    @RequestMapping("/submitUpdateAuction")
    public String submitUpdateAuction(Auction auction , MultipartFile pic, HttpSession session){
        //pic(二进制文件数据) 1.存到tomcat的目录下，2.保存到数据库中
        //pic不会为null
        try {
            if(pic.getSize()!=0){
                //upload在tomcat的绝对路径下
                String path = session.getServletContext().getRealPath("upload");

                 //删除原来的图片文件
                File file1 = new File(path, auction.getAuctionpic());
                if (file1.exists()){
                    file1.delete();
                }

                String filename = pic.getOriginalFilename();
                System.out.println(path);
                System.out.println(filename);
                File file = new File(path, filename);
                pic.transferTo(file);
                //設置图片的文件路径和文件类型
                auction.setAuctionpic(filename);
                auction.setAuctionpictype(pic.getContentType());
            }
            auctionService.updateAuction(auction);
        } catch (IOException e) {
            e.printStackTrace();
            //错误返回页面
            return "addAuction";
        }

        return "redirect:/queryAuctions";
    }
    @RequestMapping("/updateAuction/{auctionid}")
    public ModelAndView toAddAuction(@PathVariable int auctionid) {
        Auction auction = auctionService.findById(auctionid);
        ModelAndView mv = new ModelAndView();
        mv.addObject("auction",auction);
        mv.setViewName("updateAuction");

        return mv;
    }
  @RequestMapping("/deleteAction/{auctionid}")
    public String deleteAction(@PathVariable int auctionid){
        auctionService.delete(auctionid);
        return "redirect:/queryAuctions";

  }
 //查看結果
  @RequestMapping("/toAuctionResult")
    public ModelAndView toAuctionResult(){
      List<AuctionCustom> auctionEndtimeList = auctionService.findAuctionEndtimeList();
      List<Auction> auctionNoEndtimeList = auctionService.findAuctionNoEndtimeList();
      ModelAndView mv = new ModelAndView();
      mv.addObject("auctionCustomList",auctionEndtimeList);
      mv.addObject("auctionList",auctionNoEndtimeList);
      mv.setViewName("auctionResult");
      return mv;
  }
   @RequestMapping("toAuctionDetail/{auctionid}")
    public ModelAndView toAuctionDetail(@PathVariable int auctionid){
       ModelAndView mv = new ModelAndView();
       Auction auction = auctionService.findAuctionAndAuctionRecords(auctionid);
       mv.addObject("auctionDetail",auction);
       mv.setViewName("auctionDetail");
       return mv;
   }


    //使用springmvc全局异常
    @RequestMapping("/saveAuctionRecord")
    public String saveAuctionRecord(Auctionrecord record,
                                    HttpSession session,
                                    Model model) throws Exception {
        //竞拍时间：当前时间
        record.setAuctiontime(new Date());
        //设置竞拍人
        Auctionuser user = (Auctionuser) session.getAttribute("user");
        record.setUserid(user.getUserid());

        auctionService.addAuctionRecord(record);
        //详情页面
        return "redirect:/toAuctionDetail/"+record.getAuctionid();
    }
}
