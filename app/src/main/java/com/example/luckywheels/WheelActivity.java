package com.example.luckywheels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WheelActivity extends AppCompatActivity {

    private static final String[] sectors ={
      "32 red", "15 black", "19 red", "4 black",
      "21 red", "2 black", "25 red", "17 black",
      "34 red", "6 black", "27 red", "13 black",
      "36 red", "11 black", "30 red", "8 black",
      "23 red", "10 black", "5 red", "24 black",
      "16 red", "33 black", "1 red", "20 black",
      "14 red", "31 black", "9 red", "22 black",
      "18 red", "29 black", "7 red", "28 black",
      "12 red", "35 black", "3 red", "26 black",
      "zero"};

    @BindView(R.id.btn_spin)
    Button spinButton;
    @BindView(R.id.tv_result)
    TextView resultTV;
    @BindView(R.id.iv_wheel)
    ImageView wheelIV;

    //we create a random instance to make our wheel spin randomly
    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;

    //we have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector,
    //wee divide by two to have a half sector

    private static final float HALF_SECTOR = 360f / 37f / 2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_spin)
    public void spin(View view){
        degreeOld = degree % 360;
        //we calculate random angel for rotation of our wheel
        degree = RANDOM.nextInt(360) + 720;
        //rotation effect o the center of the wheel
        RotateAnimation rotateAnimation =  new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(7200);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start
                resultTV.setText("");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //we display the correct sector pointed by the triangle at the end of the rotation
                resultTV.setText(getSector(360 - (degree % 360)));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //we start the animation
        wheelIV.startAnimation(rotateAnimation);
    }

    private String getSector(int degrees) {
         int i = 0;
         String text = null;
         do{
             //start and end of each sector on the wheel
             float start = HALF_SECTOR * (i * 2 + 1);
             float end = HALF_SECTOR * (i * 2 + 3);
             if(degrees >= start && degrees < end){
                 // degrees are in the [start;end[
                 //so text is equal to sectors[i];
                 text = sectors[sectors.length-1];
             }
             i++;
         }while (text == null && i < sectors.length);
         return text;
    }
}