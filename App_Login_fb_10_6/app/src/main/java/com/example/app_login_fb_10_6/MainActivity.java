package com.example.app_login_fb_10_6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    Button btndangxuat,btnchucnang;
    TextView txtname,txtemail,txtfirstname;
    String email,name,firstname;
    CallbackManager callbackManager;
    //khi chúng ta  gửi lên server một thông điệp thì server sẽ gửi về cho chúng ta một lời nhắn
     //thông qua callbackmanager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        Anhxa();
        btnchucnang.setVisibility(View.INVISIBLE);
        btndangxuat.setVisibility(View.INVISIBLE);
        txtemail.setVisibility(View.INVISIBLE);
        txtname.setVisibility(View.INVISIBLE);
        txtfirstname.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        setLogin_Button();//dang nhap
        setLogout_Button();//dang xuat
        ChuyenManHinh();
    }

    private void ChuyenManHinh() {
        btnchucnang.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ManHinhChucNang.class);
                startActivity(intent);
            }
        });
    }

    private void setLogout_Button() {
    btndangxuat.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            LoginManager.getInstance().logOut();//cho logoutra ngoài
            btndangxuat.setVisibility(View.INVISIBLE);//cho nút mờ đi
            btnchucnang.setVisibility(View.INVISIBLE);
            txtemail.setVisibility(View.INVISIBLE);
            txtname.setVisibility(View.INVISIBLE);
            txtfirstname.setVisibility(View.INVISIBLE);
            txtemail.setText("");
            txtname.setText("");
            txtfirstname.setText("");
            profilePictureView.setProfileId(null);
            loginButton.setVisibility(View.VISIBLE);//hien login de dang nhap
        }
    });
    }

    private void setLogin_Button(){
        //callbackmanager : loai tin nhan tu server tra ve
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            //sau khi dang nhap thanh cong
            public void onSuccess(LoginResult loginResult) {

                loginButton.setVisibility(View.INVISIBLE);
                btnchucnang.setVisibility(View.VISIBLE);
                btndangxuat.setVisibility(View.VISIBLE);
                txtemail.setVisibility(View.VISIBLE);
                txtname.setVisibility(View.VISIBLE);
                txtfirstname.setVisibility(View.VISIBLE);
                result();
                //khi login nó sẽ thực hiện mở các textview trước rồi mới lấy dữ liệu về

            }

            @Override
            public void onCancel() {

            }

            @Override
            //sau khi dang nhap that bai
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            //accesstoken giúp chứng thực việc đăng nhập và đăng xuất
            //khi server nhận được request chúng ta gửi lên và nó sẽ trả về  thông qua JsonObject

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
            Log.d("JSON",response.getJSONObject().toString());
                try{
                    email = object.getString("email");
                    name = object.getString("name");
                    firstname = object.getString("first_name");

                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    txtemail.setText(email);
                    txtname.setText(name);
                    txtfirstname.setText(firstname);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
                //la ham giup ta gui len server
    }

    public void Anhxa(){
        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        btnchucnang = (Button)findViewById(R.id.buttonchucnang);
        btndangxuat = (Button)findViewById(R.id.buttondangxuat);
        txtemail = (TextView)findViewById(R.id.textviewemail);
        txtname = (TextView)findViewById(R.id.textviewname);
        txtfirstname = (TextView)findViewById(R.id.textviewfirstname);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        //cho chúng ta đăng nhập lại

        LoginManager.getInstance().logOut();
        super.onStart();
    }
}
