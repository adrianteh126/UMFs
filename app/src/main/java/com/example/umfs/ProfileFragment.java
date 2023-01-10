package com.example.umfs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    Activity context;

    StorageReference storageReference;  //Cloud Storage reference
    FirebaseUser currentUser;   //store the current FirebaseUser authenticated
    FirebaseDatabase databaseRoot;  //access to UMFs realtime database @ firebase
    DatabaseReference databaseReference;    //reference to node under UMFs database aka Users node
    DatabaseReference userRef;  //reference to specific user under "Users" node


    private boolean shouldRefreshOnResume = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        context = getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView IVProfilePicPage = view.findViewById(R.id.IVProfilePicPage);
        final TextView TVUsernameProfile = view.findViewById(R.id.TVUsernameProfile);
        final TextView TVFacultyProfile = view.findViewById(R.id.TVFacultyProfile);
        final TextView TVBioProfile = view.findViewById(R.id.TVBioProfile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();

        try {
            //TODO : Cloud Storage reference, to store profile pictures of user
            storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");
            //get access to UMFs realtime database
            String databaseURL = "https://umfs-2cb55-default-rtdb.asia-southeast1.firebasedatabase.app";
            databaseRoot = FirebaseDatabase.getInstance(databaseURL);
            //reference to Users node under UMFs realtime database
            databaseReference = databaseRoot.getReference().child("Users");
            //reference to specific user file under "Users" node
            userRef = databaseReference.child(currentUserID);
        } catch (Exception e){
            Toast.makeText(getContext(), "Error in connecting to database", Toast.LENGTH_SHORT).show();
        }

        try{
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    //String usernameProfile = snapshot.child("username").getValue().toString();
                    TVUsernameProfile.setText(user.getUsername());

                    //String bioProfile = snapshot.child("Bio").getValue().toString();
                    TVBioProfile.setText(user.getBio());

                    //String facultyProfile = snapshot.child("Faculty").getValue().toString();
                    TVFacultyProfile.setText(user.getFaculty());

                    //String pictureProfile = snapshot.child("ProfilePicture").getValue().toString();
                    Picasso.get().load(user.getProfilePicture()).into(IVProfilePicPage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error in fetching data", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e){
            Toast.makeText(getContext(), "Method Error", Toast.LENGTH_SHORT).show();
        }
    }


    public void onStart() {
        super.onStart();
        ImageButton BTEditProfile = (ImageButton) context.findViewById(R.id.BTEdit);
        ImageButton BTSettings = (ImageButton) context.findViewById(R.id.BTSettings);

        BTEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
                Intent intent1 = new Intent(context, EditProfileActivity.class);
                startActivity(intent1);
            }
        });

        BTSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context, SettingsActivity.class);
                startActivity(intent2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldRefreshOnResume){
            getFragmentManager().beginTransaction().detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
            shouldRefreshOnResume = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRefreshOnResume = true;
    }
}


