package org.learnprograming.screenmirroring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {

    Button server;
    Activity activity = getActivity();

    public static SecondFragment newInstance(){
        return new SecondFragment();
    }

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        server = activity.findViewById(R.id.server);
        server.setOnClickListener(view1 -> {
            Intent i = new Intent(view1.getContext(), MainServer.class);
            startActivity(i);
        });

        return view;
    }
}
