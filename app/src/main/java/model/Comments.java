package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comments {

    public Comments(Map<String, List<CommentUser>> commentUserMap) {
        this.commentUserMap = commentUserMap;
    }

    public Comments(List<CommentUser> commentUserList)
    {
        this.commentUserList = commentUserList;
    }

    public Comments() {
    }

    public Map<String, List<CommentUser>> getCommentUserMap() {
        return commentUserMap;
    }

    public void setCommentUserMap(Map<String, List<CommentUser>> commentUserMap) {
        this.commentUserMap = commentUserMap;
    }

    private Map<String,List<CommentUser>> commentUserMap;
    private List<CommentUser> commentUserList;
}
