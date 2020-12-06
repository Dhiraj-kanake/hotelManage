package com.example.sdl.menu;

import android.view.View;
import android.widget.TextView;

import com.example.sdl.R;
import com.example.sdl.menu.ChildList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

public class ParentList extends ExpandableGroup<ChildList> {



    public ParentList(String title, List<ChildList> items) {
        super(title, items);
    }

}

class MyParentViewHolder extends GroupViewHolder {

    public TextView listGroup;

    public MyParentViewHolder(View itemView) {
        super(itemView);
        listGroup = (TextView) itemView.findViewById(R.id.listParent);
    }

    public void setParentTitle(ExpandableGroup group) {
        listGroup.setText(group.getTitle());
    }


}