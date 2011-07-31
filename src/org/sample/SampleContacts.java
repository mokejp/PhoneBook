package org.sample;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class SampleContacts extends TabActivity
	implements OnTabChangeListener {

	private TextView indexesTabText;
	private TextView groupTabText;
	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        indexesTabText = new TextView(this);
        indexesTabText.setText(R.string.tab_indexes);
        indexesTabText.setPadding(0, 5, 0, 0);
        indexesTabText.setTextSize(15);
        indexesTabText.setBackgroundColor(Color.LTGRAY);
        indexesTabText.setTextColor(Color.WHITE);
        indexesTabText.setGravity(Gravity.CENTER_HORIZONTAL);
        indexesTabText.setHeight(39);
        
        
        TabHost.TabSpec indexes = 
        	getTabHost().newTabSpec("indexes").
        		setIndicator(indexesTabText).setContent(new Intent(this, IndexContacts.class));
        
        getTabHost().addTab(indexes);         

        
        groupTabText = new TextView(this);
        groupTabText.setText(R.string.tab_groups);
        groupTabText.setPadding(0, 5, 0, 0);
        groupTabText.setTextSize(15);
        groupTabText.setBackgroundColor(Color.DKGRAY);
        groupTabText.setTextColor(Color.WHITE);
        groupTabText.setGravity(Gravity.CENTER_HORIZONTAL);
        groupTabText.setHeight(39);
        TabHost.TabSpec group = 
        	getTabHost().newTabSpec("Group").
        		setIndicator(groupTabText).setContent(new Intent(this, GroupContacts.class));  
        
        getTabHost().addTab(group);         
        
        getTabHost().setOnTabChangedListener(this);
	}

	@Override
	public void onTabChanged(String tabId) {  
		groupTabText.setBackgroundColor(Color.DKGRAY);  
    	indexesTabText.setBackgroundColor(Color.DKGRAY); 
	    if (tabId == "Group") {
	    	groupTabText.setBackgroundColor(Color.LTGRAY);  
	    } else if (tabId == "indexes"){  
	    	indexesTabText.setBackgroundColor(Color.LTGRAY);  
	    }
	    
	  } 
}