package com.corbanha.familymapserver.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Event;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchBar;
    private RecyclerView recyclerView;
    private SearchItemAdapter itemAdapter;

    private Model model;

    private ArrayList<String> titles;
    private HashMap<String, List<PersonExpandableListAdaptor.ItemRow>> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchBar = findViewById(R.id.search_search_view);
        recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        titles = new ArrayList<>();
        titles.add("");

        model = Model.getInstance();

        itemAdapter = new SearchItemAdapter(new ArrayList<SearchResult>());
        recyclerView.setAdapter(itemAdapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemAdapter.setSearchResults(model.getFilteredSearchResults(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.setSearchResults(model.getFilteredSearchResults(newText));
                return false;
            }
        });

    }

    private class SearchItemHolder extends RecyclerView.ViewHolder{

        private TextView upperText;
        private TextView bottomText;
        private ImageView imageView;


        private String id;
        private boolean isPerson;

        public SearchItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.elist_view_item, parent, false));

            upperText = itemView.findViewById(R.id.elist_top_line_text_view);
            bottomText = itemView.findViewById(R.id.elist_bottom_line_text_view);
            imageView = itemView.findViewById(R.id.elist_item_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if(isPerson){
                        intent = new Intent(SearchActivity.this, PersonActivity.class);
                        intent.putExtra(PersonActivity.EXTRA_PERSON_ID, id);
                    }else{
                        intent = new Intent(SearchActivity.this, EventActivity.class);
                        intent.putExtra(EventActivity.EXTRA_EVENT_ID, id);
                    }
                    startActivity(intent);
                }
            });

        }

        public void bind(boolean isPerson, String id){
            this.isPerson = isPerson;
            this.id = id;

            if(isPerson){
                Person p = model.getPerson(id);
                upperText.setText(p.getFirstName() + " " + p.getLastName());
                bottomText.setText("");
                imageView.setImageResource(p.getGender().equals("m") ? R.drawable.male : R.drawable.female);
            }else{
                Event e = model.getEvent(id);
                if(e != null){
                    Person p = model.getPerson(e.getPersonID());

                    if(p != null){

                        upperText.setText(e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")");
                        bottomText.setText(p.getFirstName() + " " + p.getLastName());
                        imageView.setImageResource(R.drawable.map_pin);
                    }
                }
            }
        }
    }

    private class SearchItemAdapter extends RecyclerView.Adapter<SearchItemHolder> {
        private List<SearchResult> searchResults;

        public SearchItemAdapter(List<SearchResult> searchResults){
            this.searchResults = searchResults;
        }

        public void setSearchResults(List<SearchResult> searchResults){
            this.searchResults = searchResults;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
            return new SearchItemHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchItemHolder itemHolder, int i) {
            SearchResult result = searchResults.get(i);
            itemHolder.bind(result.isPerson, result.id);
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }
    }

    public static class SearchResult{
        boolean isPerson;
        String id;

        public SearchResult(boolean isPerson, String id){
            this.isPerson = isPerson;
            this.id = id;
        }
    }
}
