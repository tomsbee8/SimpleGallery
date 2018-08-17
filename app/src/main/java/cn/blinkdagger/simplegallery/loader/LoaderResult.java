package cn.blinkdagger.simplegallery.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class LoaderResult {

    public ImageView imageView;
    public String path;
    public Bitmap bitmap;

    public LoaderResult(ImageView imageView, String path, Bitmap bitmap) {
        this.imageView = imageView;
        this.path = path;
        this.bitmap = bitmap;
    }
}
