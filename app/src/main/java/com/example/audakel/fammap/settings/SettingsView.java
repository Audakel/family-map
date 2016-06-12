package com.example.audakel.fammap.settings;

/**
 * Created by audakel on 6/1/16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.audakel.fammap.R;
import com.example.audakel.fammap.model.Line;

/**
 * SettingsView acts as its own “holder” by using
 * member variables to store references to its significant child views.
 *
 */
public class SettingsView extends RelativeLayout {
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private Switch mSwitch;
    private Spinner mSpinner;


    /**
     *  Inflates a secondary layout for children.
     */
    public SettingsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.settings_view_children, this, true);
        setupChildren();
    }
    public SettingsView(Context c) {
        this(c, null);
    }

    public SettingsView(Context context, AttributeSet attrs) {
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
        mTitleTextView = (TextView) findViewById(R.id.settings_titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.settings_descriptionTextView);
        mSwitch = (Switch) findViewById(R.id.mySwitch);
        mSpinner = (Spinner) findViewById(R.id.spinner1);

    }


    /**
     * For convenience, we also provide the setItem(Settings) method for callers to use to populate
     * the child views with an Settings’s data:
     */
    public void setItem(Line lineSetting) {
        mTitleTextView.setText(lineSetting.getTitle());
        mDescriptionTextView.setText(lineSetting.getDescription());
        mSwitch.setChecked(lineSetting.isChecked());
    }


    /**
     * Inserts the views
     */
    public static SettingsView inflate(ViewGroup parent) {
        SettingsView settingsView = (SettingsView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_view, parent, false);
        return settingsView;
    }

    public TextView getmTitleTextView() {
        return mTitleTextView;
    }

    public void setmTitleTextView(TextView mTitleTextView) {
        this.mTitleTextView = mTitleTextView;
    }

    public TextView getmDescriptionTextView() {
        return mDescriptionTextView;
    }

    public void setmDescriptionTextView(TextView mDescriptionTextView) {
        this.mDescriptionTextView = mDescriptionTextView;
    }

    public Switch getmSwitch() {
        return mSwitch;
    }

    public void setmSwitch(Switch mSwitch) {
        this.mSwitch = mSwitch;
    }

    public Spinner getmSpinner() {
        return mSpinner;
    }

    public void setmSpinner(Spinner mSpinner) {
        this.mSpinner = mSpinner;
    }
}
