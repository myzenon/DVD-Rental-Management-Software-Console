/***************************************
 *
 * > This classes for get data value from keyboard.
 * (Created by Zenon 'SI)
 *
 ***************************************/

package module;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    private Scanner scanner;

    public Input() {
        scanner = new Scanner(System.in);
    }

    public int getInt(String getMSG, String errorMSG) {
        int data;
        System.out.print(getMSG);
        try {
            data = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println(errorMSG);
            data = getInt(getMSG, errorMSG);
        }
        return data;
    }

//    public int getInt(String getMSG, String errorMSG, int lengthLimit, String lengthMSG) {
//        int data;
//        System.out.print(getMSG);
//        try {
//            data = scanner.nextInt();
//            scanner.nextLine();
//            if(((int)(Math.log10(data) + 1)) > lengthLimit) {
//                throw new Exception();
//            }
//        }
//        catch (InputMismatchException e) {
//            scanner.nextLine();
//            System.out.println(errorMSG);
//            data = getInt(getMSG, errorMSG, lengthLimit, lengthMSG);
//        }
//        catch (Exception e) {
//            System.out.println(lengthMSG);
//            data = getInt(getMSG, errorMSG, lengthLimit, lengthMSG);
//        }
//        return data;
//    }

    public double getDouble(String getMSG, String errorMSG) {
        double data;
        System.out.print(getMSG);
        try {
            data = scanner.nextDouble();
            scanner.nextLine();
        }
        catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println(errorMSG);
            data = getDouble(getMSG, errorMSG);
        }
        return data;
    }

    public String getString(String getMSG, String blankMSG) {
        String data;
        System.out.print(getMSG);
        data = scanner.nextLine();
        if(data.equals("")) {
            System.out.println(blankMSG);
            data = getString(getMSG,blankMSG);
        }
        return data;
    }

    public String getString(String getMSG, String blankMSG, String option, String optionMSG) {
        String data = getString(getMSG, blankMSG);
        if(option.equals("alphabet")) {
            while(!StringChecker.checkAlphabet(data)) {
                System.out.println(optionMSG);
                data = getString(getMSG, blankMSG);
            }
        }
        else if(option.equals("number")) {
            while(!StringChecker.checkNumber(data)) {
                System.out.println(optionMSG);
                data = getString(getMSG, blankMSG);
            }
        }
        return data;
    }

    public char getChar(String getMSG, String errorMSG, char rule[]) {
        char data;
        System.out.print(getMSG);
        data = scanner.nextLine().charAt(0);
        boolean checker = false;
        for (char c : rule) {
            if(c == data) {
                checker = true;
            }
        }
        if(!checker) {
            System.out.println(errorMSG);
            data = getChar(getMSG, errorMSG, rule);
        }
        return data;
    }


    public int[] getDate() {
        int year = getInt("Year : ", "Error : Year must be only the number.");
        while(year <= 0) {
            System.out.println("Error : Year Incorrect.");
            year = getInt("Year : ", "Error : Year must be only the number.");
        }
        int month = getInt("Month : ", "Error : Month must be only the number.");
        while((month < 1) || (month > 12)) {
            System.out.println("Error : Month Incorrect.");
            month = getInt("Month : ", "Error : Month must be only the number.");
        }
        int day = getInt("Day : ", "Error : Day must be only the number.");
        while((day < 1) || (day > DateChecker.endDayofMonth(month,year))) {
            System.out.println("Error : Day Incorrect.");
            day = getInt("Day : ", "Error : Day must be only the number.");
        }
        return new int[]{day,month,year};
    }


    public double getPrice(String getMSG, String errorMSG, String errorMSGMinus) {
        double price = getDouble(getMSG, errorMSG);
        while(price < 0) {
            System.out.println(errorMSGMinus);
            price = getDouble(getMSG, errorMSG);
        }
        return price;
    }


    public int getAmount(String getMSG, String errorMSG, String errorMSGMinus) {
        int amount = getInt(getMSG,errorMSG);
        while(amount < 0) {
            System.out.println(errorMSGMinus);
            amount =  getInt(getMSG,errorMSG);
        }
        return amount;
    }

    public void pressEnterKey() {
        System.out.println("Press enter key to continue ...");
        scanner.nextLine();
    }

}
