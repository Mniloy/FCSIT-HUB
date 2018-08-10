
package my.unimas.a54440siswa.fcsithub;
public class Contact {

    private String Name;
    private String Email ;
    private String Number ;

    public Contact() {
    }

    public Contact(String name, String email, String number) {
        Name = name;
        Email = email;
        Number = number;
    }


    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getNumber() {
        return Number;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setNumber(String number) {
        Number = number;
    }

}