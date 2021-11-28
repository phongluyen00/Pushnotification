package com.example.pushnotification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageResponse {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("isSuccess")
    @Expose
    private String isSuccess;
    @SerializedName("commentList")
    @Expose
    private CommentList commentList;

    public class CommentList {

        @SerializedName("$values")
        @Expose
        private List<Value> values = null;

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public CommentList getCommentList() {
        return commentList;
    }

    public void setCommentList(CommentList commentList) {
        this.commentList = commentList;
    }

    public static class Value {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("time")
        @Expose
        private String time;

        public Value(String username, String comment, String rate, String time) {
            this.username = username;
            this.comment = comment;
            this.rate = rate;
            this.time = time;
        }

        public String getUsername() {
            if (username == null) {
                username = "Ẩn danh";
            }
            return username;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
