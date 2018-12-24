package model;

public class CommentUser {
    public CommentUser() {
    }

    public int getIdOfUser() {
        return idOfUser;
    }

    public void setIdOfUser(int idOfUser) {
        this.idOfUser = idOfUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private int idOfUser;
    private String comment;

    public CommentUser(int idOfUser, String comment) {
        this.idOfUser = idOfUser;
        this.comment = comment;
    }
}
