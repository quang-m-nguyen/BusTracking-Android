package com.thrifa.ruofei.bus_locator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutTabFragment extends Fragment {

    public AboutTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_about_tab, container, false);

        TextView textView = (TextView)inflate.findViewById(R.id.about_tab_title);

//        textView.setOnClickListener();

        return inflate;
    }


    public void onClick(View view){

    }
}
