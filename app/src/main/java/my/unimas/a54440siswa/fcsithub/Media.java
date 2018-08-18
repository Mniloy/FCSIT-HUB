
package my.unimas.a54440siswa.fcsithub;
public class Media {

    private String Post;
    private String PostUserName ;
    private String PostTime ;
    private String PostDate ;
    private String MediaID ;
    private String UserID ;

    public Media() {
    }

    public Media(String post, String postusername, String posttime, String postdate, String mediaid, String userid) {
        Post = post;
        PostUserName = postusername;
        PostTime = posttime;
        PostDate = postdate;
        MediaID = mediaid;
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

    public String getMediaID() {
        return MediaID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setName(String post) { Post = post; }

    public void setPostUserName(String postusername) {
        PostUserName = postusername;
    }

    public void setPostTime(String posttime) { PostTime = posttime; }

    public void setPostDate(String postdate) { PostDate = postdate; }

    public void setMediaID(String mediaid) { PostDate = mediaid; }

    public void setUserID(String userid) { PostDate = userid; }

}