package model;

public class CommentUser {

    private String idOfUser;
    private String idOfProduct;
    private String comment;

    public CommentUser() {
    }

    public CommentUser(String idOfProduct) {
        this.idOfProduct = idOfProduct;
    }

    public String getIdOfUser() {
        return idOfUser;
    }

    public void setIdOfUser(String idOfUser) {
        this.idOfUser = idOfUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public CommentUser(String idOfUser, String comment, String idOfProduct) {
        this.idOfUser = idOfUser;
        this.comment = comment;
        this.idOfProduct = idOfProduct;
    }

    public String getIdOfProduct() {
        return idOfProduct;
    }

    public void setIdOfProduct(String idOfProduct) {
        this.idOfProduct = idOfProduct;
    }




}
