package org.learnprograming.screenmirroring;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;



/**
 * Created by Phaneendra on 04-Jan-17.
 */

public class MainClint extends Activity {
    ImageView imgv;
    EditText editTextAddress;
    Button buttonConnect,buttoncancle;
    final static int port=8080;
    static int flag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clint);

        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttoncancle = (Button) findViewById(R.id.cancleButton);

        imgv=(ImageView) findViewById(R.id.imgv);

        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                flag=1;
                buttonConnect.setVisibility(View.INVISIBLE);
                buttoncancle.setVisibility(View.VISIBLE);

                Log.e("Flag",String.valueOf(flag));
                if(flag==1) {
                    Client myClient = new Client(editTextAddress.getText().toString(), port, imgv,buttonConnect,MainClint.this);
                    myClient.execute();
                }

            }
        });

        buttoncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                buttonConnect.setVisibility(View.VISIBLE);
                buttoncancle.setVisibility(View.INVISIBLE);

            }
        });

    }

}

