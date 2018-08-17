package cn.blinkdagger.simplegallery.entity;

public class GroupBean {
    /**
     * 日期
     */
    private String date;
    /**
     * 时间戳
     */
    private long dateMilles;

    public GroupBean(String date, long dateMilles) {
        this.date = date;
        this.dateMilles = dateMilles;
    }

    public long getPeroid() {
        return dateMilles;
    }

    public void setPeroid(long peroid) {
        this.dateMilles = peroid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
