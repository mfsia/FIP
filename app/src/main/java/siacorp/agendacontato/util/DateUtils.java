package siacorp.agendacontato.util;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getDate(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        Date data = calendar.getTime();

        return data;

    }

    public static String datetoString(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        Date data = calendar.getTime();

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(data);
        return dt;

    }

    public static String datetoString(Date date)
    {

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(date);
        return dt;

    }
}
