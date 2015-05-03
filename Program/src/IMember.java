import java.util.ArrayList;

public interface IMember {

    String getIDCard(String shopname);

    int getId();

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getAddress();

    void setAddress(String address);

    char getGender();

    void setGender(char gender);

    String getBirthDay();

    void setBirthDay(int date[]);

    void addLog(String date, DVD dvd);

    ArrayList<String> getLog();

}
