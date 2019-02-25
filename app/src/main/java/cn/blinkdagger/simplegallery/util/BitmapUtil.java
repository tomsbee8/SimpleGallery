package cn.blinkdagger.simplegallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

    /**
     * 根据文件流的文件描述符以及指定的宽/高进行等比例缩放
     * @param reqWidth
     * @param reqHeight
     * @return
     *
     */
    public static Bitmap decodeBitmapFromPath(String  path, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据指定的宽/高进行 2 的指数缩放
     *
     * @param options
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int viewWidth, int viewHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (viewWidth > 0 &&  viewHeight > 0) {
            if (width > viewWidth || height > viewHeight) {
                final int halfWidth = width / 2;
                final int halfHeight = height / 2;
                while (halfWidth / inSampleSize >= viewWidth
                        && halfHeight / inSampleSize >= viewHeight) {
                    inSampleSize *= 2;
                }
            }
        }
        return inSampleSize;
    }


    /**
     * 根据指定的宽/高进行缩放
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }

        return inSampleSize;
    }

    /**
     * 根据指定的宽/高进行缩放
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize3(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //取比例值中的较大值作为inSampleSize
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}
