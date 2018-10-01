package com.joey.devilfish.ui.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.joey.devilfish.R;
import com.joey.devilfish.utils.CommonUtils;
import com.joey.devilfish.utils.ExtendUtils;
import com.joey.devilfish.utils.FileUtils;
import com.joey.devilfish.utils.PromptUtils;
import com.joey.devilfish.utils.StringUtils;

import java.io.File;

/**
 * 需要上传图片的activity
 * Date: 15/12/14
 *
 * @author xusheng
 */
public abstract class BaseUploadPictureActivity extends BaseActivity {

    private static final int PIC_FROM_LOCAL_CODE = 10000;

    private static final int PIC_FROM_CAMERA_CODE = 10001;

    private static final String PICTURE_FILE_SUFFIX = ".jpg";

    protected String mCapturedFileDir;

    private String mCapturedFilePath = null;

    public abstract void selectPhotoSuccess(File file);

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        ActivityCompat.requestPermissions(BaseUploadPictureActivity.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public void selectPhoto() {
        AlertDialog.Builder builder = PromptUtils.getInstance().getAlertDialog(BaseUploadPictureActivity.this, true);
        builder.setTitle(getString(R.string.select))
                .setItems(getResources().getStringArray(R.array.select_icon_location),
                        new OnDialogClickListener()).show();
    }

    private class OnDialogClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    Intent intentFromLocalImage = new Intent();
                    intentFromLocalImage.setType("image/*");
                    intentFromLocalImage.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intentFromLocalImage, PIC_FROM_LOCAL_CODE);
                    break;
                case 1:
                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (!FileUtils.checkAndCreateFolder(mCapturedFileDir)) {
                            return;
                        }
                        mCapturedFilePath = mCapturedFileDir + String.valueOf(System.currentTimeMillis()) + PICTURE_FILE_SUFFIX;
                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCapturedFilePath)));
                        startActivityForResult(intentFromCapture, PIC_FROM_CAMERA_CODE);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.camera_sdcard_not_found), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case PIC_FROM_LOCAL_CODE:
                if (!FileUtils.checkAndCreateFolder(mCapturedFileDir)) {
                    return;
                }

                if (null == data.getData()) {
                    return;
                }
                File fileSelected = CommonUtils.uri2File(this, data.getData());

                if (null == fileSelected) {
                    return;
                }

                String filePath = fileSelected.getAbsolutePath();
                if (StringUtils.getInstance().isNullOrEmpty(filePath)) {
                    return;
                }

                mCapturedFilePath = mCapturedFileDir + String.valueOf(System.currentTimeMillis()) + PICTURE_FILE_SUFFIX;
                CommonUtils.compressBitmap(fileSelected.getAbsolutePath(), mCapturedFilePath, 1024, -1);
                selectPhotoSuccess(new File(mCapturedFilePath));
                break;
            case PIC_FROM_CAMERA_CODE:
                if (!FileUtils.checkAndCreateFolder(mCapturedFileDir)) {
                    return;
                }
                CommonUtils.compressBitmap(mCapturedFilePath, mCapturedFilePath, 1024, -1);
                selectPhotoSuccess(new File(mCapturedFilePath));
                ExtendUtils.getInstance().updateAlbum(BaseUploadPictureActivity.this, new File(mCapturedFilePath));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
