package org.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sample.ContactsAdapter.ContactItem;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndexContacts extends ListActivity{

	private final class ReadContacts extends AsyncTask<String, Integer, Boolean>{
		
		@Override
		protected Boolean doInBackground(String... params) {
		
			String index = params[0];
			Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			if(c.moveToFirst()){
			        
			    do{

			        String id = c.getString(c.getColumnIndex(BaseColumns._ID));
			        String[] projection = new String[] { 
			               StructuredName.PHONETIC_FAMILY_NAME, 
			               StructuredName.PHONETIC_GIVEN_NAME };

			        Cursor nameCursor = managedQuery(
			                ContactsContract.Data.CONTENT_URI, 
			                projection,
			                ContactsContract.Data.CONTACT_ID + " = ? and " + 
			                ContactsContract.Data.MIMETYPE + "=?" ,
			        new String[]{ id, StructuredName.CONTENT_ITEM_TYPE }, null);

			        nameCursor.moveToFirst();
			        String familyName = nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
			        String givenName = nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));

			        nameCursor.close();

					if (!INDEX_MAP.get(StringUtils.toEmpty(index)).contains(
							String.valueOf((StringUtils.toEmpty(familyName) + StringUtils.toEmpty(givenName)).charAt(0)))){
						continue;
					}

					ContactItem item = new ContactItem();

					item.id = c.getInt(c.getColumnIndex(BaseColumns._ID));
					item.photoId = c.getInt(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
					item.name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					item.familyName = familyName;
					item.givenName = givenName;
					
					items.add(item);
					
					// TODO 更新。

			    }while(c.moveToNext());
			}

			c.close();
			return true;
		}

		@Override
		protected void onPostExecute (Boolean result){
			adapter.notifyDataSetChanged();
			getListView().setSelection(0);
		}
	}
	
	@SuppressWarnings("serial")
	private static final List<String> INDEXES = new ArrayList<String>(){{
		add("あ");
		add("か");
		add("さ");
		add("た");
		add("な");
		add("ま");
		add("や");
		add("ら");
		add("わ");
		add("Ａ");
		add("他");
	}};
	
	@SuppressWarnings("serial")
	private static final Map<String, List<String>> INDEX_MAP = new HashMap<String, List<String>>(){{

		put("あ", new ArrayList<String>(){{
			add("あ");
			add("い");
			add("う");
			add("え");
			add("お");
		}});

		put("か", new ArrayList<String>(){{
			add("か");
			add("き");
			add("く");
			add("け");
			add("こ");
		}});

		put("さ", new ArrayList<String>(){{
			add("さ");
			add("し");
			add("す");
			add("せ");
			add("そ");
		}});
		
		put("た", new ArrayList<String>(){{
			add("た");
			add("ち");
			add("つ");
			add("て");
			add("と");
		}});
		
		put("な", new ArrayList<String>(){{
			add("な");
			add("に");
			add("ぬ");
			add("ね");
			add("の");
		}});
		
		put("は", new ArrayList<String>(){{
			add("は");
			add("ひ");
			add("ふ");
			add("へ");
			add("ほ");
		}});
		
		put("ま", new ArrayList<String>(){{
			add("ま");
			add("み");
			add("む");
			add("め");
			add("も");
		}});
		
		put("や", new ArrayList<String>(){{
			add("や");
			add("ゆ");
			add("よ");
		}});
		
		put("ら", new ArrayList<String>(){{
			add("ら");
			add("り");
			add("る");
			add("れ");
			add("ろ");
		}});
		
		put("わ", new ArrayList<String>(){{
			add("を");
			add("ん");
		}});
	}};
	private Map<String, TextView> indexViews = new HashMap<String, TextView>();
	private ContactsAdapter adapter;
	private ArrayList<ContactItem> items = new ArrayList<ContactItem>(500);
	
	private int selectedIndex = -1;
	
	private void findViews(){
		
		LinearLayout indexView = (LinearLayout) findViewById(R.id.index);
		float textSize = getResources().getDimension(R.dimen.text_size_normal);
		
		for (final String index : INDEXES){

			final TextView view = new TextView(this);
			
			view.setGravity(Gravity.LEFT);
			view.setText(index);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			view.setTextColor(Color.WHITE);
			view.setBackgroundResource(R.drawable.index_normal);
			indexView.addView(view);
			indexViews.put(index, view);
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (selectedIndex != -1){
						indexViews.get(INDEXES.get(selectedIndex)).setBackgroundResource(R.drawable.index_normal);
						indexViews.get(INDEXES.get(selectedIndex)).setTextColor(Color.WHITE);
					}
					
					selectedIndex = INDEXES.indexOf(index);
					view.setBackgroundResource(R.drawable.index_selected);
					view.setTextColor(Color.BLACK);
					setContacts(index);
				}
			});
		}
		
		adapter = new ContactsAdapter(this, R.layout.contact_item, items);
        setListAdapter(adapter);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.index_contacts);
        findViews();
	}
	
	private void setContacts(String index){

		// TODO ふりがなから名前を取得。CursorAdapter に変更。
		items.clear();
		new ReadContacts().execute(index);
	}
}