package org.jboss.aerogear.snapandshare.callback;

import android.animation.ObjectAnimator;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.snapandshare.MainActivity;

public final class Callbacks {

    private Callbacks() {
    }

    public static Callback<String> connect(final MainActivity activity, final ObjectAnimator animator, final int button, final int active, final int inactive) {
        return new Callback() {

            @Override
            public void onSuccess(Object o) {
                animator.end();
                activity.flipImage(button, active);
            }

            @Override
            public void onFailure(Exception e) {
                animator.end();
                activity.flipImage(button, inactive);
            }
        };
    }

}
