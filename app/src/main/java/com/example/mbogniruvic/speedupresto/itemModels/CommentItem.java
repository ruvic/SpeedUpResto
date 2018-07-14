package com.example.mbogniruvic.speedupresto.itemModels;

public class CommentItem {
    public final String user;
    public final String comment;
    public final String date;
    public final String note;

    public CommentItem(String user, String comment, String date, String note) {
        this.user = user;
        this.comment = comment;
        this.date = date;
        this.note = note;
    }
}
