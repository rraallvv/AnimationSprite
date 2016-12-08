package com.rhodylugo.animationsprite;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.VideoView;

public class MainActivity extends Activity {

	// we need a sprite bitmap to display and animate
	// frame width
	private static final int FRAME_W = 85;
	// frame height
	private static final int FRAME_H = 121;
	// number of frames
	private static final int NB_FRAMES = 14;
	// number of frames in x
	private static final int COUNT_X = 5;
	// number of frames in y
	private static final int COUNT_Y = 3;
	// frame duration
	private static final int FRAME_DURATION = 120; // in ms !
	// scale factor for each frame
	private static final int SCALE_FACTOR = 5;
	private ImageView img;
	// stores each frame
	private Bitmap[] bmps;
	private VideoView video;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        video = (VideoView) findViewById(R.id.videoView);

		// load video from raw assets folder
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        video.setVideoURI(uri);
        video.start();  
		
		img = (ImageView) findViewById(R.id.img);
		
		// load bitmap from assets
		Bitmap birdBmp = getBitmapFromAssets(this, "grossini_dance.png");
		
		if (birdBmp != null) {
			// cut bitmaps from bird to array of bitmaps
			bmps = new Bitmap[NB_FRAMES];
			int currentFrame = 0;
			
			for (int i = 0; i < COUNT_Y; i++) {
				for (int j = 0; j < COUNT_X; j++) {
					bmps[currentFrame] = Bitmap.createBitmap(birdBmp, FRAME_W * j, FRAME_H * i, FRAME_W, FRAME_H);
					
					// apply scale factor
					bmps[currentFrame] = Bitmap.createScaledBitmap(bmps[currentFrame], FRAME_W * SCALE_FACTOR, FRAME_H * SCALE_FACTOR, true);
					
					if (++currentFrame >= NB_FRAMES) {
						break;
					}
				}				
			}
			
			// create animation programmatically
			final AnimationDrawable animation = new AnimationDrawable();
			animation.setOneShot(false); // repeat animation
			
			for (int i = 0; i < NB_FRAMES; i++) {
				animation.addFrame(new BitmapDrawable(getResources(), bmps[i]), FRAME_DURATION);
			}
			
			// load animation on image
			if (Build.VERSION.SDK_INT < 16) {
				img.setBackgroundDrawable(animation);
			} else {
				img.setBackground(animation);
			}
			
			// start animation on image
			img.post(new Runnable() {
				
				@Override
				public void run() {
					animation.start();
				}
				
			});
		}
	}

	private Bitmap getBitmapFromAssets(MainActivity mainActivity, String filepath) {
		AssetManager assetManager = mainActivity.getAssets();
		InputStream istr = null;
		Bitmap bitmap = null;
		
		try {
			istr = assetManager.open(filepath);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException ioe) {
			// manage exception
		} finally {
			if (istr != null) {
				try {
					istr.close();
				} catch (IOException e) {
				}
			}
		}
		
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
