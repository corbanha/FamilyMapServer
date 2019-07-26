package com.corbanha.familymapserver.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.corbanha.familymapserver.R;

import java.util.HashMap;
import java.util.List;

public class PersonExpandableListAdaptor extends BaseExpandableListAdapter {

    private Context context;
    private List<String> elistTitles;
    private HashMap<String, List<ItemRow>> elistDetail;

    public PersonExpandableListAdaptor(Context context, List<String> elistTitles,
                                       HashMap<String, List<ItemRow>> elistDetail){
        this.context = context;
        this.elistTitles = elistTitles;
        this.elistDetail = elistDetail;
    }

    @Override
    public int getGroupCount() {
        return elistTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return elistDetail.get(elistTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return elistTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return elistDetail.get(elistTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //creates the group title things with the little arrows

        String sectionTitle = elistTitles.get(groupPosition);

        if(convertView == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.elist_view_group_header, null);
        }

        //build the rest of the little view
        TextView titleTextView = convertView.findViewById(R.id.listGroupHeader);
        titleTextView.setTypeface(null, Typeface.BOLD);
        titleTextView.setText(sectionTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ItemRow itemRow = elistDetail.get(elistTitles.get(groupPosition)).get(childPosition);

        if(convertView == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.elist_view_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.elist_item_image_view);
        imageView.setImageResource(itemRow.drawableResource);

        TextView topLineTextView = convertView.findViewById(R.id.elist_top_line_text_view);
        topLineTextView.setText(itemRow.topText);

        TextView bottomLineTextView = convertView.findViewById(R.id.elist_bottom_line_text_view);
        bottomLineTextView.setText(itemRow.bottomText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ItemRow{
        public int drawableResource;
        public String topText;
        public String bottomText;
        public String eventOrPersonId; //the event or person Id depending on the each

        //will be used for sorting:
        public String eventType;
        public int year;

        public ItemRow(int drawableResource, String topText, String bottomText, String eventOrPersonId){
            this.drawableResource = drawableResource;
            this.topText = topText;
            this.bottomText = bottomText;
            this.eventOrPersonId = eventOrPersonId;
        }

        public ItemRow(int drawableResource, String topText, String bottomText, String eventOrPersonId,
                       String eventType, int year){
            this(drawableResource, topText, bottomText, eventOrPersonId);
            this.eventType = eventType;
            this.year = year;
        }
    }
}
