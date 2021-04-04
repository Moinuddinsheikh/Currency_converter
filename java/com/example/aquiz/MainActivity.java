package com.example.aquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button yesButton, noButton, prevButton, nextButton, startbtn, resultbtn, resultViewcancelbtn;
  private TextView quesText, resultText, countText, resultViewtext;
  private LinearLayout startlyt, questionlyt;
  private ScrollView resultScrollview;
  private TableLayout resultTable;
  private int marks = 0, quesInd = 0, seenresult = 0;

  CountDownTimer cdt;

  Question[] quesArr = new Question[]{
          new Question(R.string.q1, true, false),
          new Question(R.string.q9, true, false),
          new Question(R.string.q3, false, false),
          new Question(R.string.q4, true, false),
          new Question(R.string.q5, false, false),
          new Question(R.string.q6, false, false),
          new Question(R.string.q7, true, false),
          new Question(R.string.q8, false, false),
          new Question(R.string.q2, true, false),
          new Question(R.string.q10, true, false),
          new Question(R.string.q11, true, false),
          new Question(R.string.q12, false, false),
          new Question(R.string.q13, false, false)
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    startlyt = findViewById(R.id.startlyt);
    questionlyt = findViewById(R.id.questionlyt);
    yesButton = findViewById(R.id.yesButton);
    noButton = findViewById(R.id.noButton);
    prevButton = findViewById(R.id.prevButton);
    nextButton = findViewById(R.id.nextButton);
    startbtn = findViewById(R.id.startbtn);
    resultbtn = findViewById(R.id.resultbtn);
    quesText = findViewById(R.id.quesText);
    resultText = findViewById(R.id.resultText);
    countText = findViewById(R.id.countText);
    resultViewtext = findViewById(R.id.resultViewtext);
    resultScrollview = findViewById(R.id.resultScrollview);
    resultTable = findViewById(R.id.resultTable);
    resultViewcancelbtn = findViewById(R.id.resultViewcancelbtn);

    yesButton.setOnClickListener(this);
    noButton.setOnClickListener(this);
    prevButton.setOnClickListener(this);
    nextButton.setOnClickListener(this);
    startbtn.setOnClickListener(this);
    resultbtn.setOnClickListener(this);
    resultViewcancelbtn.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.startbtn:
        String ques = getResources().getString(quesArr[quesInd].getQues(quesArr[quesInd]));
        quesText.setText("Q1. "+ques);
        startlyt.setVisibility(View.GONE);
        questionlyt.setVisibility(View.VISIBLE);
        timer();
        break;

      case R.id.yesButton:
        check(true);
        if (quesInd < 12) {
          countText.setVisibility(View.VISIBLE);
        }
        break;

      case R.id.noButton:
        check(false);
        if (quesInd < 12) {
          countText.setVisibility(View.VISIBLE);
        }
        break;

      case R.id.prevButton:
        update(false);
        cdt.cancel();
        countText.setVisibility(View.GONE);
        break;

      case R.id.nextButton:
        update(true);
        if (quesInd < 12) {
          countText.setVisibility(View.VISIBLE);
        }
        cdt.cancel();
        cdt.start();
        break;

      case R.id.resultbtn:
        if ( seenresult == 0) {
            displayResult();
            seenresult = 1;
        }
        questionlyt.setVisibility(View.GONE);
        resultScrollview.setVisibility(View.VISIBLE);
        break;

      case R.id.resultViewcancelbtn:
        resultScrollview.setVisibility(View.GONE);
        questionlyt.setVisibility(View.VISIBLE);
        break;
    }
  }

  public void timer(){
    cdt = new CountDownTimer(10000, 1000) {

      @Override
      public void onTick(long millisUntilFinished) {
        int c = (int) millisUntilFinished/1000 ;
        countText.setText("seconds left: " + c);
      }

      @Override
      public void onFinish() {
        if(quesInd != 12) {
          update(true);
        }
        else{
          resultText.setText("You scored: " + marks + " out of 13 points.");
          countText.setVisibility(View.GONE);
          resultbtn.setVisibility(View.VISIBLE);
        }
      }
    }.start();

  }

  private void check(boolean btn) {
    boolean ans = quesArr[quesInd].getAns(quesArr[quesInd]);
    boolean status = quesArr[quesInd].getStatus(quesArr[quesInd]);

    if (status == true) {
      Toast.makeText(this, "Already attempted question!", Toast.LENGTH_SHORT).show();
    } else {
      if (btn == ans) {
        Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        quesArr[quesInd].setUserans(quesArr[quesInd], true);
        quesArr[quesInd].setStatus(quesArr[quesInd]);
        marks++;
        update(true);
      } else {
        Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
        quesArr[quesInd].setUserans(quesArr[quesInd], false);
        quesArr[quesInd].setStatus(quesArr[quesInd]);
        update(true);
      }
    }
  }

  private void update(boolean ctxt) {
    if (ctxt) {
      if (quesInd + 1 < 13) {
        quesInd++;
        String ques = getResources().getString(quesArr[quesInd].getQues(quesArr[quesInd]));
        int no = quesInd + 1;
        quesText.setText("Q" + no + ". " + ques);
        cdt.start();
      } else {
        resultText.setText("You scored: " + marks + " out of 13 points.");
        countText.setVisibility(View.GONE);
        resultbtn.setVisibility(View.VISIBLE);
      }
    } else {
      if (quesInd - 1 > -1) {
        quesInd--;
        String ques = getResources().getString(quesArr[quesInd].getQues(quesArr[quesInd]));
        int no = quesInd + 1;
        quesText.setText("Q" + no + ". "+ques);
      } else {
        Toast.makeText(this, "No more previous qustion!", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void displayResult(){
    resultViewtext.setText("Score: "+ marks +" / 13");
    TextView tv1, tv2, tv3;
    tv1 = new TextView(this);
    tv2 = new TextView(this);
    tv3 = new TextView(this);
    tv1.setTextColor(Color.parseColor("#ffffff"));
    tv2.setTextColor(Color.parseColor("#ffffff"));
    tv3.setTextColor(Color.parseColor("#ffffff"));
    tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    tv1.setTextSize(16);  tv2.setTextSize(16);  tv3.setTextSize(16);
    tv1.setPadding(13,0,13,0);
    tv2.setPadding(13,0,13,0);
    tv3.setPadding(13,0,13,0);
    TableRow tr = new TableRow(this);
    tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
    tr.setPadding(0,10,0,10);
    tr.setGravity(Gravity.CENTER);
    tv1.setText("Question. ");
    tr.addView(tv1);
    tv2.setText("Correct ans");
    tr.addView(tv2);
    tv3.setText("Your ans");
    tr.addView(tv3);
    resultTable.addView(tr);
    for (int i=0;i<13;i++){
      tr = new TableRow(this);
      tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
      tr.setGravity(Gravity.CENTER);
      tv1 = new TextView(this);
      tv2 = new TextView(this);
      tv3 = new TextView(this);
      tv1.setTextColor(Color.parseColor("#ffffff"));
      tv2.setTextColor(Color.parseColor("#ffffff"));
      tv3.setTextColor(Color.parseColor("#ffffff"));
      tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
      tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
      tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
      tv1.setPadding(13,0,13,0);
      tv2.setPadding(13,0,13,0);
      tv3.setPadding(13,0,13,0);
      tv1.setText("Q"+(i+1)+". ");
      tr.addView(tv1);
      if (quesArr[i].getAns(quesArr[i])) tv2.setText("true");
      else tv2.setText("false");
      tr.addView(tv2);
      tv3.setText(quesArr[i].getUserns(quesArr[i]));
      tr.addView(tv3);
      resultTable.addView(tr);

    }
  }
}
