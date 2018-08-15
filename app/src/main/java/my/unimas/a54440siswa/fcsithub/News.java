
package my.unimas.a54440siswa.fcsithub;
public class News {

    private String Post;
    private String PostUserName ;
    private String PostTime ;
    private String PostDate ;
    private String NewsID ;
    private String UserID ;

    public News() {
    }

    public News(String post, String postusername, String posttime, String postdate, String newsid, String userid) {
        Post = post;
        PostUserName = postusername;
        PostTime = posttime;
        PostDate = postdate;
        NewsID = newsid;
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

    public String getNewsID() {
        return NewsID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setName(String post) {
        Post = post;
    }

    public void setPostUserName(String postusername) {
        PostUserName = postusername;
    }

    public void setPostTime(String posttime) { PostTime = posttime; }

    public void setPostDate(String postdate) { PostDate = postdate; }

    public void setNewsID(String newsid) { PostDate = newsid; }

    public void setUserID(String userid) { PostDate = userid; }


  }