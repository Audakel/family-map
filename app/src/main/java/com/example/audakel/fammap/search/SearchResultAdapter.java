package com.example.audakel.fammap.search;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.audakel.fammap.Constants;
import com.example.audakel.fammap.R;
import com.example.audakel.fammap.model.Searchable;
import com.example.audakel.fammap.person.PersonActivity;

import java.util.ArrayList;

import static com.example.audakel.fammap.Constants.SHARAED_PREFS_BASE;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Searchable> mDataset = new ArrayList<Searchable>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchResultAdapter(Context context) {
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Searchable searchable = mDataset.get(position);

        TextView cardTitle = (TextView)holder.view.findViewById(R.id.info_text);
        TextView idHack = (TextView)holder.view.findViewById(R.id.info_id_hack);

        cardTitle.setText(searchable.getTitle());
        idHack .setText(searchable.getIdHack());

        CardView cardView = (CardView) holder.view.findViewById(R.id.card_view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the hidden view and get the id string
                String  id = ((TextView) view.findViewById(R.id.info_id_hack)).getText().toString();

                // Parse the id
                String [] idArray = id.split(":");

                Intent intent = null;

                // its a person button
                if (idArray[0].equals("person")){
                    intent = new Intent(view.getContext(), PersonActivity.class);
                    String personId = idArray[1];
                    intent.putExtra(Constants.PERSON_INTENT_ID, personId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);

                }
                // its an event
                else if (idArray[0].equals("event")){
                    writeToSharedPrefs(idArray[1], view);
                    ((SearchActivity)context).finish();
                }
            }
        });
    }


    /**
     * takes the persons id and hacks it into shared prefs. this will let main activity map zoom correctly
     * on the right event
     *
     * @param value
     */
    private void writeToSharedPrefs(String value, View view){
        SharedPreferences sharedPref = view.getContext().getSharedPreferences(SHARAED_PREFS_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARAED_PREFS_BASE + Constants.ZOOM_LATLNG, value);
        editor.commit();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public void addResult(Searchable searchable) {
        mDataset.add(searchable);
    }

    public void clearResults() {
        mDataset.clear();
    }
}