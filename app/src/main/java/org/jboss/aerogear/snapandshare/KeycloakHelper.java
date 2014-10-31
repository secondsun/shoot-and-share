package org.jboss.aerogear.snapandshare;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.impl.authz.AuthorizationManager;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthorizationConfiguration;

import java.io.File;
import java.net.URL;

/**
 * Created by summers on 10/31/14.
 */
public class KeycloakHelper {

    private static final String AUTHZ_URL = "http://192.168.1.194:8080/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/shoot-realm/tokens/login";
    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/shoot-realm/tokens/access/codes";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/shoot-realm/tokens/access/codes";
    private static final String AUTHZ_ACCOOUNT_ID = "keycloak-token";
    private static final String AUTHZ_CLIENT_ID = "shoot-third-party";
    private static final String AUTHZ_REDIRECT_URL = "http://oauth2callback";

    public static void connect(final Activity activity, final Callback callback) {
        try {
            AuthzModule authzModule = AuthorizationManager.config("GoogleDriveAuthz", OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .asModule();

            authzModule.requestAccess(activity, new Callback<String>() {
                @Override
                public void onSuccess(String token) {
                    Log.d("TOKEN ++ ", token);
                    Toast.makeText(activity, token, Toast.LENGTH_LONG).show();
                    callback.onSuccess(token);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    callback.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void upload(final File file, final Callback callback) {

    }

}
