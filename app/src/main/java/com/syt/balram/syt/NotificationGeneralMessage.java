package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class NotificationGeneralMessage extends AppCompatActivity {

    TextView tv;
    Button btnGoToApp,btnWebLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_general_message);

        tv= (TextView) findViewById(R.id.general_message);
        btnGoToApp= (Button) findViewById(R.id.general_message_btn);
        btnWebLink= (Button) findViewById(R.id.general_message_webLnk);

        String message=getIntent().getStringExtra("Message");
        final String URL=getIntent().getStringExtra("URL");

        if(URL.isEmpty() || URL.equals(""))
        {
            btnWebLink.setVisibility(View.GONE);
        }
        else
        {
            btnWebLink.setVisibility(View.VISIBLE);
        }

        tv.setText(message);

        btnGoToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            }
        });

        btnWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WebView.class).putExtra("URL",URL));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_general_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
