package org.jboss.aerogear.snapandshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jboss.aerogear.android.Callback;

import java.io.File;
import java.io.FileFilter;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                GooglePlusHelper.connect(MainActivity.this, new Callback() {

                    @Override
                    public void onSuccess(Object o) {
                        animator.end();
                        flipImage(R.id.google_button, R.drawable.google_active);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        animator.end();
                        flipImage(R.id.google_button, R.drawable.google_inactive);
                    }
                });

            }
        });

        googleButton.setTag(new FlipTag(R.drawable.google_inactive, R.drawable.google_active));

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipImage(R.id.facebook_button, R.drawable.facebook_active);
            }
        });

        facebookButton.setTag(new FlipTag(R.drawable.facebook_inactive, R.drawable.facebook_active));

        keycloakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipImage(R.id.keycloak_button, R.drawable.keycloak_active);
            }
        });

        keycloakButton.setTag(new FlipTag(R.drawable.keycloak_inactive, R.drawable.keycloak_active));


    }


    private void flipImage(int imageViewId, int destImage) {

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

    ObjectAnimator beginWaitSpin(int buttonId) {
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

    public static class FileAdapter extends BaseAdapter {

        final static File imageDirectory;

        static {
            imageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!imageDirectory.exists()) {
                if (!imageDirectory.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    throw new RuntimeException("failed to create directory");
                }
            }
        }

        private final Context context;

        public FileAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageDirectory.listFiles().length;
        }

        @Override
        public File getItem(int position) {
            return imageDirectory.listFiles()[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ImageView image;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.image_card, null);
            }
            File file = getItem(position);

            Picasso.with(context)
                    .load(file)
                    .centerInside()
                    .resizeDimen(R.dimen.image_card_width, R.dimen.image_card_height)
                    .into((ImageView) view.findViewById(R.id.image));

            return view;
        }
    }

}
