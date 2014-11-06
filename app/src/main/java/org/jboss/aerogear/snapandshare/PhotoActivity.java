/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.snapandshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jboss.aerogear.snapandshare.service.UploadService;
import org.jboss.aerogear.snapandshare.util.FacebookHelper;
import org.jboss.aerogear.snapandshare.util.GooglePlusHelper;
import org.jboss.aerogear.snapandshare.util.KeycloakHelper;


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
                    Intent shareIntent = new Intent(PhotoActivity.this, UploadService.class);
                    shareIntent.putExtra(UploadService.FILE_URI, getIntent().getStringExtra(IMAGE_PATH));
                    shareIntent.putExtra(UploadService.PROVIDER, UploadService.PROVIDERS.GOOGLE.name());
                    startService(shareIntent);
                }
            });
        }

        if (FacebookHelper.isConnected()) {
            View button = findViewById(R.id.facebook_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(PhotoActivity.this, UploadService.class);
                    shareIntent.putExtra(UploadService.FILE_URI, getIntent().getStringExtra(IMAGE_PATH));
                    shareIntent.putExtra(UploadService.PROVIDER, UploadService.PROVIDERS.FACEBOOK.name());
                    startService(shareIntent);
                }
            });
        }

        if (KeycloakHelper.isConnected()) {
            View button = findViewById(R.id.keycloak_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(PhotoActivity.this, UploadService.class);
                    shareIntent.putExtra(UploadService.FILE_URI, getIntent().getStringExtra(IMAGE_PATH));
                    shareIntent.putExtra(UploadService.PROVIDER, UploadService.PROVIDERS.KEYCLOAK.name());
                    startService(shareIntent);
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
