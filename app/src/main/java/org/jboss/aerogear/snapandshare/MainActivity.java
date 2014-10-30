package org.jboss.aerogear.snapandshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import org.jboss.aerogear.android.Callback;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

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
                GooglePlusHelper.connect(MainActivity.this, new Callback(){

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


    private void flipImage(int imageViewId, int destImage){

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

    private static class FlipTag{
         int from;
         int to;

        private FlipTag(int from, int to) {
            this.from = from;
            this.to = to;
        }

        void swap(){
            int temp = from;
            from = to;
            to = temp;
        }

    }

}
