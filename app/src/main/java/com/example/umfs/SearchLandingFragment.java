package com.example.umfs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchLandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchLandingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchLandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchLandingFragment.
     */
    // TODO: Rename and change types and number of parameters

    private ListView listView;
    //Some test result for list view -> fragment_search_leanding
    String[] name = {"Result 1","Result 2","Result 3","Result 4","Result 5"
            ,"Result 6","Result 7","Result 8","Result 9","Result 10"};

    private ArrayAdapter<String> arrayAdapter;

    public ArrayAdapter<String> getArrayAdapter() {
        return arrayAdapter;
    }

    public static SearchLandingFragment newInstance(String param1, String param2) {
        SearchLandingFragment fragment = new SearchLandingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_landing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        listView = view.findViewById(R.id.LVsearchResults);
        arrayAdapter = new ArrayAdapter<String>(view.getContext()
                ,android.R.layout.simple_list_item_1,name);
        listView.setAdapter(arrayAdapter);
    }


}

