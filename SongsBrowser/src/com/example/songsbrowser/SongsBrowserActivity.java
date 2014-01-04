package com.example.songsbrowser;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SongsBrowserActivity extends ListActivity 
{
	private ListView mSongList;
	
	private SongListAdapter mSongListAdapter;
	
	private String mSortOrder;
	private String [] mSongCursorColumns = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION	
	} ;
	
	private Cursor mSongsCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_songs_browser);
		
		mSongListAdapter = (SongListAdapter) getLastNonConfigurationInstance();
		
		if(mSongListAdapter != null)
		{
			mSongListAdapter.setActivity(this);
			setListAdapter(mSongListAdapter);
		}
		else
		{
			mSongListAdapter = new SongListAdapter(getApplication(), this, R.layout.song_list_item, null, new String [] {}, new int [] {});
			
			setListAdapter(mSongListAdapter);
			setTitle("Songs");
			setSongsCursor(mSongListAdapter.getSongsQueryHandler(), null, true);
		}
	
	}
	
	   private Cursor setSongsCursor(SongListAdapter.SongsQueryHandler queryhandler, String filter, boolean async) 
	   {

	        if (queryhandler == null) 
	        {
	            throw new IllegalArgumentException();
	        }

	        Cursor songsCursor = null;
	        mSortOrder = MediaStore.Audio.Media.TITLE_KEY;
	        StringBuilder where = new StringBuilder();
	        where.append(MediaStore.Audio.Media.TITLE + " != ''");

            where.append(" AND " + MediaStore.Audio.Media.IS_MUSIC + "=1");
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            if (!TextUtils.isEmpty(filter)) 
            {
                uri = uri.buildUpon().appendQueryParameter("filter", Uri.encode(filter)).build();
            }

            songsCursor = queryhandler.doQuery(uri, mSongCursorColumns, where.toString() , null, mSortOrder, async);

	        if (songsCursor != null && async) 
	        {
	            initializeSongsCursor(songsCursor, false);
	        }
	        return songsCursor;
	    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.songs_browser, menu);
		return true;
	}
	
	public void initializeSongsCursor(Cursor songsCursor, boolean isLimited)
	{
		if( mSongListAdapter == null)
		{
			return ;
		}
		
		mSongListAdapter.changeCursor(songsCursor);
	}

//	static class SongListAdapter extends SimpleCursorAdapter implements SectionIndexer
	static class SongListAdapter extends SimpleCursorAdapter 
	{

		private SongsBrowserActivity mSongsBrowserActivity = null;
		private SongsQueryHandler mSongsQueryHandler;
		
		int mSongNameIndex;
		int mArtistNameIndex;
		int mDurationIndex;
		int mAudioIdIndex;
		
		private final String mUnknownArtist;
		private final String mUnknownAlbum;

		public SongListAdapter(Context context,SongsBrowserActivity activity, int layout, Cursor songsCursor, String[] from, int[] to) 
		{
			super(context, layout, songsCursor, from, to);
			
			mSongsBrowserActivity = activity;
			setColumnIndex(songsCursor);
			mSongsQueryHandler = new SongsQueryHandler(context.getContentResolver());
			
			mUnknownAlbum = context.getString(R.string.unknown_album_name);
			mUnknownArtist = context.getString(R.string.unknown_artist_name);
		}
		
		public void setColumnIndex(Cursor songsCursor)
		{
			if(songsCursor != null)
			{
				mSongNameIndex = songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
				mArtistNameIndex = songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				mDurationIndex = songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

				try
				{
					mAudioIdIndex = songsCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID);
				}
				catch(IllegalArgumentException ex)
				{
					mAudioIdIndex = songsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
				}
			}
		}
		
		public SongsQueryHandler getSongsQueryHandler()
		{
			return mSongsQueryHandler;
		}
		
		public void setActivity(SongsBrowserActivity activity)
		{
			this.mSongsBrowserActivity = activity;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			View songView = super.newView(context, cursor, parent);
			SongViewHolder songViewHolder = new SongViewHolder();
			
			songViewHolder.mSongName = (TextView) songView.findViewById(R.id.songName);
			songViewHolder.mArtistName = (TextView) songView.findViewById(R.id.artistName);
			songViewHolder.mDuration = (TextView) songView.findViewById(R.id.songDuration);
			
			songView.setTag(songViewHolder);
			
			return songView;
		}

		@Override
		public void bindView(View songView, Context context, Cursor songsCursor) 
		{
			SongViewHolder songViewHolder = (SongViewHolder)  songView.getTag();
			
			int seconds = songsCursor.getInt(mDurationIndex) / 1000;
			
			if(seconds == 0)
			{
				songViewHolder.mDuration.setText("");
			}
			else
			{
				songViewHolder.mDuration.setText(MusicUtils.makeTimeString(context, seconds));
			}
			
			String songName = songsCursor.getString(mSongNameIndex);
			if(songName == null || songName.equals(MediaStore.UNKNOWN_STRING))
			{
				songViewHolder.mSongName.setText("Unknown Song");
			}
			else
			{
				songViewHolder.mSongName.setText(songName);
			}
			
			String artistName = songsCursor.getString(mArtistNameIndex);
			
			if(artistName == null || artistName.equals(MediaStore.UNKNOWN_STRING))
			{
				songViewHolder.mArtistName.setText(mUnknownArtist);
			}
			else
			{
				songViewHolder.mArtistName.setText(artistName);
			}

		}
		
		@Override
		public void changeCursor(Cursor songsCursor)
		{
			if(songsCursor != mSongsBrowserActivity.mSongsCursor)
			{
				mSongsBrowserActivity.mSongsCursor = songsCursor;
				super.changeCursor(songsCursor);
				setColumnIndex(songsCursor);
			}
		}
		
		static class SongViewHolder
		{
			TextView mSongName;
			TextView mArtistName;
			TextView mDuration;
		}
		
		class SongsQueryHandler extends AsyncQueryHandler
		{
			
			class QueryArgs
			{
				public Uri uri;
				public String [] projection;
				public String selection;
				public String [] selectionArgs;
				public String orderBy;
			}

			public SongsQueryHandler(ContentResolver cr)
			{
				super(cr);
			}
			
			public Cursor doQuery(Uri uri, String [] projection, String selection, String [] selectionArgs, 
					String orderBy, boolean async)
			{
				if(async)
				{
					Uri limitUri = uri.buildUpon().appendQueryParameter("limit", "100").build();
					
					QueryArgs args = new QueryArgs();
					
					args.uri = uri;
					args.projection = projection;
					args.selection = selection;
					args.selectionArgs = selectionArgs;
					args.orderBy = orderBy;

//					startQuery(0, args, limitUri, projection, selection, selectionArgs, orderBy);
					startQuery(0, args, uri, projection, selection, selectionArgs, orderBy);
					
					return null;
				}
				
				return MusicUtils.query(mSongsBrowserActivity, uri, projection, selection, selectionArgs, orderBy);
			}
			
			@Override
			protected void onQueryComplete(int token, Object cookie, Cursor cursor)
			{
				mSongsBrowserActivity.initializeSongsCursor(cursor, cookie != null);
			}
		}
	
	}
}
