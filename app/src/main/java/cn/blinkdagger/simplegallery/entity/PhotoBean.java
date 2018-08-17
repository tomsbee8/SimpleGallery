package cn.blinkdagger.simplegallery.entity;

/**
 * 图片元素实体类
 */
public class PhotoBean {
    /**
     * 路径
     */
    private String photoPath;
    /**
     * 日期
     */
    private long photoDate;
    /**
     * 文件ID
     */
    private int photoId;

    private int width;
    private int height;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public long getPhotoDate() {
        return photoDate;
    }

    public void setPhotoDate(long photoDate) {
        this.photoDate = photoDate;
    }

    public  int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
