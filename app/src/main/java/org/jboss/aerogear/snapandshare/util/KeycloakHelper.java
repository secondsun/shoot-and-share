package org.jboss.aerogear.snapandshare.util;


import android.app.Activity;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;
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

/**
 * Created by summers on 10/31/14.
 */
public class KeycloakHelper {

    private static final String AUTHZ_URL = "http://192.168.1.194:8080/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/shoot-realm/tokens/login";
    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/shoot-realm/tokens/access/codes";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/shoot-realm/tokens/refresh";
    private static final String AUTHZ_ACCOOUNT_ID = "keycloak-token";
    private static final String AUTHZ_CLIENT_ID = "shoot-third-party";
    private static final String AUTHZ_REDIRECT_URL = "http://oauth2callback";
    private static final String MODULE_NAME = "KeyCloakAuthz";

    static {
        try {
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .asModule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final AuthzModule authzModule = AuthorizationManager.getModule(MODULE_NAME);

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
        PipeConfiguration config = PipeManager.config("kc-upload", RestfulPipeConfiguration.class);
        MultipartRequestBuilder requestBuilder = new MultipartRequestBuilder();

        try {
            Pipe pipe = config.module(AuthorizationManager.getModule(MODULE_NAME))
                    .withUrl(new URL("http://192.168.1.194:8080/shoot/rest/photos"))
                    .requestBuilder(requestBuilder)
                    .forClass(PhotoHolder.class);

            PipeManager.get("kc-upload", activity).save(new PhotoHolder(file), callback);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }

}
