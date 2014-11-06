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
