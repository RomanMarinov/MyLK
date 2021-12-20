package com.dev_marinov.mylk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import cz.msebera.android.httpclient.Header;

public class EnterActivity extends AppCompatActivity {

    Button bt_enter;
    EditText edt_login, edt_password;
    Animation anim;

    TextView tv_blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConstraintLayout constraintLayout = findViewById(R.id.cl_main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        bt_enter = findViewById(R.id.bt_enter);
        edt_login = findViewById(R.id.edt_login);
        edt_password = findViewById(R.id.edt_password);
        tv_blink = findViewById(R.id.tv_blink);

        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                requestParams.put("cmd", "enter");
                requestParams.put("login_post", edt_login.getText().toString());
                requestParams.put("password", edt_password.getText().toString());

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.post("https://dev-marinov.ru/server/mylk_server/srEnter.php", requestParams,
                        new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                Log.e("ENTACT", "onFailure-respone-" + responseString);

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                Log.e("ENTACT", "onSuccess-respone-" + responseString);

                                JSONObject jsonObject_1 = null;
                                try {
                                    jsonObject_1 = new JSONObject(responseString);
                                    String cmd = jsonObject_1.getString("cmd");

                                    if (cmd.equals("enter")) {
                                        tv_blink.setVisibility(View.GONE);
                                        Intent intent = new Intent(EnterActivity.this, PromoActivity.class);
                                        startActivity(intent);
                                    }
                                    if (cmd.equals("error")) {
                                        tv_blink.setVisibility(View.VISIBLE);
                                        anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.blink);
                                        tv_blink.startAnimation(anim);
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
            });
    }
}
