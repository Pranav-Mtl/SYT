package com.syt.balram.syt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;


public class AdminMessage extends AppCompatActivity {

    SmartImageView imgView;
    TextView tvMessage;
    Button btnApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);
        imgView= (SmartImageView) findViewById(R.id.admin_img);
        tvMessage= (TextView) findViewById(R.id.admin_tv);
        btnApp= (Button) findViewById(R.id.admin_btn);

        Intent intent=getIntent();
        Bundle bb=intent.getExtras();
        String message=bb.get("Message").toString();
        String image=bb.get("Image").toString();

        if(image.equals(""))
        {

        }
        else {
            imgView.setImageUrl(image);
        }
        tvMessage.setText(message);


        btnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMessage.this,HomeScreen.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_message, menu);
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
