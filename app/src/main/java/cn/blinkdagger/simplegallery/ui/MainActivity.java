package cn.blinkdagger.simplegallery.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.blinkdagger.simplegallery.R;
import cn.blinkdagger.simplegallery.entity.GroupBean;
import cn.blinkdagger.simplegallery.entity.GroupChildsEntity;
import cn.blinkdagger.simplegallery.entity.PhotoBean;
import cn.blinkdagger.simplegallery.util.DateUtil;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView galleryRV;
    private GalleryAdapter mAdapter;
    private List<GroupChildsEntity> allGroupChildList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        galleryRV = findViewById(R.id.gallery_rv);
        final int spanCount = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //当是group时就让他显示一列，如果是child就让他显示两列
                return mAdapter.getItemViewType(position) == GalleryAdapter.GROUP_ITEM_TYPE ? spanCount : 1;
            }
        });
        galleryRV.setLayoutManager(gridLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galleryRV.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void initData() {
        getSupportLoaderManager().initLoader(1, null, MainActivity.this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        final String[] PHOTOS_MEDIA_INFO = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA
        };
        CursorLoader cursorLoader = new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PHOTOS_MEDIA_INFO,
                "",
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "获取图片失败", Toast.LENGTH_LONG).show();
            return;
        }

        List<PhotoBean> allPhotoList = new ArrayList<>();
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
                allPhotoList.add(photoInfo);
            }
        }
        allGroupChildList = sortPhotoList(allPhotoList);


        mAdapter =new GalleryAdapter(this,allGroupChildList);
        galleryRV.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    private List<GroupChildsEntity> sortPhotoList(List<PhotoBean> allPhotoList) {
        List<GroupChildsEntity> result = new ArrayList<>();

        // 降序排序的TreeMap
        Map<Long, List<PhotoBean>> groupUserMap = new TreeMap<>(
                new Comparator<Long>() {
                    @Override
                    public int compare(Long obj1, Long obj2) {
                        return obj2.compareTo(obj1);
                    }
                });
        for (final PhotoBean photoBean : allPhotoList) {
            Long dateMilles = DateUtil.getShortDateMilles(photoBean.getPhotoDate());
            if (groupUserMap.containsKey(dateMilles)) {
                groupUserMap.get(dateMilles).add(photoBean);
            } else {
                groupUserMap.put(dateMilles, new ArrayList<PhotoBean>(){{
                    add(photoBean);
                }});
            }
        }

        Iterator titer = groupUserMap.entrySet().iterator();
        while (titer.hasNext()) {
            Map.Entry entry = (Map.Entry) titer.next();
            Long dateMilles = (Long) entry.getKey();
            String groupDateString = DateUtil.getShortDateString(dateMilles);
            List<PhotoBean> groupChildList = (ArrayList<PhotoBean>) entry.getValue();

            GroupChildsEntity entity = new GroupChildsEntity();
            entity.setGroupBean(new GroupBean(groupDateString, dateMilles));
            entity.setChildList(groupChildList);
            result.add(entity);
        }
        return result;
    }
}
