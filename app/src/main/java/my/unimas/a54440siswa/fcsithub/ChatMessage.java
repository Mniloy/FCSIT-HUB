package my.unimas.a54440siswa.fcsithub;

public class ChatMessage {

    private String ChatPartnerUserId, ChatMessageId, SenderId, Time, Message;

    public ChatMessage() {
    }

    public ChatMessage(String chatPartnerUserId, String chatMessageId, String senderId, String message, String time) {
        ChatPartnerUserId = chatPartnerUserId;
        ChatMessageId = chatMessageId;
        SenderId = senderId;
        Message = message;
        Time = time;

    }


    public String getChatPartnerUserId() {
        return ChatPartnerUserId;
    }

    public void setChatPartnerUserId(String chatPartnerUserId) {
        ChatPartnerUserId = chatPartnerUserId;
    }


    public String getChatMessageId() {
        return ChatMessageId;
    }

    public void setChatMessageId(String chatMessageId) {
        ChatMessageId = chatMessageId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
