package org.jboss.aerogear.snapandshare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.snapandshare.util.FacebookHelper;
import org.jboss.aerogear.snapandshare.util.GooglePlusHelper;
import org.jboss.aerogear.snapandshare.util.KeycloakHelper;

import java.io.File;


public class PhotoActivity extends ActionBarActivity {

    public static final String IMAGE_PATH = "PhotoActivity.IMAGE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
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
        if (GooglePlusHelper.isConnected()) {
            View button = findViewById(R.id.google_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = new ProgressDialog(PhotoActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setTitle("Uploading");
                    dialog.show();
                    GooglePlusHelper.upload(new File(getIntent().getStringExtra(IMAGE_PATH)), new Callback() {
                        @Override
                        public void onSuccess(Object o) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }, PhotoActivity.this);
                }
            });
        }

        if (FacebookHelper.isConnected()) {
            View button = findViewById(R.id.facebook_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = new ProgressDialog(PhotoActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setTitle("Uploading");
                    dialog.show();
                    FacebookHelper.upload(new File(getIntent().getStringExtra(IMAGE_PATH)), new Callback() {
                        @Override
                        public void onSuccess(Object o) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }, PhotoActivity.this);
                }
            });
        }

        if (KeycloakHelper.isConnected()) {
            View button = findViewById(R.id.keycloak_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = new ProgressDialog(PhotoActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setTitle("Uploading");
                    dialog.show();
                    KeycloakHelper.upload(new File(getIntent().getStringExtra(IMAGE_PATH)), new Callback() {
                        @Override
                        public void onSuccess(Object o) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            dialog.dismiss();
                            Toast.makeText(PhotoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }, PhotoActivity.this);
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        ImageView imageView = (ImageView) findViewById(R.id.image);
        String imagePath = getIntent().getStringExtra(IMAGE_PATH);
        Picasso.with(this).load("file:"+imagePath).fit().centerInside().into(imageView);
    }
}
