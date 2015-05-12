import java.util.ArrayList;
import module.DateChecker;
import module.Input;

public class Store {

    ArrayList<DVD> dvdList;
    int idDVDs;
    ArrayList<IMember> memberList;
    int idMembers;
    Input input;

    double normalMemberPrice ,vipMemberPrice, priceRate, priceOver;
    int oldDaysRent, newDaysRent, vipDaysRent;
    String name;

    Store(){
        dvdList = new ArrayList<DVD>();
        this.idDVDs = 0;
        memberList = new ArrayList<IMember>();
        this.idMembers = 0;
        input = new Input();
        settings();
        debug();
        showMenu();
    }

    void showMenu() {
        System.out.println("-=-=-=-=-=-=-= " + this.name + " : DVD Rental Management -=-=-=-=-=-=-=-");
        System.out.println("[1] Rent");
        System.out.println("[2] Return");
        System.out.println("[3] DVD");
        System.out.println("[4] Member");
        System.out.println("[5] History");
        System.out.println("[6] Exit");
        int menu = input.getInt("Menu > ", "Error: Please input only numbers.");
        switch (menu) {
            case 1 : rentDVD(); break;
            case 2 : returnDVD(); break;
            case 3 : showDVDMenu(); break;
            case 4 : showMemberMenu(); break;
            case 5 : statement(); break;
            case 6 : System.exit(0); break;
            default:
                System.out.println("Error : Please press only number 1-6.");
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
        int menu = input.getInt("Menu > ", "Error: Please input only numbers.");
        switch (menu) {
            case 1 : addDVD(); break;
            case 2 : searchDVD(); break;
            case 3 : editDVD(); break;
            case 4 : deleteDVD(); break;
            case 5 : showMenu(); break;
            default:
                System.out.println("Error : Please press only number 1-5.");
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
        int menu = input.getInt("Menu > ", "Error: Please input only numbers.");
        switch (menu) {
            case 1 : addMember(); break;
            case 2 : searchMember(); break;
            case 3 : editMember(); break;
            case 4 : deleteMember(); break;
            case 5 : showMenu(); break;
            default:
                System.out.println("Error : Please press only number 1-5.");
                showDVDMenu();
        }
    }

    void rentDVD() {
        System.out.println("< ============ Rent DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only numbers.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(dvd.getRentUser() == -1) {
                int memberID = input.getInt("Member ID : ", "Error : Please input only numbers.");
                IMember member = searchMemberByID(memberID);
                if(member != null) {
                    int date[] = input.getDate();
                    dvd.rentDVD(member, date[0], date[1], date[2]);
                    dvd.addLog(date[0] + "-" + date[1] + "-" + date[2], member);
                    member.addLog(date[0] + "-" + date[1] + "-" + date[2], dvd);
                    System.out.println("Members names : " + member.getFirstName() + " | It's included : " + priceRate + " baht.");
                    System.out.println("Members names : " + member.getFirstName() + " | Rent : " + dvd.getName() + ".");
                }
                else
                    System.out.println("Error : This member cannot be found.");
            }
            else
                System.out.println("Error : This DVD has already rent.");
        }
        else
            System.out.println("Error : This DVD cannot be found.");
        input.pressEnterKey();
        showMenu();
    }

    void returnDVD() {
        System.out.println("< ============ Return DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only numbers.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(dvd.getRentUser() != -1) {
                double brokenPrice = 0;
                if(input.getChar("DVD's ruined ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'.", new char[]{'y','n'}) == 'y') {
                    brokenPrice = dvd.getPrice() * 2;
                }
                int date[] = input.getDate();
                int compareDate = DateChecker.compareDate(dvd.getRentDay(), dvd.getRentMonth(), dvd.getRentYear(), date[0], date[1], date[2]);
                if(compareDate >= 0) {
                    int oldDays = (searchMemberByID(dvd.getRentUser()) instanceof NormalMember ? oldDaysRent : oldDaysRent + vipDaysRent);
                    int newDays = (searchMemberByID(dvd.getRentUser()) instanceof NormalMember ? newDaysRent : newDaysRent + vipDaysRent);
                    if((compareDate > oldDays) || (compareDate > newDays)){
                        if((dvd.getAge() == 'o') && (compareDate > oldDays))
                            System.out.println("Member Name : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " | It's included : " + (((compareDate - oldDays) * priceOver) + brokenPrice) + " baht.");
                        if((dvd.getAge() == 'n') && (compareDate > newDays))
                            System.out.println("Member Name : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " | It's included : " + (((compareDate - newDays) * priceOver) + brokenPrice) + " baht.");
                    }
                    else if (brokenPrice != 0)
                        System.out.println("Member Name : " + searchMemberByID(dvd.getRentUser()).getFirstName() + " | It's included " + brokenPrice +  " baht.");
                    System.out.println(dvd.getName() + " was completely return by " + searchMemberByID(dvd.getRentUser()).getFirstName() + ".");
                    dvd.returnDVD();
                }
                else
                    System.out.println("Error: Incorrect data.");
            }
            else
                System.out.println("Error : This DVD has not rent yet.");
        }
        else
            System.out.println("Error : This DVD cannot be found.");
        input.pressEnterKey();
        showMenu();
    }

    // ----------------------------------- DVD MENU ----------------------------------- //

    void addDVD() {
        System.out.println("< ============ Add DVD ============ >");
        String name = input.getString("Name : ", "Error : Please input DVD name.");
        double price = input.getPrice("Price : ", "Error : Please press only numbers." , "Error : Price cannot be negative numbers.");
        char age = input.getChar("Period [o = old, n = new] : ", "Error : Please input only 'o' or 'n'.", new char[]{'o','n'});
        dvdList.add(new DVD(idDVDs, name, price, age));
        idDVDs++;
        System.out.println("DVD name : " + name + " was completely with id " + (idDVDs-1) + ".");
        input.pressEnterKey();
        showDVDMenu();
    }

    void searchDVD() {
        System.out.println("< ============ Search DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only numbers.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ DVD : " + dvd.getName() + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("Name : " + dvd.getName());
            System.out.println("Price : " + dvd.getPrice());
            System.out.println("Period : " + (dvd.getAge() == 'o' ? "Old" : "New"));
            System.out.println("Status : " + (dvd.getRentUser() == -1 ? "Available" : "Rent by " + searchMemberByID(dvd.getRentUser()).getFirstName() + " At " + dvd.getRentDay() + "/" + dvd.getRentMonth() + "/" + dvd.getRentYear()));
            System.out.println("Log : ");
            for(String log : dvd.getLog())
                System.out.println(log);
        }
        else
            System.out.println("Error : This DVD cannot be found.");
        input.pressEnterKey();
        showDVDMenu();
    }

    void editDVD() {
        System.out.println("< ============ Edit DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only numbers.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ DVD : " + dvd.getName() + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("[1] Name : " + dvd.getName());
            System.out.println("[2] Price : " + dvd.getPrice());
            System.out.println("[3] Period : " + (dvd.getAge() == 'o' ? "Old" : "New"));
            int choice = input.getInt("Choice : ", "Error : Please input only numbers.");
            switch (choice) {
                case 1:
                    String name = input.getString("Name : ", "Error : Please input DVD name.");
                    dvd.setName(name);
                    break;
                case 2:
                    double price = input.getDouble("Price : ", "Error : Please input only numbers.");
                    dvd.setPrice(price);
                    break;
                case 3:
                    char age = input.getChar("Period [o = old, n = new] : ", "Error : Please input only 'o' or 'n'.", new char[]{'o','n'});
                    dvd.setAge(age);
                    break;
                default:
                    System.out.println("Sorry! Don't have this choice.");
                    break;
            }
        }
        else
            System.out.println("Error : This DVD cannot be found.");
        input.pressEnterKey();
        showDVDMenu();
    }


    void deleteDVD() {
        System.out.println("< ============ Delete DVD ============ >");
        int dvdID = input.getInt("DVD ID : ", "Error : Please input only numbers.");
        DVD dvd = searchDVDById(dvdID);
        if(dvd != null) {
            if(input.getChar("Are you sure ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'.", new char[]{'y','n'}) == 'y') {
                deleteDVDbyID(dvdID);
                System.out.println(" DVD [" + dvd.getId() + "] " + dvd.getName() + " Completely delete.");
            }
        }
        else
            System.out.println("Error : This DVD cannot be found.");
        input.pressEnterKey();
        showDVDMenu();
    }

    // ----------------------------------- DVD MENU ----------------------------------- //

    // ----------------------------------- MEMBER MENU ----------------------------------- //


    void addMember() {
        System.out.println("< ============ Add Member ============ >");
        char type = input.getChar("Type [v = VIP, n = Normal] : ", "Error : please input only 'v' or 'n'.", new char[]{'v', 'n'});
        String firstname = input.getString("First name : ", "Error : Please input first name, you can only use alphabet.", "alphabet", "Error : First name must be only alphabet.");
        String lastname = input.getString("Last name : ", "Error : Please input last name, you can only use alphabet.", "alphabet", "Error : Last name must be only alphabet.");
        char gender = input.getChar("Gender [m = Male, f = Female] : ", "Error : Please input only 'm' or 'f'.", new char[]{'m', 'f'});
        String phoneNumber = input.getString("Phone Number : ", "Error : Please input phone number, you can only use numbers.", "number", "Error : please input only numbers.");
        String address = input.getString("Address : ", "Error : Please input address, you can only use alphabet.");
        System.out.println("Date of birth :> ");
        int birthDay[] = input.getDate();
        IMember member;
        if(type == 'v') {
            member = new VIPMember(idMembers,firstname,lastname,gender,phoneNumber,address,birthDay);
        }
        else {
            member = new NormalMember(idMembers,firstname,lastname,gender,phoneNumber,address,birthDay);
        }
        memberList.add(member);
        idMembers++;
        System.out.println(member.getIDCard(this.name));
        System.out.println("Completely ! Add Member : " + firstname + " With ID : " + (idMembers-1) + ".");
        System.out.println("Members name : " + firstname + " It's included : " + (type == 'v' ? vipMemberPrice : normalMemberPrice) + " baht.");
        input.pressEnterKey();
        showMemberMenu();
    }

    void searchMember() {
        System.out.println("< ============ Search Member ============ >");
        int memberID = input.getInt("Member ID : ", "Error : Please input only numbers.");
        IMember member = searchMemberByID(memberID);
        if(member != null) {
            System.out.println("=+=+=+=+=+=+=+=+=+=+ Member : " + (member instanceof NormalMember ? "Normal" : "*VIP*") + " =+=+=+=+=+=+=+=+=+=+");
            System.out.println("FirstName : " + member.getFirstName());
            System.out.println("LastName : " + member.getLastName());
            System.out.println("Gender : " + (member.getGender() == 'm' ? "Male" : "Female"));
            System.out.println("PhoneNumber : " + member.getPhoneNumber());
            System.out.println("Address : " + member.getAddress());
            System.out.println("Date of birth : " + member.getBirthDay());
            System.out.println("Log : ");
            for(String log : member.getLog())
                System.out.println(log);
        }
        else
            System.out.println("Error : This member cannot be found.");
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
            System.out.println("[6] Date of birth : " + member.getBirthDay());
            int choice = input.getInt("Choice : ", "Error : Please input only numbers.");
            switch (choice) {
                case 1:
                    String firstname = input.getString("First name : ", "Error : Please input first name.", "alphabet", "Error : First name must be only alphabet.");
                    member.setFirstName(firstname);
                    break;
                case 2:
                    String lastname = input.getString("Last name : ", "Error : Please input last name.", "alphabet", "Error : Last name must be only alphabet.");
                    member.setLastName(lastname);
                    break;
                case 3:
                    char gender = input.getChar("Gender [m = Male, f = Female] : ", "Error : Please input only 'm' or 'f'.", new char[]{'m', 'f'});
                    member.setGender(gender);
                    break;
                case 4:
                    String phoneNumber = input.getString("Phone Number : ", "Error : Please input phone number, you can only use numbers.", "number", "Error : Please input only numbers.");
                    member.setPhoneNumber(phoneNumber);
                    break;
                case 5:
                    String address = input.getString("Address : ", "Error : Please input address.");
                    member.setAddress(address);
                    break;
                case 6:
                    System.out.println("Date of birth : > ");
                    int birthDay[] = input.getDate();
                    member.setBirthDay(birthDay);
                    break;
                default:
                    System.out.println("Sorry! Don't have this choice.");
                    break;
            }
        }
        else
            System.out.println("Error : This member cannot be found.");
        input.pressEnterKey();
        showMemberMenu();
    }

    void deleteMember() {
        System.out.println("< ============ Delete Member ============ >");
        int memberID = input.getInt("Member ID : ", "Error : Please input only numbers.");
        IMember member = searchMemberByID(memberID);
        if(member != null) {
            if(input.getChar("Are you sure ? [y = Yes, n = No] : ", "Error : Please input only 'y' or 'n'.", new char[]{'y','n'}) == 'y') {
                deleteMemberbyID(memberID);
                System.out.println("Member [" + member.getId() + "] " + member.getFirstName() + " Completely delete.");
            }
        }
        else
            System.out.println("Error : This member cannot be found.");
        input.pressEnterKey();
        showMemberMenu();
    }


    // ----------------------------------- MEMBER MENU ----------------------------------- //

    // ----------------------------------- STATEMENT MENU ----------------------------------- //

    void statement() {
        System.out.println("< (============ History of rent on a daily. ============ >");
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
            System.out.println("There is no history of any rental.");
        }
        input.pressEnterKey();
        showMenu();
    }

    // ----------------------------------- STATEMENT MENU ----------------------------------- //

    // ----------------------------------- SETTINGS MENU ----------------------------------- //

    void settings() {
        System.out.println("-------------- Settings Program --------------");
        this.normalMemberPrice = input.getPrice("Default Normal Member Fee : ", "Error : Please input only numbers.", "Error : Price can't be negative numbers.");
        this.vipMemberPrice = input.getPrice("Default VIP Member Fee : ", "Error : Please input only numbers.", "Error : Price can't be negative numbers.");
        this.oldDaysRent = input.getAmount("Default Old Movie Rent Days : ", "Error : Please input only numbers.", "Error : Amount of date can't be negative numbers.");
        this.newDaysRent = input.getAmount("Default New Movie Rent Days : ", "Error : Please input only numbers.", "Error : Amount of date can't be negative numbers.");
        this.vipDaysRent = input.getAmount("Default VIP Member Extra Days : ", "Error : Please input only numbers.", "Error : Amount of date can't be negative numbers.");
        this.priceRate = input.getPrice("Default Rent Price Rate : ", "Error : Please input only numbers.", "Error : Price can't be negative numbers.");
        this.priceOver = input.getPrice("Default Price that over each day from rent days : ", "Error : Please input only numbers.", "Error : Price can't be negative numbers.");
        this.name = input.getString("DVD Shop Name : ", "Error : Please input shop name.");
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

    // ----------------------------------- DEBUG MODE ----------------------------------- //

    void debug() {
        dvdList.add(new DVD(idDVDs,"Terminator 2: Judgment Day" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"World War Z" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Prometheus" ,289,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Secondhand Lions" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Because of Winn-Dixie" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Salt" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Kick-Ass" ,289,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Dark Knight " ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Dark Knight Rises " ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Superman" ,289,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Dirty Dancing " ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Notebook " ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Prestige" ,100,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Shutter Island" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"50/50" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Juno" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Stardust" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Princess Bride " ,289,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Lemony Snicket's A Series of Unfortunate Events" ,289,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Shrek" ,250,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"UP" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"How to Train Your Dragon" ,100,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The X-Files" ,189,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"American Horror Story" ,199,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Once Upon a Time " ,100,'o'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Revenge " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Single White Female " ,100,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Hand That Rocks the Cradle " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Black Swan " ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Girl with the Dragon Tattoo  " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"American Psycho  " ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Side Effects " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Call " ,250,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Savannah Smiles " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"A Nightmare on Elm Street " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Tourist " ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Gia" ,289,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Sin City " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Knocked Up " ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Easy A" ,250,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Going the Distance " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Scream" ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"From Hell " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Avengers " ,289,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Star Wars: Episode V The Empire Strikes Back " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"The Matrix " ,189,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Contagion " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Revenge " ,250,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Silver Linings Playbook " ,199,'n'));
        idDVDs++;
        dvdList.add(new DVD(idDVDs,"Ted " ,199,'n'));
        idDVDs++;

        memberList.add(new VIPMember(idMembers,"Jason","Statham",'m',"0847372822","1250 Weaver Street,San Diego CA 92114",new int[]{11,12,1994}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Bud","Abbott",'m',"0844736322","1072 Folsom Street, #470San Francisco, CA 94103",new int[]{19,1,2000}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Paula","Abdul",'m',"0827465332","174 Spandia Avenue, Suite 610 Toronto, ON M5T 2C2",new int[]{4,9,1994}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Antonio","Aguilar",'m',"0822533637","PO Box 86605 Los Angeles, CA 90086",new int[]{28,2,2001}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Philip","Ahn",'m',"0835353536","565 North Fifth Street San Jose, CA 95112",new int[]{13,11,1999}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Paul","Anka",'m',"0853627273","244 Madison Ave. #277 New York , NY , 10016",new int[]{18,5,1989}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Richard","Arlen",'m',"0836464627","3010 W Wilson Apt 3 Chicago IL 60625",new int[]{11,2,1970}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"George","Arliss",'m',"0838382299","Los Angeles, CA 90012 213.625.7000",new int[]{25,10,1789}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"James","Arness",'m',"0822662277","977 S Van Ness Ave, San Francisco, CA 94110",new int[]{21,7,1892}));
        idMembers++;
        memberList.add(new VIPMember(idMembers,"Edward", "Arnold",'m',"0852772233","109 West 27th Street, Suite 9A New York, NY 10001",new int[]{3,8,2001}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Carroll","Baker",'f',"0835262772","410 Queens Quay West Suite 401",new int[]{12,7,1699}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Lucille","Ball",'f',"0826337474","1145 Wilshire Boulevard, Suite 100-D Los Angeles, CA 90017",new int[]{3,2,1923}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Binnie","Barnes",'f',"0926367272","9903 Santa Monica, Suite 575 Beverly Hills, CA 90212",new int[]{10,4,2003}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Alice"," Brady",'f',"0862537272","46 Merchant Street Honolulu, Hawai'i 96813",new int[]{8,3,1992}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Lina","Basquette",'f',"0963747475","1271 Avenues of Americas, Suite 4300, 43rd Floor, New York, NY 10020",new int[]{30,11,1999}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Bernie","Brillstein",'f',"0935673884","520 Eighth Ave., Suite 309 New York, NY 10018",new int[]{16,11,1890}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Belle","Bennett",'f',"0863734475","68 Jay Street #711 Brooklyn NY 11201",new int[]{6,5,1742}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Vanessa","Brown",'f',"0835745754","1501 S. 4th St. Minneapolis, MN 55454",new int[]{9,8,1989}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Sandra","Bullock",'f',"0943446633","PO Box 70554 Washington, DC 20024",new int[]{8,2,1900}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Sarah","Bernhardt",'f',"0936637363","520 8th Ave. New York, NY 10012",new int[]{22,2,1999}));
        idMembers++;
        memberList.add(new NormalMember(idMembers,"Leonard","Bernstein",'f',"0862565532","914 Westwood Blvd PMB 140 Los Angeles, CA 90024",new int[]{23,7,1789}));
        idMembers++;

        rentDVD_parm(0, 0, new int[]{9, 5, 2015});
        rentDVD_parm(2, 2, new int[]{9, 5, 2015});
        rentDVD_parm(6, 6, new int[]{9, 5, 2015});
        rentDVD_parm(4, 4, new int[]{9, 5, 2015});
        rentDVD_parm(8, 8, new int[]{9, 5, 2015});
        rentDVD_parm(10, 10, new int[]{10, 5, 2015});
        rentDVD_parm(12, 12, new int[]{10, 5, 2015});
        rentDVD_parm(14, 14, new int[]{10, 5, 2015});
        rentDVD_parm(16, 16, new int[]{10, 5, 2015});
        rentDVD_parm(18, 18, new int[]{10, 5, 2015});
        rentDVD_parm(1, 1, new int[]{11, 5, 2015});
        rentDVD_parm(3, 3, new int[]{11, 5, 2015});
        rentDVD_parm(5, 5, new int[]{11, 5, 2015});
        rentDVD_parm(7, 7, new int[]{11, 5, 2015});
        rentDVD_parm(9, 9, new int[]{11, 5, 2015});

        returnDVD_parm(6);
        returnDVD_parm(8);
        returnDVD_parm(12);
        returnDVD_parm(3);
        returnDVD_parm(7);
        returnDVD_parm(9);
        returnDVD_parm(4);
        returnDVD_parm(2);
        returnDVD_parm(0);


        rentDVD_parm(9, 1, new int[]{11, 5, 2015});
    }

    void rentDVD_parm(int dvdID, int memberID, int date[]) {
        DVD dvd = searchDVDById(dvdID);
        IMember member = searchMemberByID(memberID);
        dvd.rentDVD(member,date[0],date[1],date[2]);
        dvd.addLog(date[0] + "-" + date[1] + "-" + date[2], member);
        member.addLog(date[0] + "-" + date[1] + "-" + date[2], dvd);
    }

    void returnDVD_parm(int dvdID) {
        DVD dvd = searchDVDById(dvdID);
        dvd.returnDVD();
    }

    // ----------------------------------- DEBUG MODE ----------------------------------- //
}