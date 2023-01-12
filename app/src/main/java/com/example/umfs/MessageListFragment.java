package com.example.umfs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.umfs.databinding.FragmentMessageListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageListFragment extends Fragment {

    FragmentMessageListBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ChatAdapter chatAdapter;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView TVToolbarTitle;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageListFragment newInstance(String param1, String param2) {
        MessageListFragment fragment = new MessageListFragment();
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
        binding  = FragmentMessageListBinding.inflate(inflater, container, false);
        binding= FragmentMessageListBinding.inflate(getLayoutInflater());
        mAuth= FirebaseAuth.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        chatAdapter=new ChatAdapter(getContext());
        binding.messageRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatAdapter.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String uid=dataSnapshot.getKey();
                    if (!uid.equals(FirebaseAuth.getInstance().getUid())){
                        User user=dataSnapshot.getValue(User.class);
                        chatAdapter.add(user);
                    }
                }
                binding.messageRecycleView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TVToolbarTitle = getView().getRootView().findViewById(R.id.TVToolbarTitle);
        TVToolbarTitle.setText("Inbox");
    }
}