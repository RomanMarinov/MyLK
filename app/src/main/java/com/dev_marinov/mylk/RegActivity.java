package com.dev_marinov.mylk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class RegActivity extends AppCompatActivity {

    EditText edt_login_post, edt_fio;
    Button bt_reg;
    TextView tv_next;
    TextView tv_blink;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_login_post = findViewById(R.id.edt_login_post);
        edt_fio = findViewById(R.id.edt_fio);

        bt_reg = findViewById(R.id.bt_reg);
        tv_next = findViewById(R.id.tv_next);
        tv_blink = findViewById(R.id.tv_blink);
        tv_blink.setVisibility(View.GONE);


        ConstraintLayout constraintLayout = findViewById(R.id.cl_main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Gson json = new Gson();
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("cmd", "registration");
//                hashMap.put("login_post", edt_login_post.getText().toString());
//                hashMap.put("fio", edt_fio.getText().toString());

              runOnUiThread(new Runnable() {
              @Override
              public void run() {
              ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
              progressBar.setVisibility(ProgressBar.VISIBLE);
// запускаем длительную операцию
//  progressBar.setVisibility(ProgressBar.INVISIBLE);
              }
      });

                RequestParams requestParams = new RequestParams();
                requestParams.put("cmd", "registration");
                requestParams.put("login_post", edt_login_post.getText().toString());
                requestParams.put("fio", edt_fio.getText().toString());

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.post("https://dev-marinov.ru/server/mylk_server/srRegistration.php", requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("REGACT", "onFailure-respone-" + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                   Log.e("Пришел","ответ: "+responseString);
                        JSONObject jsonObject_1 = null;
                        try {
                            jsonObject_1 = new JSONObject(responseString);
                            String cmd = jsonObject_1.getString("cmd");
                                Log.e("REGACT ","-respone-" + responseString);

                            if (cmd.equals("reg")) { // успешно записан
                                tv_blink.setVisibility(View.GONE);
                                Intent intent = new Intent(RegActivity.this, EnterActivity.class);
                                startActivity(intent);

                            }
                            if (cmd.equals("error")) { // не записано
                                tv_blink.setText("заполните все строки");
                                tv_blink.setVisibility(View.VISIBLE);
                                anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.blink);
                                tv_blink.startAnimation(anim);
                            }
                            if (cmd.equals("error_reg")) { // не записано
                                tv_blink.setText("такой логин уже есть");
                                tv_blink.setVisibility(View.VISIBLE);
                                anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.blink);
                                tv_blink.startAnimation(anim);
                            }
                        }
                        catch (JSONException e) {
                            Log.e("Ошибка при разборе","Отчет: "+e);
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegActivity.this, EnterActivity.class);
                startActivity(intent);
            }
        });
    }
}