package cn.web.auction.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class CustomExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest req, 
										HttpServletResponse res,
										Object handler,
										Exception ex) {
		CustomException customEx = null;
		if (ex instanceof CustomException) {
			customEx = (CustomException) ex;
		} else {
			customEx = new CustomException("未知异常");
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("errorMsg", customEx.getMessage());
		mv.setViewName("error");
		return mv;
	}
	
}
