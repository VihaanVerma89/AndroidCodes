package com.example.albumbrowser;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AlbumBrowserActivity extends ListActivity 
{

	private AlbumListAdapter mAlbumListAdapter;
	private Cursor mAlbumCursor;
	
	private static int mListPositionStart = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_browser);
		
		mAlbumListAdapter = (AlbumListAdapter) getLastNonConfigurationInstance();
		
		if(mAlbumListAdapter == null)
		{
			mAlbumListAdapter = new AlbumListAdapter(getApplication(), this, R.layout.album_row, mAlbumCursor, 
					new String[] {}, new int [] {});
			
			setListAdapter(mAlbumListAdapter);
			setTitle(R.string.working_albums);
			startCursorQuery(mAlbumListAdapter.getQueryHandler());
		}
		else
		{
//			mAlbumListAdapter.setActivity(this);
//			setListAdapter(mAlbumListAdapter);
//			startCursorQuery(mAlbumListAdapter.getQueryHandler());
		}
	}

	@Override
	public void onDestroy()
	{
		ListView albumListView = getListView();
		
		if(albumListView != null)
		{
			mListPositionStart = albumListView.getFirstVisiblePosition();
		}
		
		super.onDestroy();
	}
	
   public void initializeAlbumCursor(Cursor albumCursor)
    {
    	mAlbumListAdapter.changeCursor(albumCursor);
    	
    	if(mListPositionStart >= 0)
    	{
    		getListView().setSelectionFromTop(mListPositionStart, 0);
    	}
    }

   
   private void startCursorQuery(AsyncQueryHandler async)
   {
       String[] cols = new String[] {
               MediaStore.Audio.Albums._ID,
               MediaStore.Audio.Albums.ARTIST,
               MediaStore.Audio.Albums.ALBUM,
               MediaStore.Audio.Albums.ALBUM_ART
       };

       Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
       async.startQuery(0, null, uri, cols, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
   }
   
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) 
//	{
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.album_browser, menu);
//		return true;
//	}
//	

 
    
	
	static class AlbumListAdapter extends SimpleCursorAdapter
	{
		private int mAlbumIndex;
		private int mArtistIndex;
		private int mAlbumArtIndex;
		
		private final String mUnknownAlbum;
		private final String mUnknownArtist;

		private final BitmapDrawable mDefaultAlbumIcon;

		private AlbumBrowserActivity mAlbumBrowserActivity;
		
		private AsyncQueryHandler mAsyncQueryHandler;

		static class AlbumViewHolder
		{
			TextView albumName;
			TextView artistName;
			ImageView albumIcon;
		}
		
        class QueryHandler extends AsyncQueryHandler 
        {
            QueryHandler(ContentResolver res) 
            {
                super(res);
            }
            
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) 
            {
                //Log.i("@@@", "query complete");
//            	mAlbumBrowserActivity.mListView.invalidateViews();
                mAlbumBrowserActivity.initializeAlbumCursor(cursor);
            }
        }
        
        public AsyncQueryHandler getQueryHandler()
        {
        	return mAsyncQueryHandler;
        }
        
        

		public void setActivity(AlbumBrowserActivity albumBrowserActivity) 
		{
			mAlbumBrowserActivity = albumBrowserActivity;
		}



		//		public AlbumListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to) 
		public AlbumListAdapter(Context context, AlbumBrowserActivity albumBrowserActivity, int layout, Cursor cursor, String[] from, int[] to) 
		{
			super(context, layout, cursor, from, to);
			
			mAsyncQueryHandler = new QueryHandler(context.getContentResolver());
			mAlbumBrowserActivity = albumBrowserActivity;
			
			mUnknownAlbum = context.getString(R.string.unknown_album_name);
			mUnknownArtist = context.getString(R.string.unknown_artist_name);
			
			Resources resources = context.getResources();
			Bitmap defaultAlbumBitmap = BitmapFactory.decodeResource(resources, R.drawable.unknown_album);
			mDefaultAlbumIcon = new BitmapDrawable(context.getResources(), defaultAlbumBitmap);
			mDefaultAlbumIcon.setFilterBitmap(false);
			mDefaultAlbumIcon.setDither(false);
			
			setColumnIndices(cursor);
		}
		
		private void setColumnIndices(Cursor cursor)
		{
			if(cursor != null)
			{
				mAlbumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
				mArtistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
				mAlbumArtIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART);
			}
		}
		
		@Override
		public void changeCursor(Cursor cursor)
		{
			mAlbumBrowserActivity.mAlbumCursor = cursor;
			setColumnIndices(cursor);
			super.changeCursor(cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) 
		{
			// TODO Auto-generated method stub
			AlbumViewHolder albumViewHolder= (AlbumViewHolder) view.getTag();
			
			String albumName = cursor.getString(mAlbumIndex);
			String albumDisplayName = albumName;
			boolean albumNameIsUnkown = albumName == null || albumName.equals(MediaStore.UNKNOWN_STRING);
			
			if(albumNameIsUnkown)
			{
				albumDisplayName =  mUnknownAlbum;
			}
			
			albumViewHolder.albumName.setText(albumDisplayName);
			
			String artistName = cursor.getString(mArtistIndex);
			String artistDisplayName = artistName;
			
			boolean artistIsUnknown = artistName == null || artistName.equals(MediaStore.UNKNOWN_STRING);
			
			if(artistIsUnknown)
			{
				artistDisplayName = mUnknownArtist;
			}
			
			albumViewHolder.artistName.setText(artistDisplayName);
			
			ImageView albumImage = albumViewHolder.albumIcon;
			
			String albumArt = cursor.getString(mAlbumArtIndex);
			
			long albumID = cursor.getLong(0);
			
			if(albumNameIsUnkown || albumArt == null || albumArt.length() == 0)
			{
				albumImage.setImageDrawable(null);
			}
			else
			{
				Drawable albumArtDrawable = MusicUtils.getCachedAlbumArtwork(context, albumID, mDefaultAlbumIcon);
				albumImage.setImageDrawable(albumArtDrawable);
			}
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) 
		{
			View albumRow = super.newView(context, cursor, parent);
			
			AlbumViewHolder albumViewHolder = new AlbumViewHolder();
			
			albumViewHolder.albumIcon = (ImageView)albumRow.findViewById(R.id.albumIcon);
			albumViewHolder.albumName = (TextView)albumRow.findViewById(R.id.albumName);
			albumViewHolder.artistName = (TextView)albumRow.findViewById(R.id.artistName);
			
			albumViewHolder.albumIcon.setBackgroundDrawable(mDefaultAlbumIcon);
			albumViewHolder.albumIcon.setPadding(0, 0, 1, 0);
			
			albumRow.setTag(albumViewHolder);
			return albumRow;
		}
		
	}

}

