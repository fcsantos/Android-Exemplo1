package br.com.fcsconsulting.agendacontatos.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FCSantos on 25/01/2016.
 */
public class DateUtil {

    public static Date getDate(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date data = calendar.getTime();
        return data;
    }

    public static String dateToString(int year, int monthOfYear, int dayOfMonth)
    {
        Date data = getDate(year, monthOfYear, dayOfMonth);
        DateFormat format = java.text.DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(data);
        return dt;
    }

    public static String dateToString(Date date)
    {
        DateFormat format = java.text.DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(date);
        return dt;
    }
}
