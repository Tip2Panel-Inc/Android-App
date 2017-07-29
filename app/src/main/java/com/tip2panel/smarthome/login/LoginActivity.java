package com.tip2panel.smarthome.login;

/**
 * Created by Setsuna F. Seie on 21/07/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.gateway.GatewayActivity;

public class LoginActivity extends AppCompatActivity {

    private Intent intent;

    Button login;
    Button loginfb;
    Button register;
    ImageView imgVw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(this, GatewayActivity.class);
        login = (Button) findViewById(R.id.button_login);
        loginfb = (Button) findViewById(R.id.button_loginfb);
        register = (Button) findViewById(R.id.button_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }
}
