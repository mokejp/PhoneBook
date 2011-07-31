package org.sample;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class SampleContacts extends TabActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost.TabSpec indexes = 
        	getTabHost().newTabSpec("indexes ").
        		setIndicator(getString(R.string.tab_indexes)).setContent(new Intent(this, IndexContacts.class));  
        getTabHost().addTab(indexes);         

        TabHost.TabSpec group = 
        	getTabHost().newTabSpec("Group").
        		setIndicator(getString(R.string.tab_groups)).setContent(new Intent(this, GroupContacts.class));  
        getTabHost().addTab(group);         
	}
}