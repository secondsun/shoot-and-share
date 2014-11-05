package org.jboss.aerogear.snapandshare.util;

import android.app.Activity;
import android.util.Pair;

import com.google.common.collect.Sets;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzModule;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.android.impl.pipeline.MultipartRequestBuilder;
import org.jboss.aerogear.android.impl.pipeline.RestfulPipeConfiguration;
import org.jboss.aerogear.android.pipeline.Pipe;
import org.jboss.aerogear.android.pipeline.PipeConfiguration;
import org.jboss.aerogear.android.pipeline.PipeManager;
import org.jboss.aerogear.snapandshare.PhotoHolder;

import java.io.File;
import java.net.MalformedURLException;
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
    private static final String MODULE_NAME = "FacebookOAuth";

    static {
        try {
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final OAuth2AuthzModule authzModule = (OAuth2AuthzModule) AuthorizationManager.getModule(MODULE_NAME);

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

    public static void upload(final File file, final Callback callback, Activity activity) {
        PipeConfiguration config = PipeManager.config("fb-upload", RestfulPipeConfiguration.class);
        MultipartRequestBuilder requestBuilder = new MultipartRequestBuilder();

        try {
            Pipe pipe = config.module(AuthorizationManager.getModule(MODULE_NAME))
                    .withUrl(new URL("https://graph.facebook.com/me/photos"))
                    .requestBuilder(requestBuilder)
                    .forClass(PhotoHolder.class);

            PipeManager.get("fb-upload", activity).save(new PhotoHolder(file), callback);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }

}
