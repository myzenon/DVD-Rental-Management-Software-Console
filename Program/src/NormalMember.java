import module.DateChecker;
import module.StringChecker;
import java.util.ArrayList;

public class NormalMember implements IMember {
    private String firstName, lastName, address, phoneNumber;
    private int id, dayBirth, monthBirth, yearBirth;
    private char gender;
    private ArrayList<String[]> log;

    public String getIDCard(String shopname) {
        return "--------------------------------------------------- \n" +
                "[ " + shopname + " : ID CARD ]\n" +
                "Name : " + firstName + " " + lastName + "\n" +
                "Gender : " + (gender == 'm' ? "Male" : "Female") + " " + " PhoneNumber : " + phoneNumber + "\n" +
                "Address : " + address + "\n" +
                "Birthday : " + dayBirth + "-" + monthBirth + "-" + yearBirth + "\n" +
                "--------------------------------------------------";
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(StringChecker.checkAlphabet(firstName)) {
            this.firstName = firstName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(StringChecker.checkAlphabet(lastName)) {
            this.lastName = lastName;
        }
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        if((gender == 'm') || (gender == 'f')) {
            this.gender = gender;
        }
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(StringChecker.checkNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDay() {
        return this.dayBirth + "-" + this.monthBirth + "-" + this.yearBirth;
    }

    public void setBirthDay(int date[]) {
        int year = date[2];
        int month = date[1];
        int day = date[0];
        if(year > 0) {
            if((month > 0) && (month < 13)) {
                if((day > 0) && (day <= DateChecker.endDayofMonth(month,year))) {
                    this.dayBirth = day;
                    this.monthBirth = month;
                    this.yearBirth = year;
                }
            }
        }

    }

    public void addLog(String date, DVD dvd) {
        this.log.add(new String[] {date, " [" + dvd.getId() + "] "  + dvd.getName()});
    }

    public ArrayList<String> getLog() {
        ArrayList<String> log = new ArrayList<String>();
        for (String[] eachlog : this.log)
            log.add(eachlog[0] + " : " + eachlog[1]);
        return log;
    }

    public NormalMember(int id, String firstName, String lastName, char gender, String phoneNumber,  String address, int birthDay[]) {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setBirthDay(birthDay);
        this.log = new ArrayList<String[]>();
    }
}
