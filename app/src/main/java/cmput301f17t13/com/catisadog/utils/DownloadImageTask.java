package cmput301f17t13.com.catisadog.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

    private WeakReference<ImageView> imageView = null;

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        this.imageView = new WeakReference<>(imageViews[0]);
        return downloadImage((String)imageView.get().getTag());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.get().setImageBitmap(bitmap);
    }

    private Bitmap downloadImage(String urlString) {
        Bitmap bmp = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (bmp != null) {
                return bmp;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
