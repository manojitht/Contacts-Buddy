package com.example.contactsbuddy.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contactsbuddy.Helpers.DBHelperClass;
import com.example.contactsbuddy.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import de.hdodenhof.circleimageview.CircleImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddContact_Activity extends AppCompatActivity {

    private CircleImageView cb_image_selector;
    private EditText cb_name, cb_number, cb_email;
    private Button save_contact_btn;
    private ImageView back_btn;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    private String contact_name, contact_number, contact_email;

    private DBHelperClass dbHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        cb_image_selector = (CircleImageView) findViewById(R.id.contact_profile_img);
        cb_name = (EditText) findViewById(R.id.contact_name);
        cb_number = (EditText) findViewById(R.id.contact_number);
        cb_email = (EditText) findViewById(R.id.contact_email);
        save_contact_btn = (Button) findViewById(R.id.save_contact_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        dbHelperClass = new DBHelperClass(this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContact_Activity.this, ShowContactsList_Activity.class);
                startActivity(intent);
            }
        });

        cb_image_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseContactImage();
            }
        });

        save_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateContactDetails();
            }
        });
    }

    private void chooseContactImage(){
        String[] options = {"Take Image From Camera", "Choose Image From Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (ContextCompat.checkSelfPermission(AddContact_Activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(AddContact_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                takeImageFromCamera();
                            } else {
                                cameraPermissionRequest();
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(AddContact_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                getImageFromGallery();
                            } else {
                                storagePermissionRequest();
                            }
                        }
                    }
                }).show();
    }

    private void takeImageFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void storagePermissionRequest() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private void cameraPermissionRequest() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takeImageFromCamera();
            } else {
                Toast.makeText(this, "Please allow contact buddy to access the camera!", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromGallery();
            } else {
                Toast.makeText(this, "Please allow contact buddy to access the storage!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveOnDirectory(String srcDir, String desDir) throws IOException {
        File src = new File(srcDir);
        File des = new File(desDir, src.getName());

        FileChannel source = null;
        FileChannel destination = null;

        try {
            if (!des.getParentFile().exists()) {
                des.mkdirs();
            }
            if (!des.exists()) {
                des.createNewFile();
            }

            source = new FileInputStream(src).getChannel();
            destination = new FileOutputStream(des).getChannel();
            destination.transferFrom(source, 0, source.size());

            imageUri = Uri.parse(des.getPath());

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    cb_image_selector.setImageURI(resultUri);
                    try {
                        saveOnDirectory("" + imageUri.getPath(), "" + getDir("contacts_images", MODE_PRIVATE));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void validateContactDetails() {
        String contactNumberRegex = "\\d{10}";

        contact_name = cb_name.getText().toString().trim();
        contact_number = cb_number.getText().toString().trim();
        contact_email = cb_email.getText().toString().trim();

        if (TextUtils.isEmpty(contact_name)) {
            Toast.makeText(this, "Please enter contact name!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact_number)) {
            Toast.makeText(this, "Please enter your contact number!", Toast.LENGTH_LONG).show();
            return;
        } else if (!contact_number.matches(contactNumberRegex)) {
            Toast.makeText(this, "Contact number format is invalid!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact_email)) {
            Toast.makeText(this, "Please enter contact email!", Toast.LENGTH_LONG).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(contact_email).matches()) {
            Toast.makeText(this, "Email format is invalid!", Toast.LENGTH_LONG).show();
            return;
        }
        saveContactDetails();
    }

    private void saveContactDetails() {
        String timestamp = "" + System.currentTimeMillis();
        long result = dbHelperClass.createContact("" + contact_name, "" + contact_number,
                "" + contact_email, "" + imageUri, "" + timestamp, "" + timestamp);
        Toast.makeText(this, "" + contact_name + " was saved to our contacts list successfully!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddContact_Activity.this, ShowContactsList_Activity.class);
        startActivity(intent);
    }

}