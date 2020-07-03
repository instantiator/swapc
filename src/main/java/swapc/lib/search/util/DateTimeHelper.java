package swapc.lib.search.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

    public static Date fromYYYYMMDD(String yyyymmdd) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.parse(yyyymmdd);
    }

}
