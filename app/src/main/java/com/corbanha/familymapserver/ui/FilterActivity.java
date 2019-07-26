package com.corbanha.familymapserver.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Model;

import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.filter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        model = Model.getInstance();

        updateUI();

    }

    private void updateUI(){
        itemAdapter = new ItemAdapter(model.getFilter().getFilterOptions());
        recyclerView.setAdapter(itemAdapter);
    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView filterTitle;
        private Switch filterSwitch;
        private String filter;

        public ItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.filter_list_item, parent, false));

            filterTitle = itemView.findViewById(R.id.filter_list_item_text_view);
            filterSwitch = itemView.findViewById(R.id.filter_list_item_switch);

            filterSwitch.setOnClickListener(this);
        }

        public void bind(String string){
            filter = string;
            filterTitle.setText(string.substring(0, 1).toUpperCase() + string.substring(1));
            filterSwitch.setChecked(model.getFilter().isShowEventType(string));
        }

        @Override
        public void onClick(View v) {
            Switch filterSwitch = v.findViewById(R.id.filter_list_item_switch);

            model.getFilter().setShowEventType(filter, filterSwitch.isChecked());
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<String> filters;

        public ItemAdapter(List<String> filters){
            this.filters = filters;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(FilterActivity.this);
            return new ItemHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
            String string = filters.get(i);
            itemHolder.bind(string);
        }

        @Override
        public int getItemCount() {
            return filters.size();
        }
    }
}
