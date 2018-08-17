package cn.blinkdagger.simplegallery.entity;

import java.util.List;

public class GroupChildsEntity {
    private GroupBean groupBean;
    private List<PhotoBean> childList;

    public GroupBean getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
    }

    public List<PhotoBean> getChildList() {
        return childList;
    }

    public void setChildList(List<PhotoBean> childList) {
        this.childList = childList;
    }
}
