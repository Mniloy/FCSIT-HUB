package my.unimas.a54440siswa.fcsithub;
public class Users {


    private String UserID ;
    private String UserName ;

    public Users() {}

    public Users(String chatUserID, String chatUserName) {
        UserID = chatUserID;
        UserName = chatUserName;
    }


    public String getUserID() {
        return UserID;
    }
    public String getUserName() {
        return UserName;
    }


    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}