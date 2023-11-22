package com.example.lab05_v2;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText dobEditText;
    private EditText emailEditText;
    private Button submitButton;

    private String imagePath;

    private Button viewContactsButton;
    private DatabaseHelper databaseHelper;

    Button BSelectImage;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        emailEditText = findViewById(R.id.emailEditText);
        submitButton = findViewById(R.id.submitButton);
        viewContactsButton = findViewById(R.id.viewContacts);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set click listener for submit button
        submitButton.setOnClickListener(v -> handleSubmitButtonClick());

        viewContactsButton.setOnClickListener(v -> handleViewListContact());

        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();

                        if (null != selectedImageUri) {
                            // update the preview image in the layout
                            imagePath = saveImageToPrivateDirectory(selectedImageUri);
                            IVPreviewImage.setImageURI(Uri.parse("file://" + imagePath));
                        }
                    }
                }
            });

    private String saveImageToPrivateDirectory(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            // Create a file in the app's private directory
            File privateDir = new File(getFilesDir(), "images");
            if (!privateDir.exists()) {
                privateDir.mkdirs();
            }

            String fileName = "selected_image_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(privateDir, fileName);

            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[4 * 1024]; // Adjust buffer size as needed
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputFile.getAbsolutePath();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Method to handle submit button click
    private void handleSubmitButtonClick() {
        // Retrieve input values
        String name = nameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Perform validation
        if (name.isEmpty()) {
            showToast("Please enter a name");
            return;
        }

        if (dob.isEmpty()) {
            showToast("Please enter a date of birth");
            return;
        }

        if (!isValidDateOfBirth(dob)) {
            showToast("Please enter a valid date of birth (dd/MM/yyyy)");
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address");
            return;
        }

        // Validation successful, proceed with saving the contact information
        saveContactDetails(name, dob, email);
    }

    // Helper method to validate email address
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Helper method to validate date of birth
    private boolean isValidDateOfBirth(String dob) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dob);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void handleViewListContact(){
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }

    // Helper method to save contact details
    private void saveContactDetails(String name, String dob, String email) {
        Person person = new Person(name, dob, email, imagePath);
        long personId = databaseHelper.insertDetails(person);

        // Example toast message to indicate successful submission
        showToast("Contact information saved successfully with ID: " + personId);

        // Clear input fields
        nameEditText.setText("");
        dobEditText.setText("");
        emailEditText.setText("");
        IVPreviewImage.setImageURI(null);
        imagePath = null;
    }

    // Helper method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}