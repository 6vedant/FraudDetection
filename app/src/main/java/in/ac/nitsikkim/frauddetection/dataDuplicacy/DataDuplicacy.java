package in.ac.nitsikkim.frauddetection.dataDuplicacy;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import in.ac.nitsikkim.frauddetection.R;

public class DataDuplicacy extends AppCompatActivity {

    ProgressDialog progressDialog;
    private int upload_count = 0;

    public int getUpload_count() {
        return upload_count;
    }

    public void setUpload_count(int upload_count) {
        this.upload_count = upload_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_duplicacy);

        // here upload the files and show it in the display to show that using permission,
        // duplicate files have been uploaded to the server


        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Getting duplicates of the files to the server...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // getting some of the images from the dcim camera upto 10 files
        String dcim_folder_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/DCIM/Camera";

        File dcim_folder = new File(dcim_folder_path);
        setUpload_count(0);
        for (final File f : dcim_folder.listFiles()) {
            if (f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg") || f.getName().endsWith(".png")) {
                // it is image file
                FirebaseStorage.getInstance().getReference().child("child").putFile(Uri.parse(f.getAbsolutePath()))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // success
                                setUpload_count(getUpload_count() + 1);
                                Toast.makeText(getApplicationContext(), "Success file " + getUpload_count(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "Failure file : " + f.getName().replace("" + Environment.getExternalStorageDirectory().getAbsolutePath(), ""), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            if (getUpload_count() > 9) {
                Toast.makeText(getApplicationContext(), "Done uploading the files", Toast.LENGTH_SHORT).show();
                break;
            }
        }


    }
}
