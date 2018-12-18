
package my.unimas.a54440siswa.fcsithub;
public class Announcement {

    private String Post;
    private String PostUserName ;
    private String PostTime ;
    private String PostDate ;
    private String AnnouncementID ;
    private String UserID ;

    public Announcement() {
    }

    public Announcement(String post, String postusername, String posttime, String postdate, String announcementid, String userid) {
        Post = post;
        PostUserName = postusername;
        PostTime = posttime;
        PostDate = postdate;
        AnnouncementID = announcementid;
        UserID = userid;
    }


    public String getPost() {
        return Post;
    }

    public String getPostUserName() {
        return PostUserName;
    }

    public String getPostTime() {
        return PostTime;
    }

    public String getPostDate() {
        return PostDate;
    }

    public String getAnnouncementID() {
        return AnnouncementID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setPost(String post) { Post = post; }

    public void setPostUserName(String postusername) {
        PostUserName = postusername;
    }

    public void setPostTime(String posttime) { PostTime = posttime; }

    public void setPostDate(String postdate) { PostDate = postdate; }

    public void setAnnouncementID(String announcementid) { PostDate = announcementid; }

    public void setUserID(String userid) { PostDate = userid; }

}