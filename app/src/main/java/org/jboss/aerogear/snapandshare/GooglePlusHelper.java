package org.jboss.aerogear.snapandshare;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.common.collect.ImmutableSet;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;

import java.net.URL;
import java.util.Arrays;

/**
 * Created by summers on 10/27/14.
 */
public class GooglePlusHelper {

    private static final String AUTHZ_URL = "https://accounts.google.com";
    private static final String AUTHZ_ENDPOINT = "/o/oauth2/auth";
    private static final String AUTHZ_TOKEN_ENDPOINT = "/o/oauth2/token";
    private static final String AUTHZ_ACCOOUNT_ID = "ag-authz";
    private static final String AUTHZ_CLIENT_ID = "37850448940-bqahddol8a694oadlqnj4n2el4kr5pqk.apps.googleusercontent.com";
    private static final String AUTHZ_CLIENT_SECRET = "6F6pPjHQT29qwvqvxzNv_Ks7";
    private static final String AUTHZ_REDIRECT_URL = "http://localhost";

    public static void connect(final Activity activity) {
        try {
            AuthzModule authzModule = AuthorizationManager.config("GoogleDriveAuthz", OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(AUTHZ_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setClientSecret(AUTHZ_CLIENT_SECRET)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .setScopes(Arrays.asList("https://www.googleapis.com/auth/drive"))
                    .setAdditionalAuthorizationParams(ImmutableSet.of(Pair.create("access_type", "offline")))
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
