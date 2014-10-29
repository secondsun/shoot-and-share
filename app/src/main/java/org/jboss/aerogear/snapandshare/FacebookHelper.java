package org.jboss.aerogear.snapandshare;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzModule;

import java.net.URL;
import java.util.Arrays;

/**
 * Created by summers on 10/28/14.
 */
public class FacebookHelper {


    private static final String AUTHZ_ENDPOINT = "www.facebook.com/dialog/oauth";
    private static final String AUTHZ_TOKEN_ENDPOINT = "graph.facebook.com/oauth/access_token";
    private static final String AUTHZ_ACCOOUNT_ID = "facebook-token";
    private static final String AUTHZ_CLIENT_ID = "310605179126239";
    private static final String AUTHZ_CLIENT_SECRET = "f6053a175cd0f3ce8de64c78ca974f82";
    private static final String AUTHZ_REDIRECT_URL = "https://localhost/";

    public static void connect(final Activity activity) {
        try {
            final OAuth2AuthzModule authzModule = (OAuth2AuthzModule) AuthorizationManager.config("FacebookOAuth", OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL("https://"))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(AUTHZ_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setClientSecret(AUTHZ_CLIENT_SECRET)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .setRefreshEndpoint(AUTHZ_TOKEN_ENDPOINT)
                    .setAdditionalAccessParams(Sets.newHashSet(Pair.create("response_type", "code")))
                    .setScopes(Arrays.asList("photo_upload, publish_actions"))
                    .asModule();

            authzModule.requestAccess(activity, new Callback<String>() {
                @Override
                public void onSuccess(String token) {
                    Log.d("TOKEN ++ ", token);
                    Toast.makeText(activity, token, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
