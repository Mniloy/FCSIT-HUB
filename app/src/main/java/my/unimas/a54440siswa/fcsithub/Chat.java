
package my.unimas.a54440siswa.fcsithub;
public class Chat {

    private String ChatUserId ;
    private String ChatUserName;

    public Chat() {
    }

    public Chat(String chatUserId, String chatUserName) {
        ChatUserId = chatUserId;
        ChatUserName = chatUserName;
    }

    public String getChatUserId() {
        return ChatUserId;
    }

    public String getChatUserName() {
        return ChatUserName;
    }

    public void setChatUserId(String chatUserId) {
        ChatUserId = chatUserId;
    }

    public void setChatUserName(String chatUserName) {
        ChatUserName = chatUserName;
    }
}