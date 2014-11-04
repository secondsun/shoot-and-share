package org.jboss.aerogear.snapandshare.util;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.common.collect.ImmutableSet;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuthWebViewDialog;

import java.net.URL;
import java.util.Arrays;

/**
 * Created by summers on 10/27/14.
 */
public class GooglePlusHelper {

    private static final String AUTHZ_URL = "https://accounts.google.com";
    private static final String AUTHZ_ENDPOINT = "/o/oauth2/auth";
    private static final String AUTHZ_TOKEN_ENDPOINT = "/o/oauth2/token";
    private static final String AUTHZ_ACCOOUNT_ID = "google-token";
    private static final String AUTHZ_CLIENT_ID = "374822310857.apps.googleusercontent.com";
    private static final String AUTHZ_CLIENT_SECRET = "brGLaQh_KRm-SvmXz2kYGASc";
    private static final String AUTHZ_REDIRECT_URL = "http://localhost";

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final AuthzModule authzModule = AuthorizationManager.config("GoogleDriveAuthz", OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(AUTHZ_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(AUTHZ_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setClientSecret(AUTHZ_CLIENT_SECRET)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .setScopes(Arrays.asList("https://www.googleapis.com/auth/drive"))
                    .setAdditionalAuthorizationParams(ImmutableSet.of(Pair.create("access_type", "offline")))
                    .asModule();

            authzModule.requestAccess(activity, new Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    callback.onSuccess(s);
                }

                @Override
                public void onFailure(Exception e) {
                    if (!e.getMessage().matches(OAuthWebViewDialog.OAuthReceiver.DISMISS_ERROR)) {
                        authzModule.deleteAccount();
                    }
                    callback.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
