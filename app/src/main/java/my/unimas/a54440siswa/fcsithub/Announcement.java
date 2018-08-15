
package my.unimas.a54440siswa.fcsithub;
public class Announcement {

    private String Post;
    private String PostUserName ;
    private String PostTime ;
    private String PostDate ;

    public Announcement() {
    }

    public Announcement(String post, String postusername, String posttime, String postdate) {
        Post = post;
        PostUserName = postusername;
        PostTime = posttime;
        PostDate = postdate;
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

    public void setName(String post) { Post = post; }

    public void setPostUserName(String postusername) {
        PostUserName = postusername;
    }

    public void setPostTime(String posttime) { PostTime = posttime; }

    public void setPostDate(String postdate) { PostDate = postdate; }

}