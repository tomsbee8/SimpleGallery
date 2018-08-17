package cn.blinkdagger.simplegallery.entity;

/**
 * 列表item序号信息类
 */
public class IndexBean {

    /**
     * 是否group元素
     */
    private boolean isGroup;
    /**
     * group序号
     */
    private int groupIndex;
    /**
     * child序号
     */
    private int childIndex;

    private IndexBean(boolean isGroup, int groupIndex, int childIndex) {
        this.isGroup = isGroup;
        this.groupIndex = groupIndex;
        this.childIndex = childIndex;
    }

    public static IndexBean newGroupIndexBean(int groupIndex) {
        return new IndexBean(true,groupIndex,-1);
    }

    public static IndexBean newChildIndexBean(int groupIndex, int childIndex) {
        return new IndexBean(false,groupIndex,childIndex);
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }
}
