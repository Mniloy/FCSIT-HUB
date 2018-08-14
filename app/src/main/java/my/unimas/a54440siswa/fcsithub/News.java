
package my.unimas.a54440siswa.fcsithub;
public class News {

    private String Post;
    private String PostUserName ;
  //  private String Number ;

    public News() {
    }

    public News(String post, String postusername) {
        Post = post;
        PostUserName = postusername;
     //   Number = number;
    }


    public String getPost() {
        return Post;
    }

    public String getPostUserName() {
        return PostUserName;
    }
/*
    public String getNumber() {
        return Number;
    }   */

    public void setName(String post) {
        Post = post;
    }

    public void setPostUserName(String postusername) {
        PostUserName = postusername;
    }

  }