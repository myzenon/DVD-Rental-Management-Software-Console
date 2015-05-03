import java.util.ArrayList;
import module.DateChecker;

public class DVD {
    private int id, rentUser, rentDay, rentMonth, rentYear;
    private String name;
    private double price;
    private char age;
    private ArrayList<String[]> log;

    DVD(int id, String name, double price, char age) {
        this.id = id;
        setName(name);
        setPrice(price);
        setAge(age);
        this.rentUser = -1;
        this.log = new ArrayList<String[]>();
    }

    void addLog(String date, IMember iMember) {
        this.log.add(new String[] {date, " [" + iMember.getId() + "] "  + iMember.getFirstName() + " " + iMember.getLastName()});
    }

    ArrayList<String> getLog() {
        ArrayList<String> log = new ArrayList<String>();
        for(String[] eachlog : this.log)
            log.add(eachlog[0] + " : " + eachlog[1]);
        return log;
    }

    ArrayList<String> findLog(int day, int month, int year) {
        ArrayList<String> data = new ArrayList<String>();
        for(String[] eachlog : log) {
            if(eachlog[0].equals(day + "-" + month + "-" + year)) {
                data.add(eachlog[0] + " : " + eachlog[1]);
            }
        }
        return data;
    }

    void rentDVD(IMember rentUser,int rentDay, int rentMonth, int rentYear) {
        this.rentUser = rentUser.getId();
        this.rentDay = rentDay;
        this.rentMonth = rentMonth;
        this.rentYear = rentYear;
    }

    void returnDVD() {
        this.rentUser = -1;
    }

    int compareRentDate(int returnDay, int returnMonth, int returnYear) {
        return DateChecker.compareDate(rentDay,rentMonth,rentYear,returnDay,returnMonth,returnYear);
    }


    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setPrice(double price) {
        if(price > 0) {
            this.price = price;
        }
    }

    double getPrice() {
        return price;
    }

    char getAge() {
        return age;
    }

    void setAge(char age) {
        if(age == 'o' || age == 'n') {
            this.age = age;
        }
    }

    public int getRentUser() {
        return rentUser;
    }

    public void setRentUser(int rentUser) {
        this.rentUser = rentUser;
    }

    public int getRentDay() {
        return rentDay;
    }

    public int getRentMonth() {
        return rentMonth;
    }

    public int getRentYear() {
        return rentYear;
    }
}
