package com.google.sign.in.linesignintesting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    private String TAG = "Line";
    private String CHANNEL_ID ;
    private int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CHANNEL_ID = getApplicationContext().getString(R.string.env_channel_id);
        LoginButton loginButton = findViewById(R.id.line_login_btn);

        loginButton.setChannelId(CHANNEL_ID);

        // configure whether login process should be done by Line App, or inside WebView.
        loginButton.enableLineAppAuthentication(true);

        // set up required scopes.
        loginButton.setAuthenticationParams(new LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                .build()
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        Log.e("Code", result.getResponseCode().toString());
        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();

                Log.e("SUCCESS ", result.getLineProfile().getDisplayName());

                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user.");
                Log.e("ERROR", result.getErrorData().toString());
                break;
            case AUTHENTICATION_AGENT_ERROR:
                Log.e("ERROR", "LINE Login AUTHENTICATION_AGENT_ERROR ");
                Log.e("ERROR", result.getErrorData().toString());
                break;
            default:
                // Login canceled due to other error3
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getResponseCode().toString());
                Log.e("ERROR", result.getErrorData().toString());
        }
    }

}
