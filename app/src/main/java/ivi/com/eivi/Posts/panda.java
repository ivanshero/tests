package ivi.com.eivi.Posts;



public class panda {

    private  String title , image,username , pandaid , userprofile , Comment , from_panda , uid;
    public panda(){



    }

    public panda(String title, String image, String username , String pandaid, String userprofile, String Comment,String uid , String from_panda ){

        this.title = title;

        this.from_panda = from_panda;

        this.uid = uid;

        this.image = image;

        this.pandaid = pandaid;

        this.username = username;

        this.userprofile = userprofile;

        this.Comment = Comment;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(String userprofile) {
        this.userprofile = userprofile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

}



