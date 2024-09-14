package com.example.autobike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.autobike.database.UserDBHelper;
import com.example.map.R;


public class MainActivity extends Activity {

    public static UserDBHelper mHelper;

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = UserDBHelper.getInstance(this);
        mHelper.OpenRead();
        mHelper.OpenWrite();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }

    // 调用Actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 关联activity.xml
        setContentView(R.layout.activity_main);
        // 关联用户名、密码和登录、注册按钮
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        Button loginButton = (Button) this.findViewById(R.id.LoginButton);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        CheckBox remember = (CheckBox) this.findViewById(R.id.remember);
        onStart();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException ex)
        {
            System.out.println("出现异常");
        }
        int k = mHelper.QueryRemember();
        if(k == 1){
            Intent intent = new Intent(MainActivity.this,MapApplication.class);
            startActivity(intent);
        }
        // 登录按钮监听器
        loginButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取用户名和密码
                        onStart();
                        String strUserName = userName.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        String password = mHelper.QueryPassword(strUserName);
                        // 判断如果用户名密码为正确则登录成功
                        if (strPassWord.equals(password)) {
                            Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(1000);
                            }catch (InterruptedException ex)
                            {
                                System.out.println("出现异常");
                            }
                            if(remember.isChecked()){
                                mHelper.remember(strUserName);
                            }
                            Intent intent = new Intent(MainActivity.this, QianboActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "请输入正确的用户名或密码！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        // 注册按钮监听器
        signUpButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到注册界面
                        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }
}

