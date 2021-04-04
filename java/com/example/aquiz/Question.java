package com.example.aquiz;

import android.text.style.QuoteSpan;

public class Question {
    private int qId;
    private boolean ans;
    private String userans;
    private boolean attended;

    Question(int id, boolean a, boolean st){
        this.qId = id;
        this.ans = a;
        this.userans = "not attempted";
        this.attended = st;
    }

    public int getQues(Question q){
        return q.qId;
    }

    public boolean getAns(Question q){
        return q.ans;
    }

    public String getUserns(Question q){
        return q.userans;
    }

    public void setUserans(Question q, boolean a){
        if (a) q.userans = "true";
        else q.userans = "false";
    }

    public boolean getStatus(Question q){
        return q.attended;
    }

    public void setStatus(Question q){
        q.attended = true;
    }

}
