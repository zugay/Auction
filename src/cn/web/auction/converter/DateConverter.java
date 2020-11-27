package cn.web.auction.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {

    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String time) {
    	try {
			return sd.parse(time);
		}catch (ParseException e){
    		e.printStackTrace();
		}

        return null;
    }
}
