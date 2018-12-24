package model;

import java.util.Map;

public class Comments {
    public Comments(Map<Integer, CommentUser> commentUserMap) {
        this.commentUserMap = commentUserMap;
    }

    public Comments() {
    }

    public Map<Integer, CommentUser> getCommentUserMap() {
        return commentUserMap;
    }

    public void setCommentUserMap(Map<Integer, CommentUser> commentUserMap) {
        this.commentUserMap = commentUserMap;
    }

    private Map<Integer,CommentUser> commentUserMap;
}
