/***************************************
 *
 * > This classes for check date in many condition
 * (Created by Zenon 'SI)
 *
 *  isLeapYear(Year) => Boolean
 *  endDayofMonth(Month,Year) => Int (Amount of Day in Month)
 *  compareDate(startDay,startMonth,startYear,endDay,endMonth,endYear) => Int (Day between both date)
 *
 ***************************************/
package module;

import java.util.Date;

public class DateChecker {
    public static boolean isLeapYear(int year) {
        if(( ((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0) )
            return true;
        else
            return false;
    }
    public static int endDayofMonth(int month, int year) {
        if(month == 2) {
            if(isLeapYear(year))
                return 29;
            else
                return 28;
        }
        else if((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12)) {
            return 31;
        }
        else {
            return 30;
        }
    }
    public static int compareDate(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        Date startDate = new Date(startYear-1900,startMonth-1,startDay); // Start 1900-01-00
        Date endDate = new Date(endYear-1900,endMonth-1,endDay); // Start 1900-01-00
        /*
             Divided 1000 for change to seconds
             Divided 3600 for change to hours
             Divided 24 for change to days
         */
        return (int)((endDate.getTime() - startDate.getTime()) /  1000 / 3600 / 24);
    }
}
