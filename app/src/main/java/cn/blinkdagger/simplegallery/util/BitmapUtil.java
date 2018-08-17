package cn.blinkdagger.simplegallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapUtil {

    /**
     * 根据文件流的文件描述符以及指定的宽/高进行等比例缩放
     * @param reqWidth
     * @param reqHeight
     * @return
     *
     */
    public static Bitmap decodeSampledBitmapFromPath(String  path, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据指定的宽/高进行 2 的指数缩放
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (reqHeight > 0 || reqHeight > 0) {
            if (width > reqHeight && height > reqHeight) {
                final int halfWidth = width / 2;
                final int halfHeight = height / 2;
                while (halfWidth / inSampleSize >= reqWidth
                        && halfHeight / inSampleSize >= reqHeight) {
                    inSampleSize *= 2;
                }
            }
        }
        return inSampleSize;
    }

}
