package com.example.audakel.fammap.filter;

/**
 * Created by audakel on 6/1/16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.audakel.fammap.R;

import java.util.List;

/**
 * FilterView acts as its own “holder” by using
 * member variables to store references to its significant child views.
 *
 */
public class FilterView extends RelativeLayout {
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private ImageView mImageView;


    /**
     *  Inflates a secondary layout for children.
     */
    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.filter_view_children, this, true);
        setupChildren();
    }
    public FilterView(Context c) {
        this(c, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    /**
     * The root element is a merge tag, which means that during inflation, each of the children
     * of that tag will be added as children of the parent argument passed to the inflate(...)
     * method in the constructor. After that is done, we call our private setupChildren() method
     * to wrap up the work of calling findViewById(int) and associating the child views with the
     * appropriate member variables.
     */
    private void setupChildren() {
        mTitleTextView = (TextView) findViewById(R.id.filter_titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.filter_descriptionTextView);
    }


    /**
     * For convenience, we also provide the setItem(Filter) method for callers to use to populate
     * the child views with an Filter’s data:
     */
    public void setItem(Filter filter) {
        mTitleTextView.setText(filter.getTitle());
        mDescriptionTextView.setText(filter.getDescription());
    }


    /**
     * Inserts the views
     */
    public static FilterView inflate(ViewGroup parent) {
        FilterView filterView = (FilterView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_view, parent, false);
        return filterView;
    }

}
