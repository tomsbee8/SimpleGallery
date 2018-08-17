package cn.blinkdagger.simplegallery.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.blinkdagger.simplegallery.entity.PhotoBean;

public class GalleryUtil {

    /**
     * 获取所有图片
     *
     * @param context
     * @return
     */
    public static List<PhotoBean> getAllPhotoes(Context context) {
        List<PhotoBean> allPhotoes = new ArrayList<PhotoBean>();

        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA
        };

        Cursor cursor = null;
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projectionPhotos,
                    "",
                    null,
                    MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                final int imageDateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                while (cursor.moveToNext()) {
                    final int imageId = cursor.getInt(imageIdColumn);
                    final String path = cursor.getString(dataColumn);
                    final long date = cursor.getLong(imageDateColumn);
                    File file = new File(path);
                    if (file.exists() && file.length() > 0) {
                        final PhotoBean photoInfo = new PhotoBean();
                        photoInfo.setPhotoId(imageId);
                        photoInfo.setPhotoPath(path);
                        photoInfo.setPhotoDate(date);
                        allPhotoes.add(photoInfo);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("GalleryUtil", ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return allPhotoes;
    }
}
