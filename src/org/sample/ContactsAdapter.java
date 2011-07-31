package org.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// TODO CursorAdapter に変更。
public class ContactsAdapter extends ArrayAdapter<ContactsAdapter.ContactItem>{

	public static final class ContactItem{

		public int id;
		public int photoId;
		public String name;
		public String familyName;
		public String givenName;

		private SoftReference<Bitmap> thum;
		
		public Bitmap getPhoto(Context context){

			if (thum != null && thum.get() != null){
				return thum.get();
			}
			
            if (0 < photoId) {

            	InputStream in = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), 
                		ContentUris.withAppendedId(
			                    ContactsContract.Contacts.CONTENT_URI, id));
            	thum = new SoftReference<Bitmap>(BitmapFactory.decodeStream(in));

                try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            
            } else {
            	thum = new SoftReference<Bitmap>(((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_contact_picture)).getBitmap());
            }
            
			return thum.get();
		}
	}
	
	private static final class ViewHolder{
		
		public TextView name;
		public ImageView thum;
		
		public ViewHolder(TextView name, ImageView thum){
			this.name = name;
			this.thum = thum;
		}
	}
	
	private LayoutInflater inflater;
	private int textViewResourceId;
	
	public ContactsAdapter(Context context, int textViewResourceId, List<ContactItem> items) {
		
		super(context, textViewResourceId, items);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textViewResourceId = textViewResourceId;
		setNotifyOnChange(false);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ContactItem item = getItem(position);
		final ViewHolder holder;

		if (convertView == null || convertView.getTag() == null) {
			convertView = inflater.inflate(textViewResourceId, null);
			holder = new ViewHolder(
					(TextView) convertView.findViewById(R.id.name),
					(ImageView) convertView.findViewById(R.id.thum));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(item.name);
		holder.thum.setImageBitmap(item.getPhoto(getContext()));
		
		return convertView;
	}
}