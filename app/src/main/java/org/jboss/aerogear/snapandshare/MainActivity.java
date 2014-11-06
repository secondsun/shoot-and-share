package org.jboss.aerogear.snapandshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageButton;

import org.jboss.aerogear.snapandshare.adapter.FileAdapter;
import org.jboss.aerogear.snapandshare.callback.Callbacks;
import org.jboss.aerogear.snapandshare.util.FacebookHelper;
import org.jboss.aerogear.snapandshare.util.GooglePlusHelper;
import org.jboss.aerogear.snapandshare.util.KeycloakHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private static final int CAMERA = 0x87;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
    private FileAdapter fileAdaper;
    private GridView grid;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        fileAdaper = new FileAdapter(this);
        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAMERA);
            }
        });

        grid = (GridView) findViewById(R.id.photo_grid);
        grid.setAdapter(fileAdaper);

        getSupportActionBar().setLogo(R.drawable.gear);
        getSupportActionBar().setIcon(R.drawable.gear);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA:
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent

                    grid.invalidate();
                    fileAdaper.notifyDataSetChanged();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }

            default:

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        View googleButton = findViewById(R.id.google_button);
        View facebookButton = findViewById(R.id.facebook_button);
        View keycloakButton = findViewById(R.id.keycloak_button);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator animator = beginWaitSpin(R.id.google_button);
                animator.start();
                GooglePlusHelper.connect(MainActivity.this, Callbacks.connect(MainActivity.this,
                        animator,
                        R.id.google_button,
                        R.drawable.google_active,
                        R.drawable.google_inactive));

            }
        });

        googleButton.setTag(new FlipTag(R.drawable.google_inactive, R.drawable.google_active));

        if (!GooglePlusHelper.isConnected()) {
            googleButton.setTag(new FlipTag(R.drawable.google_inactive, R.drawable.google_active));
        } else {
            googleButton.setTag(new FlipTag(R.drawable.google_active, R.drawable.google_inactive));
        }

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator animator = beginWaitSpin(R.id.facebook_button);
                animator.start();
                FacebookHelper.connect(MainActivity.this, Callbacks.connect(MainActivity.this,
                        animator,
                        R.id.facebook_button,
                        R.drawable.facebook_active,
                        R.drawable.facebook_inactive));
            }
        });

        facebookButton.setTag(new FlipTag(R.drawable.facebook_inactive, R.drawable.facebook_active));

        if (!FacebookHelper.isConnected()) {
            facebookButton.setTag(new FlipTag(R.drawable.facebook_inactive, R.drawable.facebook_active));
        } else {
            facebookButton.setTag(new FlipTag(R.drawable.facebook_active, R.drawable.facebook_inactive));
        }

        keycloakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator animator = beginWaitSpin(R.id.keycloak_button);
                animator.start();
                KeycloakHelper.connect(MainActivity.this, Callbacks.connect(MainActivity.this,
                        animator,
                        R.id.keycloak_button,
                        R.drawable.keycloak_active,
                        R.drawable.keycloak_inactive));
            }
        });

        if (!KeycloakHelper.isConnected()) {
            keycloakButton.setTag(new FlipTag(R.drawable.keycloak_inactive, R.drawable.keycloak_active));
        } else {
            keycloakButton.setTag(new FlipTag(R.drawable.keycloak_active, R.drawable.keycloak_inactive));
        }


    }


    public void flipImage(int imageViewId, int destImage) {

        ImageButton imageButton = (ImageButton) findViewById(imageViewId);
        FlipTag flipTag = (FlipTag) imageButton.getTag();

        if (flipTag.from == destImage) {
            return;
        }

        ObjectAnimator flipOut = ObjectAnimator.ofFloat(imageButton, "rotationY", 0, 90);
        flipOut.setDuration(150);
        flipOut.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator swapImage = ObjectAnimator.ofInt(imageButton, "imageResource", flipTag.from, flipTag.to);
        swapImage.setDuration(0);

        ObjectAnimator flipIn = ObjectAnimator.ofFloat(imageButton, "rotationY", 90, 0);
        flipIn.setDuration(150);
        flipIn.setInterpolator(new DecelerateInterpolator());


        AnimatorSet set = new AnimatorSet();
        set.play(flipOut).before(swapImage).before(flipIn);
        set.start();
        flipTag.swap();

    }

    private ObjectAnimator beginWaitSpin(int buttonId) {
        ImageButton imageButton = (ImageButton) findViewById(buttonId);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageButton, "rotation", 0, 360);
        rotate.setDuration(900);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        return rotate;
    }

    private static class FlipTag {
        int from;
        int to;

        private FlipTag(int from, int to) {
            this.from = from;
            this.to = to;
        }

        void swap() {
            int temp = from;
            from = to;
            to = temp;
        }

    }

}
