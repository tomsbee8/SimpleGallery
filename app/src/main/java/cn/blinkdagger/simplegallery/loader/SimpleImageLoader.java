package cn.blinkdagger.simplegallery.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.blinkdagger.simplegallery.R;
import cn.blinkdagger.simplegallery.util.BitmapUtil;
import cn.blinkdagger.simplegallery.util.GalleryUtil;
import cn.blinkdagger.simplegallery.util.Md5Util;
import cn.blinkdagger.simplegallery.util.ViewUtil;


/**
 * 简单的加载图片
 */
public class SimpleImageLoader {

    private static volatile SimpleImageLoader singleInstance;

    private SimpleImageLoader(Context mContext) {
        initImageLoader(mContext);
    }

    public static SimpleImageLoader getSingleStance(Context mContext) {

        if (singleInstance == null) {
            synchronized (SimpleImageLoader.class) {
                if (singleInstance == null) {
                    singleInstance = new SimpleImageLoader(mContext);
                }
            }
        }
        return singleInstance;
    }

    private static int MEMORY_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int TAG_KEY_ID = R.id.imageloader_id;
    private static final int MESSAGE_BITMAP_RESULT = 100;

    //CPU核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //最大线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    //线程存活时间
    private static final long KEEP_ALIVE_TIME = 10;
    // 内存缓存
    private static LruCache<String, SoftReference<Bitmap>> memoryLruCache;

    final private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_BITMAP_RESULT) {
                LoaderResult result = (LoaderResult) msg.obj;
                ImageView imageView = result.imageView;
                String tagPath = (String) imageView.getTag();
                if (tagPath.equals(result.path)) {
                    imageView.setImageBitmap(result.bitmap);
                } else {
                    Log.e("SimpleLoader", "set image bitmap,but path has changed, ignored!");
                }
            }
        }
    };

    //压缩图片线程工厂
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        //AtomicInteger，线程安全的加减操作接口
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SimpleImageLoader" + mCount.getAndIncrement());
        }
    };

    //线程池
    public static final Executor THREAD_POOR_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    /**
     * 初始化
     */
    private void initImageLoader(Context mContext) {
        memoryLruCache = new LruCache<>(MEMORY_CACHE_SIZE);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param imageView
     * @param imagePath
     */
    public static void loadImage(Context context, ImageView imageView, String imagePath) {
        Bitmap bitmap = SimpleImageLoader.getSingleStance(context).getBitmapFromMemory(imagePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            SimpleImageLoader.getSingleStance(context).loadBitmapFromDisk(imageView, imagePath);
        }
    }

    /**
     * 从内存缓存中获取bitmap
     *
     * @return
     */
    private Bitmap getBitmapFromMemory(String imagePath) {

        SoftReference<Bitmap> reference = memoryLruCache.get(imagePath);
        Bitmap cacheBitmap = null;
        if (reference != null) {
            cacheBitmap = reference.get();
        }
        return cacheBitmap;
    }

    /**
     * 从文件缓存中获取bitmap
     *
     * @param imagePath
     * @return
     */
    private void loadBitmapFromDisk(final ImageView imageView, final String imagePath) {
        imageView.setTag(imagePath);
        // 获取ImageView的真实高度
        final int height = ViewUtil.getTargetWidth(imageView);
        final int width =ViewUtil.getTargetHeight(imageView);
        Log.e("图片的高度",""+width);
        Log.e("图片的宽度",""+height);
        Log.e("100dp =",""+ GalleryUtil.dp2px(imageView.getContext(),100));

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapUtil.decodeBitmapFromPath(imagePath,width,height);
                if (bitmap != null) {
                    addBitmapToMemoryCache(imagePath, bitmap);
                    LoaderResult result = new LoaderResult(imageView, imagePath, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_BITMAP_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOR_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 添加到内存缓存
     *
     * @param key
     */
    private static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (memoryLruCache.get(key) == null) {
            memoryLruCache.put(key, new SoftReference<>(bitmap));
        }
    }


    /**
     * 根据url生成缓存key
     *
     * @param url
     */
    private static String generateKeyFromUrl(String url) {
        return Md5Util.getMd5Value(url);
    }


}
