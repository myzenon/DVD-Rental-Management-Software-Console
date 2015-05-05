import java.util.ArrayList;

import module.DateChecker;
import module.Input;
import module.StringChecker;

public class Store {

    ArrayList<DVD> dvdList;
    int idDVDs;
    ArrayList<IMember> memberList;
    int idMembers;
    Input input;

//    double normalMemberPrice = 23;
//    double vipMemberPrice = 45;
//    double priceRate = 20;
//    int oldDaysRent = 3;
//    int newDaysRent = 1;
//    double priceOver = 10;

    double normalMemberPrice ,vipMemberPrice = 45, priceRate = 20, priceOver = 10;
    int oldDaysRent = 3, newDaysRent = 1;
    String name;


    Store(){
        dvdList = new ArrayList<DVD>();
        this.idDVDs = 0;
        memberList = new ArrayList<IMember>();
        this.idMembers = 0;
        input = new Input();
        settings();
        showMenu();
    }

    void showMenu() {
        System.out.println("-=-=-=-=-=-=-= " + this.name + " : DVD Rental Management -=-=-=-=-=-=-=-");
        System.out.println("[1] Rent");
        System.out.println("[2] Return");
        System.out.println("[3] DVD");
        System.out.println("[4] Member");
        System.out.println("[5] Statement");
        System.out.println("[6] Exit");
        int menu = input.getInt("Menu > ", "Error: Please input only number");
        switch (menu) {
            case 1 : rentDVD(); break;
            case 2 : returnDVD(); break;
            case 3 : showDVDMenu(); break;
            case 4 : showMemberMenu(); break;
            case 5 : statement(); break;
            case 6 : System.exit(0); break;
            default:
                System.out.println("Error : Not Found this menu number.");
                showMenu();
        }
    }

    void showDVDMenu() {
        System.out.println("+++++++++++++++ DVD Menu +++++++++++++++");
        System.out.println("[1] Add");
        System.out.println("[2] Search");
        System.out.println("[3] Edit");
        System.out.println("[4] Delete");
        System.out.println("[5] Back to Menu");
        int menu = input.getInt("Menu > ", "Error: Please input only number");
        switch (menu) {
            case 1 : addDVD(); break;
            case 2 : searchDVD(); break;
            case 3 : editDVD(); break;
            case 4 : deleteDVD(); break;
            case 5 : showMenu(); break;
            default:
                System.out.println("Error : Not Found this menu number.");
                showDVDMenu();
        }
    }

    void showMemberMenu() {
        System.out.println("+++++++++++++++ Member Menu +++++++++++++++");
        System.out.println("[1] Add");
        System.out.println("[2] Search");
        System.out.println("[3] Edit");
        System.out.println("[4] Delete");
        System.out.println("[5] Back to Menu");
        int menu = input.getInt("Menu > ", "Error: Please input only number");
        switch (menu) {
            case 1 : addMember(); break;
            case 2 : searchMember(); break;
            case 3 : editMember(); break;
            case 4 : deleteMember(); break;
            case 5 : showMenu(); break;
            default:
                System.out.println("Error : Not Found this menu number.");
                showDVDMenu();
        }
    }

    void rentDVD() {
        System.out.println("< ============ Rent DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only number");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(dvd.getRentUser() == -1) {
                int memberID = input.getInt("Member ID : ", "Error : Please input only number");
                IMember member = searchMemberByID(memberID);
                if(member != null) {
                    int date[] = input.getDate();
                    dvd.rentDVD(member,date[0],date[1],date[2]);
                    dvd.addLog(date[0] + "-" + date[1] + "-" + date[2], member);
                    member.addLog(date[0] + "-" + date[1] + "-" + date[2], dvd);
                    System.out.println("System : " + member.getFirstName() + " must pay " + priceRate + " baht");
                    System.out.println("Complete : " + member.getFirstName() + " Rented " + dvd.getName());
                }
                else
                    System.out.println("Error : Cannot found this member.");
            }
            else
                System.out.println("Error : this dvd has already rent.");
        }
        else
            System.out.println("Error : Cannot found dvd.");
        input.pressEnterKey();
        showMenu();
    }

    void returnDVD() {
        System.out.println("< ============ Return DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only number");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(dvd.getRentUser() != -1) {
                double brokenPrice = 0;
                if(input.getChar("Broken ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'", new char[]{'y','n'}) == 'y') {
                    brokenPrice = dvd.getPrice() * 2;
                }
                int date[] = input.getDate();
                int compareDate = DateChecker.compareDate(dvd.getRentDay(), dvd.getRentMonth(), dvd.getRentYear(), date[0], date[1], date[2]);
                if(compareDate >= 0) {
                    if((compareDate > oldDaysRent) || (compareDate > newDaysRent)){
                        if((dvd.getAge() == 'o') && (compareDate > oldDaysRent))
                            System.out.println("System : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " must pay " + (((compareDate - oldDaysRent) * priceOver) + brokenPrice) + " baht");
                        if((dvd.getAge() == 'n') && (compareDate > newDaysRent))
                            System.out.println("System : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " must pay " + (((compareDate - newDaysRent) * priceOver) + brokenPrice) + " baht");
                    }
                    else if (brokenPrice != 0)
                        System.out.println("System : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " must pay " + brokenPrice +  " baht");
                    System.out.println("Complete : Returned " + dvd.getName() + " from " + searchMemberByID(dvd.getRentUser()).getFirstName());
                    dvd.returnDVD();
                }
                else
                    System.out.println("Error: Date Incorrect.");
            }
            else
                System.out.println("Error : this dvd has not already rent.");
        }
        else
            System.out.println("Error : Cannot found dvd.");
        input.pressEnterKey();
        showMenu();
    }

    // ----------------------------------- DVD MENU ----------------------------------- //

    void addDVD() {
        System.out.println("< ============ Add DVD ============ >");
        String name = input.getString("Name : ", "Error : Please input dvd name.");
        double price = input.getPrice("Price : ", "Error : Please input price only double." , "Error : Price can't minus.");
        char age = input.getChar("Age [o = old, n = new] : ", "Error : Please input only 'o' and 'n'", new char[]{'o','n'});
        dvdList.add(new DVD(idDVDs, name, price, age));
        idDVDs++;
        System.out.println("OK : DVD " + name + " add complete with id " + (idDVDs-1));
        input.pressEnterKey();
        showDVDMenu();
    }

    void searchDVD() {
        System.out.println("< ============ Search DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only number.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ DVD : " + dvd.getName() + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("Name : " + dvd.getName());
            System.out.println("Price : " + dvd.getPrice());
            System.out.println("Age : " + (dvd.getAge() == 'o' ? "Old" : "New"));
            System.out.println("Status : " + (dvd.getRentUser() == -1 ? "Available" : "Rent by " + searchMemberByID(dvd.getRentUser()).getFirstName() + " At " + dvd.getRentDay() + "/" + dvd.getRentMonth() + "/" + dvd.getRentYear()));
            System.out.println("Log :: ");
            for(String log : dvd.getLog())
                System.out.println(log);
        }
        else
            System.out.println("Error : Cannot found dvd.");
        input.pressEnterKey();
        showDVDMenu();
    }

    void editDVD() {
        System.out.println("< ============ Edit DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only number.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ DVD : " + dvd.getName() + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("[1] Name : " + dvd.getName());
            System.out.println("[2] Price : " + dvd.getPrice());
            System.out.println("[3] Age : " + (dvd.getAge() == 'o' ? "Old" : "New"));
            int choice = input.getInt("Choice : ", "Error : Please input only number");
            switch (choice) {
                case 1:
                    String name = input.getString("Name : ", "Error : Please input dvd name.");
                    dvd.setName(name);
                    break;
                case 2:
                    double price = input.getDouble("Price : ", "Error : Please input price only double.");
                    dvd.setPrice(price);
                    break;
                case 3:
                    char age = input.getChar("Age [o = old, n = new] : ", "Error : Please input only 'o' and 'n'", new char[]{'o','n'});
                    dvd.setAge(age);
                    break;
                default:
                    System.out.println("No this choice");
                    break;
            }
        }
        else
            System.out.println("Error : Cannot found dvd.");
        input.pressEnterKey();
        showDVDMenu();
    }


    void deleteDVD() {
        System.out.println("< ============ Delete DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only number.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(input.getChar("Are you sure ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'", new char[]{'y','n'}) == 'y') {
                deleteDVDbyID(dvdID);
                System.out.println("OK : DVD [" + dvd.getId() + "] " + dvd.getName() + " Delete Complete.");
            }
        }
        else
            System.out.println("Error : Cannot found dvd.");
        input.pressEnterKey();
        showDVDMenu();
    }

    // ----------------------------------- DVD MENU ----------------------------------- //

    // ----------------------------------- MEMBER MENU ----------------------------------- //


    void addMember() {
        System.out.println("< ============ Add Member ============ >");
        char type = input.getChar("Type [v = VIP, n = Normal] : ", "Error : please inpt only 'v' or 'n'", new char[]{'v', 'n'});
        String firstname = input.getString("Firstname : ", "Error : Please input firstname.", "alphabet", "Error : firstname must be only alphabet.");
        String lastname = input.getString("Lastname : ", "Error : Please input lastname.", "alphabet", "Error : lastname must be only alphabet.");
        char gender = input.getChar("Gender [m = Male, f = Female] : ", "Error : please inpt only 'm' or 'f'", new char[]{'m', 'f'});
        String phoneNumber = input.getString("Phone Number : ", "Error : please input phonenumber.", "number", "Error : please input only number.");
        String address = input.getString("Address : ", "Error : please input address");
        System.out.println("BirthDay :> ");
        int birthDay[] = input.getDate();
        IMember member = null;
        if(type == 'v') {
            member = new VIPMember(idMembers,firstname,lastname,gender,phoneNumber,address,birthDay);
        }
        else {
            member = new NormalMember(idMembers,firstname,lastname,gender,phoneNumber,address,birthDay);
        }
        memberList.add(member);
        idMembers++;
        System.out.println(member.getIDCard(this.name));
        System.out.println("Complete : Add Member : " + firstname + " With ID : " + (idMembers-1));
        System.out.println("System : " + firstname + " must pay " + (type == 'v' ? vipMemberPrice : normalMemberPrice + " baht."));
        input.pressEnterKey();
        showMemberMenu();
    }

    void searchMember() {
        System.out.println("< ============ Search Member ============ >");
        int memberID = input.getInt("Member ID : ", "Error : Please input only number.");
        IMember member = searchMemberByID(memberID);
        if(member != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ Member : " + (member instanceof NormalMember ? "Normal" : "*VIP*") + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("FirstName : " + member.getFirstName());
            System.out.println("LastName : " + member.getLastName());
            System.out.println("Gender : " + (member.getGender() == 'm' ? "Male" : "Female"));
            System.out.println("PhoneNumber : " + member.getPhoneNumber());
            System.out.println("Address : " + member.getAddress());
            System.out.println("BirthDay : " + member.getBirthDay());
            System.out.println("Log :: ");
            for(String log : member.getLog())
                System.out.println(log);
        }
        else
            System.out.println("Error : Cannot found member.");
        input.pressEnterKey();
        showMemberMenu();
    }

    void editMember() {
        System.out.println("< ============ Edit Member ============ >");
        int memberID = input.getInt("Member ID : ", "Error : Please input only number.");
        IMember member = searchMemberByID(memberID);
        if(member != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ Member : " + (member instanceof NormalMember ? "Normal" : "*VIP*") + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("[1] FirstName : " + member.getFirstName());
            System.out.println("[2] LastName : " + member.getLastName());
            System.out.println("[3] Gender : " + (member.getGender() == 'm' ? "Male" : "Female"));
            System.out.println("[4] PhoneNumber : " + member.getPhoneNumber());
            System.out.println("[5] Address : " + member.getAddress());
            System.out.println("[6] BirthDay : " + member.getBirthDay());
            int choice = input.getInt("Choice : ", "Error : Please input only number");
            switch (choice) {
                case 1:
                    String firstname = input.getString("Firstname : ", "Error : Please input firstname.", "alphabet", "Error : firstname must be only alphabet.");
                    member.setFirstName(firstname);
                    break;
                case 2:
                    String lastname = input.getString("Lastname : ", "Error : Please input lastname.", "alphabet", "Error : lastname must be only alphabet.");
                    member.setLastName(lastname);
                    break;
                case 3:
                    char gender = input.getChar("Gender [m = Male, f = Female] : ", "Error : please inpt only 'm' or 'f'", new char[]{'m', 'f'});
                    member.setGender(gender);
                    break;
                case 4:
                    String phoneNumber = input.getString("Phone Number : ", "Error : please input phonenumber.", "number", "Error : please input only number.");
                    member.setPhoneNumber(phoneNumber);
                    break;
                case 5:
                    String address = input.getString("Address : ", "Error : please input address");
                    member.setAddress(address);
                    break;
                case 6:
                    System.out.println("BirthDay :> ");
                    int birthDay[] = input.getDate();
                    member.setBirthDay(birthDay);
                    break;
                default:
                    System.out.println("No this choice");
                    break;
            }
        }
        else
            System.out.println("Error : Cannot found member.");
        input.pressEnterKey();
        showMemberMenu();
    }

    void deleteMember() {
        System.out.println("< ============ Delete Member ============ >");
        int memberID = input.getInt("Member ID : ", "Error : Please input only number.");
        IMember member = searchMemberByID(memberID);
        if(member != null) {
            if(input.getChar("Are you sure ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'", new char[]{'y','n'}) == 'y') {
                deleteMemberbyID(memberID);
                System.out.println("OK : Member [" + member.getId() + "] " + member.getFirstName() + " Delete Complete.");
            }
        }
        else
            System.out.println("Error : Cannot found member.");
        input.pressEnterKey();
        showMemberMenu();
    }


    // ----------------------------------- MEMBER MENU ----------------------------------- //

    // ----------------------------------- STATEMENT MENU ----------------------------------- //

    void statement() {
        System.out.println("< ============ Statement for each day ============ >");
        int date[] = input.getDate();
        ArrayList<String> allLog = new ArrayList<String>();
        for (DVD dvd : dvdList) {
            ArrayList<String> log = dvd.findLog(date[0],date[1],date[2]);
            if(!log.isEmpty()) {
                for(String logString : log) {
                    allLog.add(logString + " -> Took -> " + "[" + dvd.getId() + "] " + dvd.getName());
                }
            }
        }
        if(!allLog.isEmpty()) {
            for(String log : allLog) {
                System.out.println(log);
            }
        }
        else {
            System.out.println("No Statement for this day.");
        }
        input.pressEnterKey();
        showMenu();
    }

    // ----------------------------------- STATEMENT MENU ----------------------------------- //

    // ----------------------------------- SETTINGS MENU ----------------------------------- //

    void settings() {
        System.out.println("-------------- Settings Program --------------");
        this.normalMemberPrice = input.getPrice("Default Normal Member Fee : ", "Error : Please input only number", "Error : Price can't minus.");
        this.vipMemberPrice = input.getPrice("Default VIP Member Fee : ", "Error : Please input only number", "Error : Price can't minus.");
        this.oldDaysRent = input.getAmount("Default Old Movie Rent Days : ", "Error : Please input only number", "Error : Amount Days can't minus.");
        this.newDaysRent = input.getAmount("Default New Movie Rent Days : ", "Error : Please input only number", "Error : Amount Days can't minus.");
        this.priceRate = input.getPrice("Default Rent Price Rate : ", "Error : Please input only number", "Error : Price can't minus.");
        this.priceOver = input.getPrice("Default Price that over each day from rent days : ", "Error : Please input only number", "Error : Price can't minus.");
        this.name = input.getString("DVD Shop Name : ", "Error : Please input shop name");
    }

    // ----------------------------------- SETTINGS MENU ----------------------------------- //

    // ----------------------------------- DATABASE INTERFACE ----------------------------------- //

    DVD searchDVDById(int dvdID) {
        try {
            return dvdList.get(dvdID);
        }
        catch (Exception e) {
            return null;
        }
    }

    IMember searchMemberByID(int memberID) {
        try {
            return memberList.get(memberID);
        }
        catch (Exception e) {
            return null;
        }
    }


    void deleteDVDbyID(int dvdID) {
        dvdList.set(dvdID, null);
    }

    void deleteMemberbyID(int memberID) {
        memberList.set(memberID, null);
    }

    // ----------------------------------- DATABASE INTERFACE ----------------------------------- //
}
