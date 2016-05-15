package cuny.gc.multimodality.gettingthere;

/**
 * This code has been adapted from:
 * https://github.com/DynamsoftRD/android-tesseract-ocr
 */

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class TessOCR {

    private static final String TAG = "TessOCR.java";

    private TessBaseAPI mTess;

    public TessOCR() {
        // TODO Auto-generated constructor stub
        mTess = new TessBaseAPI();

        String language = "eng";

        // check the top path
        // String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
        String datapath = Environment.getExternalStorageDirectory().toString() + "/GettingThere/";

        File datapath_subdir = new File(datapath + "tessdata/");

        if (!datapath_subdir.exists())
            datapath_subdir.mkdirs();

        mTess.init(datapath, language);
    }

    public String getOCRResult(Bitmap bitmap) {

        Log.v(TAG, "TessOCR: before mTess.setImage ");

        mTess.setImage(bitmap);

        Log.v(TAG, "TessOCR: mTess.setImage got through");

        String result = mTess.getUTF8Text();

        Log.v(TAG, "TessOCR: mTess.getUTF8Text() got through");

        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

}
