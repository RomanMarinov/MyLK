package com.dev_marinov.mylk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PromoActivity extends AppCompatActivity {

    TextView tv_count_scores, tv_link,tv_link_head;

    int i;
    Random random = new Random();
    int count_thread = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        tv_count_scores = findViewById(R.id.tv_count_scores);
        tv_link = findViewById(R.id.tv_link);
        tv_link_head = findViewById(R.id.tv_link_head);

        int random_int = random.nextInt(1000);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConstraintLayout constraintLayout = findViewById(R.id.cl_main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        // первый поток для счетчика
        new Thread(new Runnable() {
            @Override
            public void run() {
                //boolean b = true;
                i = -1;
                while (true)
                {
                    count_thread++;
                    i++;
                    if(i == random_int)
                    {
                        break;
                    }

                try {
                    Thread.sleep(6);
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                }
            }
        }).start();

                // второй поток для вывода
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                     //   boolean b = true;
                        while (true)
                        {

                            if(i==random_int)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       tv_link.setText("http://dev-marinov.ru/server/mylk_server/akzia/index.php");
                                       tv_link_head.setText("ВАША ССЫЛКА ДЛЯ АКЦИИ");
                                    }
                                });
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_count_scores.setText("" + i);
                                    }
                                });
                            }


                        try {
                            Thread.sleep(6);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        }
                    }
                }).start();
    }
}