package org.learnprograming.screenmirroring;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment implements View.OnClickListener {

    Button client;

    public static ThirdFragment newInstance(){
        return new ThirdFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        client = view.findViewById(R.id.client);
        client.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), MainClint.class);
        startActivity(i);
    }
}