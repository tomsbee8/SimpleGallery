package cn.blinkdagger.simplegallery.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.blinkdagger.simplegallery.R;
import cn.blinkdagger.simplegallery.entity.GroupChildsEntity;
import cn.blinkdagger.simplegallery.entity.IndexBean;
import cn.blinkdagger.simplegallery.entity.PhotoBean;
import cn.blinkdagger.simplegallery.loader.SimpleImageLoader;

/**
 * 适配器
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int GROUP_ITEM_TYPE = 0;
    public static final int CHILD_ITEM_TYPE = 1;
    private Context context;
    /**
     *  数据源
     */
    private List<GroupChildsEntity> dataSourceList;
    /**
     * 存放每个item位置元素的child 序号和group序号信息
     */
    private SparseArray<IndexBean> groupIndexList = new SparseArray<IndexBean>();

    public GalleryAdapter(Context context, List<GroupChildsEntity> dataSourceList) {
        this.context = context;
        this.dataSourceList = dataSourceList;
        initGroupIndex();
    }

    private void initGroupIndex() {
        if (dataSourceList != null && !dataSourceList.isEmpty()) {
            int position = 0;
            for (int i = 0; i < dataSourceList.size(); i++) {

                Log.e("Adapater:", "i=" + i + "     " + "position =" + position + "个数" + dataSourceList.get(i).getChildList().size());
                IndexBean groupIndexBean = IndexBean.newGroupIndexBean(i);
                groupIndexList.put(position, groupIndexBean);
                position++;

                if (dataSourceList.get(i).getChildList() != null) {
                    for (int j = 0; j < dataSourceList.get(i).getChildList().size(); j++) {
                        Log.e("Adapater:", "i=" + i + "     " + "j=" + j + "    " + "position =" + position);

                        IndexBean childIndexBean = IndexBean.newChildIndexBean(i, j);
                        groupIndexList.put(position, childIndexBean);
                        position++;
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        IndexBean indexbean = groupIndexList.get(position);

        if (indexbean != null && indexbean.isGroup()) {
            return GROUP_ITEM_TYPE;
        } else {
            return CHILD_ITEM_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == GROUP_ITEM_TYPE) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.group_gallery_list, parent, false);
            return new GroupViewHolder(convertView);
        } else {
            View convertView = LayoutInflater.from(context).inflate(R.layout.item_gallery_list, parent, false);
            return new ItemViewHolder(convertView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final IndexBean indexBean = groupIndexList.get(position);
        if (indexBean != null) {
            if (getItemViewType(position) == GROUP_ITEM_TYPE) {
                GroupViewHolder itemHolder = (GroupViewHolder) holder;
                int groupIndex = indexBean.getGroupIndex();
                itemHolder.dateTV.setText(dataSourceList.get(groupIndex).getGroupBean().getDate());
            } else if (getItemViewType(position) == CHILD_ITEM_TYPE) {
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
                int groupIndex = indexBean.getGroupIndex();
                int childIndex = indexBean.getChildIndex();
                PhotoBean photoBean = dataSourceList.get(groupIndex).getChildList().get(childIndex);

                if (photoBean.getPhotoPath() != null) {
                    SimpleImageLoader.loadImage(context, itemHolder.imageView, photoBean.getPhotoPath());
//                    Glide.with(context).load(photoBean.getPhotoPath()).into(itemHolder.imageView);
                }
            }
        }
    }

    @Override
    public int getItemCount() {

        if (dataSourceList == null || dataSourceList.isEmpty()) {
            return 0;
        }
        // 分组group数目+ child数目
        int count = dataSourceList.size();
        for (int i = 0; i < dataSourceList.size(); i++) {
            GroupChildsEntity item = dataSourceList.get(i);
            if (item.getChildList() != null && !item.getChildList().isEmpty()) {
                count = count + item.getChildList().size();
            }
        }
        return count;
    }

    /**
     * 图片的ViewhHolder
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_gallery_iv);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_gallery_cb);
        }
    }

    /**
     * 分组的ViewhHolder
     */
    static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        TextView selectTV;

        public GroupViewHolder(View itemView) {
            super(itemView);
            dateTV = (TextView) itemView.findViewById(R.id.group_gallery_date_tv);
            selectTV = (TextView) itemView.findViewById(R.id.group_gallery_select_tv);
        }
    }
}
