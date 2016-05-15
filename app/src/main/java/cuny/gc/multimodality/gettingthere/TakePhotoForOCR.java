package cuny.gc.multimodality.gettingthere;

/**
 * Some of the code in this file is derived from SimpleAndroidOCRActivity.java
 * as found in the project https://github.com/GautamGupta/Simple-Android-OCR.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class TakePhotoForOCR extends AppCompatActivity {

    // way of identifying intents
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final String PHOTO_TAKEN = "photo_taken";

    // constants for OCR
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/GettingThere/";
    public static final String lang = "eng";
    private static final String TAG = "TakePhotoForOCR.java";

    protected EditText _field;
    protected String _path;
    protected boolean _taken;

    ImageView ocrResultImageView;
    TextView ocrResultTextView;
    Button takePhotoButton;
    Button acceptButton;
    Button backToNavButton;
    TessOCR mTessOCR;
    private ProgressDialog mProgressDialog;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // set variables for layout
        ocrResultImageView = (ImageView) findViewById(R.id.ocrResultImageView);
        ocrResultTextView = (TextView) findViewById(R.id.ocrResultTextView);
        takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        backToNavButton = (Button) findViewById(R.id.backToNavButton);

        // disable some buttons if user has no camera
        if (!hasCamera()) {
            takePhotoButton.setEnabled(false);
            acceptButton.setEnabled(false);
        }

        // make dir for putting tesseract trained data file onto SD card
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }

        // copy the training file to the SD card if it's not there already
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {

            try {

                // asset management and identification
                AssetManager assetManager = getAssets();
                InputStream trainfile_in = assetManager.open("tessdata/" + lang + ".traineddata");
                OutputStream trainfile_out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;

                while ((len = trainfile_in.read(buf)) > 0) {
                    trainfile_out.write(buf, 0, len);
                }

                trainfile_in.close();
                trainfile_out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");

            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }

        // define path on disk of photo file
        _path = DATA_PATH + "/ocr.jpg";

        // start a tesseract instance
        mTessOCR = new TessOCR();
    }

    public void onClick(View v) {

        if (v.getId() == R.id.acceptButton) {

            // if accepted, send to another class for verification that
            // user is at the right platform

            Intent i = new Intent(this, NavCheck.class);
            startActivity(i);

        } else if (v.getId() == R.id.backToNavButton) {

            // right now this defaults to restarting the Navigation scenario
            // in a future version, this should instead go back to the previous
            // step, allowing for the choice of text input, OCR input, or no input
            // needed

            Intent i = new Intent(this, NavMenu.class);
            startActivity(i);
        }
    }

    // check if user has a camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    // method for launching camera
    public void launchCamera(View view) {

        // camera-launching intent
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // take picture and pass results to onActivityResult
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    // if you want to return the image taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // this is the code from the youtube tutorial for saving the photo
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // get the photo
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (photo == null) {
                Log.i(TAG, "onActivityResult: photo is null.");
            } else {
                Log.i(TAG, "onActivityResult: photo is not null.");
            }

            ocrResultImageView.setImageBitmap(photo);

            String result = mTessOCR.getOCRResult(photo);
            ocrResultTextView.setText(result);

        }
    }

//    private void doOCR() {
//
//        Log.v(TAG, "doOCR");
//
//        if (mProgressDialog == null) {
//            mProgressDialog = ProgressDialog.show(this, "Processing", "Doing OCR...", true);
//        }
//        else {
//            mProgressDialog.show();
//        }
//
//        new Thread(new Runnable() {
//            public void run() {
//
//                try {
//                    final String result = mTessOCR.getOCRResult(bitmap);
//                    Log.i(TAG, "mTessOCR.getOCRResult(bitmap) seems to have gotten this far");
//
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            if ((result != null) && !result.equals("")) {
//                                ocrResultTextView.setText(result);
//                                Log.i(TAG, "doOCR.run() seems to have run");
//                            } else {
//                                Log.e(TAG, "doOCR.run() doesn't work.");
//                            }
//
//                            mProgressDialog.dismiss();
//                        }
//
//                    });
//                } catch (Exception e) {
//                    Log.v(TAG, "doOCR: Something went wrong in the thread: " + e);
//                    mProgressDialog.dismiss();
//                }
//            };
//        }).start();
//    }
//
//    protected void onPhotoTaken(Bitmap bitmap) {
//        _taken = true;
//
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inSampleSize = 4;
////
////        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
//
//        // orientation fixes if necessary
//        try {
//            ExifInterface exif = new ExifInterface(_path);
//            int exifOrientation = exif.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//
//            Log.v(TAG, "Orient: " + exifOrientation);
//
//            int rotate = 0;
//
//            switch (exifOrientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//            }
//
//            Log.v(TAG, "Rotation: " + rotate);
//
//            if (rotate != 0) {
//
//                // Getting width & height of the given image.
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//
//                // Setting pre rotate
//                Matrix mtx = new Matrix();
//                mtx.preRotate(rotate);
//
//                // Rotating Bitmap
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
//            }
//
//            // Convert to ARGB_8888, required by tess
//            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        } catch (IOException e) {
//            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
//        }
//
//        // _image.setImageBitmap( bitmap );
//
//        Log.v(TAG, "Before baseApi");
//
//        TessBaseAPI baseApi = new TessBaseAPI();
//        baseApi.setDebug(true);
//        baseApi.init(DATA_PATH, lang);
//        baseApi.setImage(bitmap);
//
//        String recognizedText = baseApi.getUTF8Text();
//
//        baseApi.end();
//
//        // You now have the text in recognizedText var, you can do anything with it.
//
//        Log.v(TAG, "OCRED TEXT: " + recognizedText);
//
//        // using regex to clear out non-alphanumeric cruft
//        if ( lang.equalsIgnoreCase("eng") ) {
//            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
//        }
//
//        recognizedText = recognizedText.trim();
//
//        if ( recognizedText.length() != 0 ) {
//
//            ocrResultTextView.setText(recognizedText);
//
////            _field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
////            _field.setSelection(_field.getText().toString().length());
//        } else {
//
//            ocrResultTextView.setText("something didn't work");
//        }
//
//        // Cycle done.
//    }


}
