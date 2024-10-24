package com.example.autobike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.autobike.database.UserDBHelper;
import com.example.autobike.enity.user;
import com.example.map.R;

public class SignUpActivity extends Activity {

    public UserDBHelper mHelper;

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
        //关联activity_register.xml
        setContentView(R.layout.activity_sign_up);
        // 关联用户名、密码、确认密码、邮箱和注册、返回登录按钮
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        EditText passWordAgain = (EditText) this.findViewById(R.id.PassWordAgainEdit);
        EditText email = (EditText) this.findViewById(R.id.EmailEdit);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        Button backLoginButton = (Button) this.findViewById(R.id.BackLoginButton);
        // 立即注册按钮监听器
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStart();
                        String strUserName = userName.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        String strPassWordAgain = passWordAgain.getText().toString().trim();
                        String strE_mail = email.getText().toString().trim();
                        user user = null;
                        //注册格式粗检
                        if (strUserName.length() > 10) {
                            Toast.makeText(SignUpActivity.this, "用户名长度必须小于10！", Toast.LENGTH_SHORT).show();
                        } else if (strUserName.length() < 4) {
                            Toast.makeText(SignUpActivity.this, "用户名长度必须大于4！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() > 16) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须小于16！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() < 6) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须大于6！", Toast.LENGTH_SHORT).show();
                        } else if (!strPassWord.equals(strPassWordAgain)) {
                            Toast.makeText(SignUpActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                        } else if (!strE_mail.contains("@")) {
                            Toast.makeText(SignUpActivity.this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                        } else {
                            user = new user(strUserName,strPassWord,strE_mail,0);
                             if (mHelper.insert(user) > 0){
                                Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
        );
        // 返回登录按钮监听器
        backLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }


}

