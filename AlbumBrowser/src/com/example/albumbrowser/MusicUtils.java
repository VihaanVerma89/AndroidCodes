package com.example.albumbrowser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class MusicUtils 
{

	private static final HashMap<Long, Drawable> albumArtworkCache = new HashMap<Long, Drawable>();
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");


	public static Drawable getCachedAlbumArtwork(Context context, long albumID, BitmapDrawable defaultAlbumArtwork)
	{
			Drawable albumArtworkDrawable = null;
			
			synchronized (albumArtworkCache) 
			{
				albumArtworkDrawable = albumArtworkCache.get(albumID);
			}
			
			if(albumArtworkDrawable == null)
			{
				albumArtworkDrawable = defaultAlbumArtwork;
				
				final Bitmap defaultAlbumIcon = defaultAlbumArtwork.getBitmap();
				
				int w = defaultAlbumIcon.getWidth();
				int h = defaultAlbumIcon.getHeight();
				
				Bitmap albumBitmap = getArtworkQuick(context, albumID, w, h); 
				
				if(albumBitmap != null)
				{
					albumArtworkDrawable = new FastBitmapDrawable(albumBitmap);
					
					synchronized (albumArtworkCache)
					{
						Drawable albumDrawable = albumArtworkCache.get(albumID);
						
						if(albumDrawable == null)
						{
							albumArtworkCache.put(albumID, albumArtworkDrawable);
						}
						else
						{
							albumArtworkDrawable = albumDrawable;
						}
					}
				}
			}

			return albumArtworkDrawable;
	}
	
	private static class FastBitmapDrawable extends Drawable
	{
		private Bitmap mBitmap;
		public FastBitmapDrawable(Bitmap b)
		{
			 mBitmap = b;
		}
		
		@Override
		public void draw(Canvas canvas)
		{
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
		
		@Override
		public int getOpacity()
		{
			return PixelFormat.OPAQUE;
		}
		
		@Override
		public void setAlpha(int alpha)
		{
			
		}

		@Override
		public void setColorFilter(ColorFilter arg0) 
		{
			
		}
	}

	private static Bitmap getArtworkQuick(Context context, long albumID, int w, int h) 
	{
		// NOTE: There is in fact a 1 pixel border on the right side in the ImageView
		// used to display this drawable. Take it into account now, so we don't have to
		// scale later.
		w -= 1;
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, albumID);
		if (uri != null)
		{
			ParcelFileDescriptor fd = null;
			try
			{
				fd = res.openFileDescriptor(uri, "r");
				int sampleSize = 1;

				// Compute the closest power-of-two scale factor 
				// and pass that to sBitmapOptionsCache.inSampleSize, which will
				// result in faster decoding and better quality
				sBitmapOptionsCache.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
				int nextWidth = sBitmapOptionsCache.outWidth >> 1;
				int nextHeight = sBitmapOptionsCache.outHeight >> 1;
				while (nextWidth>w && nextHeight>h) 
				{
					sampleSize <<= 1;
					nextWidth >>= 1;
					nextHeight >>= 1;
				}

				sBitmapOptionsCache.inSampleSize = sampleSize;
				sBitmapOptionsCache.inJustDecodeBounds = false;
				Bitmap b = BitmapFactory.decodeFileDescriptor( fd.getFileDescriptor(), null, sBitmapOptionsCache);

				if (b != null) 
				{
					// finally rescale to exactly the size we need
					if (sBitmapOptionsCache.outWidth != w || sBitmapOptionsCache.outHeight != h) 
					{
						Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
						// Bitmap.createScaledBitmap() can return the same bitmap
						if (tmp != b) b.recycle();
						b = tmp;
					}
				}

				return b;
			}
			catch (FileNotFoundException e) 
			{
			} 
			finally 
			{
				try 
				{
					if (fd != null)
						fd.close();
				}
				catch (IOException e) 
				{
				}
			}
		}
		return null;
	}

}
