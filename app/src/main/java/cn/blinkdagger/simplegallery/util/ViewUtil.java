package cn.blinkdagger.simplegallery.util;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @Author ls
 * @Date 2019/2/23
 * @Description
 * @Version
 */
public class ViewUtil {

    private static final int PENDING_SIZE = 0;
    @Nullable
    static Integer maxDisplayLength;


    /**
     * 获取ImageView 高度
     *
     * @return
     */
    public static int getTargetHeight(View view) {
        int verticalPadding = view.getPaddingTop() + view.getPaddingBottom();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int layoutParamSize = layoutParams != null ? layoutParams.height : PENDING_SIZE;
        return getTargetDimen(view, view.getHeight(), layoutParamSize, verticalPadding);
    }

    /**
     * 获取ImageView 宽度
     *
     * @return
     */
    public static int getTargetWidth(View view) {
        int horizontalPadding = view.getPaddingLeft() + view.getPaddingRight();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int layoutParamSize = layoutParams != null ? layoutParams.width : PENDING_SIZE;
        return getTargetDimen(view, view.getWidth(), layoutParamSize, horizontalPadding);
    }

    private static int getTargetDimen(View view, int viewSize, int paramSize, int paddingSize) {
        int adjustedParamSize = paramSize - paddingSize;
        if (adjustedParamSize > 0) {
            return adjustedParamSize;
        }

        if (view.isLayoutRequested()) {
            return PENDING_SIZE;
        }

        int adjustedViewSize = viewSize - paddingSize;
        if (adjustedViewSize > 0) {
            return adjustedViewSize;
        }

        if (!view.isLayoutRequested() && paramSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return getMaxDisplayLength(view.getContext());
        }
        return PENDING_SIZE;
    }

    private static int getMaxDisplayLength(@NonNull Context context) {
        if (maxDisplayLength == null) {
            WindowManager windowManager =
                    (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            Display display = checkNotNull(windowManager).getDefaultDisplay();
            Point displayDimensions = new Point();
            display.getSize(displayDimensions);
            maxDisplayLength = Math.max(displayDimensions.x, displayDimensions.y);
        }
        return maxDisplayLength;
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T arg) {
        return checkNotNull(arg, "Argument must not be null");
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T arg, @NonNull String message) {
        if (arg == null) {
            throw new NullPointerException(message);
        }
        return arg;
    }
}
