package com.example.breezil.pixxo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.ActivityEditImageBinding;
import com.example.breezil.pixxo.model.ImagesModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import dagger.android.AndroidInjection;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static com.example.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

public class EditImageActivity extends AppCompatActivity {

    PhotoEditor mPhotoEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditorView.getSource().setImageResource(R.drawable.got);

    //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);

    //loading font from assest
        Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();

        mPhotoEditor.setBrushDrawingMode(true);
        mPhotoEditor.brushEraser();

    }





    private ImagesModel imagesModel(){
        Intent intent = this.getIntent();
        if(intent.hasExtra(SINGLE_PHOTO)){
            return intent.getParcelableExtra(SINGLE_PHOTO);
        }else{
            return null;
        }

    }
}
