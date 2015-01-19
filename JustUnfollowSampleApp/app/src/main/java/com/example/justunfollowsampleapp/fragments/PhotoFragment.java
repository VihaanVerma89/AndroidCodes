package com.example.justunfollowsampleapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.justunfollowsampleapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by vihaan on 19/1/15.
 */
public class PhotoFragment extends Fragment implements View.OnClickListener {


    private Uri mImageCaptureUri;
    private Uri mImageUri;

    private LinearLayout mPhotoOptionsLinearLayout;


    private ImageView mImageView, mCameraImageView, mGalleryImageView;

    private final int GALLERY_REQ_CODE = 9999, CAMERA_REQ_CODE = 8888;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_photo, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhotoOptionsLinearLayout = (LinearLayout)view.findViewById(R.id.photoOptionsLinearLayout);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mCameraImageView = (ImageView) view.findViewById(R.id.cameraImageView);
        mGalleryImageView = (ImageView) view.findViewById(R.id.galleryImageView);


        mCameraImageView.setOnClickListener(this);
        mGalleryImageView.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            switch(requestCode)
            {

                case CAMERA_REQ_CODE:
                    if (mImageCaptureUri != null) {
                        mImageView.setImageDrawable(Drawable.createFromPath(getRealPathFromURI(mImageCaptureUri, getActivity())));
                    }
                    break;

                case GALLERY_REQ_CODE:
                    mImageUri = data.getData();
                    setImageView(mImageUri);
                    break;
            }
        }

    }

    private void setImageView(Uri uri)
    {
        mImageView.setVisibility(View.VISIBLE);
//        Picasso.with(getActivity()).load(uri).fit().centerInside().into(mImageView);
        Picasso.with(getActivity()).load(uri).resize(500,500).centerInside().into(mImageView);

//        mImageView.setImageURI(uri);

//        ImageView iv  = (ImageView)waypointListView.findViewById(R.id.waypoint_picker_photo);
////        Bitmap d = new BitmapDrawable(getActivity().getResources() , w.photo.getAbsolutePath()).getBitmap();
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
//        int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
//        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
//        mImageView.setImageBitmap(scaled);

        mPhotoOptionsLinearLayout.setVisibility(View.GONE);
    }

    private  String getRealPathFromURI(Uri contentUri, Activity activity) {
        if (contentUri != null) {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(contentUri,
                    proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }


    private void pickImageFromGallery()
    {
        Intent pickPhoto = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(pickPhoto,
                    GALLERY_REQ_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(),
                    getString(R.string.no_gallery_txt),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void captureImage()
    {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(
                android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            intent.putExtra("return-data", false);
            startActivityForResult(intent,
                    CAMERA_REQ_CODE);
        } catch (Exception e) {

            Toast.makeText(getActivity(),
                    getString(R.string.no_camera),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_contact_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        switch (v.getId()) {
            case R.id.cameraImageView:
                captureImage();
                break;

            case R.id.galleryImageView:
                pickImageFromGallery();
                break;
        }
    }

    public boolean isImagePresent()
    {
        return mImageView.getVisibility() == View.VISIBLE? true: false;
    }

    public Uri getImageUri()
    {
        return mImageUri;
    }
}
