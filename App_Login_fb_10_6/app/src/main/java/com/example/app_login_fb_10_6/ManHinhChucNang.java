package com.example.app_login_fb_10_6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ManHinhChucNang extends AppCompatActivity {
    EditText edittitle,editdescription,editurl;
    Button btnsharelink,btnsharevideo,btnpickvideo,btnshareimage;
    ImageView imghinhanh;
    VideoView videoView;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    public static int Select_Image = 1;
    public static int Pick_Video = 2;
    //Khai bao
    Bitmap bitmap;
    Uri selectvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chuc_nang);
        Anhxa();

        shareDialog=new ShareDialog(ManHinhChucNang.this);
        //Khi muốn xem gì thì nó sẽ mở hộp thoại lên cho chúng ta
        btnsharelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle(edittitle.getText().toString())
                            .setContentDescription(editdescription.getText().toString())
                            .setContentUrl(Uri.parse(editurl.getText().toString()))
                            .build();
                }
                shareDialog.show(shareLinkContent);

            }
        });
        imghinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Select_Image);
            }
        });
        btnshareimage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });
        btnpickvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent,Pick_Video);
            }
        });
        btnsharevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareVideo shareVideo = null;
                shareVideo = new ShareVideo.Builder()
                        .setLocalUrl(selectvideo)
                        .build();
                ShareVideoContent content = new ShareVideoContent.Builder()
                        .setVideo(shareVideo)
                        .build();
                shareDialog.show(content);
                videoView.stopPlayback();
            }
        });
    }

    private void Anhxa() {
        //khai bao va anh xa cho cac thuoc tinh
        edittitle = (EditText)findViewById(R.id.edittexttitle);
        editdescription = (EditText)findViewById(R.id.edittextdescription);
        editurl = (EditText)findViewById(R.id.edittexturl);
        btnsharelink = (Button)findViewById(R.id.buttonsharelink);
        btnshareimage = (Button)findViewById(R.id.buttonshareimage);
        btnpickvideo = (Button)findViewById(R.id.buttonpickvideo);
        btnsharevideo = (Button)findViewById(R.id.buttonshareVideo);
        imghinhanh = (ImageView)findViewById(R.id.imagehinh);
        videoView = (VideoView)findViewById(R.id.videoview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==Select_Image &&resultCode == RESULT_OK){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imghinhanh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==Pick_Video&&resultCode==RESULT_OK){
            selectvideo = data.getData();
            videoView.setVideoURI(selectvideo);
            videoView.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}