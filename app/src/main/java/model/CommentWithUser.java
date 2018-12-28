package model;

public class CommentWithUser {
    private String comments;

    public CommentWithUser(String comments, int idOfUser) {
        this.comments = comments;
        this.idOfUser = idOfUser;
    }

    public CommentWithUser() {
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getIdOfUser() {
        return idOfUser;
    }

    public void setIdOfUser(int idOfUser) {
        this.idOfUser = idOfUser;
    }

    private int idOfUser;
}
