package com.example.umfs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton BTWrong;
    private ImageButton BTCorrect;
    private Button BTChangeProfile;
    private ImageView IVProfilePicEdit;
    private EditText ETUsernameEdit;
    private EditText ETBioEdit;

    StorageReference storageReference;  //Cloud Storage reference
    FirebaseUser currentUser;   //store the current FirebaseUser authenticated
    FirebaseDatabase databaseRoot;  //access to UMFs realtime database @ firebase
    DatabaseReference databaseReference;    //reference to node under UMFs database aka Users node
    DatabaseReference userRef;  //reference to specific user under "Users" node
    String currentUserID;

    ActivityResultLauncher<Intent> activityResultLauncher; //to launch open files activity
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        BTWrong = findViewById(R.id.BTWrong);
        BTCorrect = findViewById(R.id.BTCorrect);
        BTChangeProfile = findViewById(R.id.BTChangeProfile);
        IVProfilePicEdit = findViewById(R.id.IVProfilePicEdit);
        ETUsernameEdit = findViewById(R.id.ETUsernameEdit);
        ETBioEdit = findViewById(R.id.ETBioEdit);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = currentUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");
        //get access to UMFs realtime database
        String databaseURL = "https://umfs-2cb55-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseRoot = FirebaseDatabase.getInstance(databaseURL);
        //reference to Users node under UMFs realtime database
        databaseReference = databaseRoot.getReference().child("Users");
        //reference to specific user file under "Users" node
        userRef = databaseReference.child(currentUserID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameProfile = snapshot.child("username").getValue().toString();
                ETUsernameEdit.setText(usernameProfile);

                String bioProfile = snapshot.child("Bio").getValue().toString();
                ETBioEdit.setText(bioProfile);

                String pictureProfile = snapshot.child("ProfilePicture").getValue().toString();
                Picasso.get().load(pictureProfile).into(IVProfilePicEdit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BTChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });


        BTWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        BTCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null){
                uploadProfilePicture();
                }
                uploadProfile();
            }
        });

        //To launch file chooser activity
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            imageUri = result.getData().getData();
                            IVProfilePicEdit.setImageURI(imageUri);
                        }
                    }
                });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadProfilePicture() {
        StorageReference fileReference = storageReference.child(currentUserID).child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        Map<String, Object> picUpdates = new HashMap<>();
                                        picUpdates.put("ProfilePicture", imageUrl);
                                        userRef.updateChildren(picUpdates);

                                        Toast.makeText(EditProfileActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Failure to upload Profile Picture", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadProfile() {
        try {
            // TODO : Upload User Profile Data in Realtime Database under the specific user node
            Map<String, Object> profileUpdates = new HashMap<>();
            profileUpdates.put("username", ETUsernameEdit.getText().toString().trim());
            profileUpdates.put("Bio", ETBioEdit.getText().toString().trim());
            userRef.updateChildren(profileUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfileActivity.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Failure to upload Profile Data", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e){
            Toast.makeText(EditProfileActivity.this, "Failed to Upload Profile Data", Toast.LENGTH_LONG).show();
        }
    }

    private void chooseFile() {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction((Intent.ACTION_GET_CONTENT));
            activityResultLauncher.launch(intent);

        } catch(Exception e) {
                Toast.makeText(EditProfileActivity.this, "Choosing Files Failed. Try Again", Toast.LENGTH_SHORT).show();
        }
    }

}