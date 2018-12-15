
package my.unimas.a54440siswa.fcsithub;
public class Chat {

    private String ChatUserId ;
    private String ChatUserName;
    private String Notification;

    public Chat() {
    }

    public Chat(String chatUserId, String chatUserName, String notification) {
        ChatUserId = chatUserId;
        ChatUserName = chatUserName;
        Notification = notification;
    }

    public String getChatUserId() {
        return ChatUserId;
    }

    public String getChatUserName() {
        return ChatUserName;
    }

    public String getNotification() {
        return Notification;
    }

    public void setChatUserId(String chatUserId) {
        ChatUserId = chatUserId;
    }

    public void setChatUserName(String chatUserName) {
        ChatUserName = chatUserName;
    }

    public void setNotification(String notification) {
        ChatUserName = notification;
    }
}