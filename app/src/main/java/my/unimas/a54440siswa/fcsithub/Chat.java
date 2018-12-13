
package my.unimas.a54440siswa.fcsithub;
public class Chat {

    private String ChatId;
    private String ChatUserId ;
    private String ChatUserName;

    public Chat() {
    }

    public Chat(String chatId, String chatUserId, String chatUserName) {
        ChatId = chatId;
        ChatUserId = chatUserId;
        ChatUserName = chatUserName;
    }

    public String getChatId() {
        return ChatId;
    }

    public String getChatUserId() {
        return ChatUserId;
    }

    public String getChatUserName() {
        return ChatUserName;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    public void setChatUserId(String chatUserId) {
        ChatUserId = chatUserId;
    }

    public void setChatUserName(String chatUserName) {
        ChatUserName = chatUserName;
    }
}