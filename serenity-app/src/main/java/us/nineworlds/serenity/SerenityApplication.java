/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity;

import java.util.concurrent.ConcurrentHashMap;

import org.teleal.cling.model.meta.Device;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.serenity.core.ConcurrentLoader;
import us.nineworlds.serenity.core.SerenityLoaderSettings;
import us.nineworlds.serenity.core.ServerConfig;

import com.google.analytics.tracking.android.EasyTracker;
import com.novoda.imageloader.core.ImageManager;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Global manager for the Serenity application
 * 
 * @author dcarver
 *
 */
public class SerenityApplication extends Application {
	
	private static ImageManager imageManager;
	private static PlexappFactory plexFactory;
	private static SerenityLoaderSettings settings;
	
	private static ConcurrentHashMap<String, Device> plexmediaServers = new ConcurrentHashMap<String, Device>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		EasyTracker.getInstance().setContext(this);

		settings = new SerenityLoaderSettings.SettingsBuilder()
	      .withDisconnectOnEveryCall(true).withUpsampling(true).build(this);
		settings.setLoader(new ConcurrentLoader(settings));
		
	    imageManager = new ImageManager(this, settings);
	    
	    // Temporarily clean the cache
	    //imageManager.getFileManager().clean();
	    
	    IConfiguration config = ServerConfig.getInstance(this);
		plexFactory = PlexappFactory.getInstance(config);
		String deviceModel = android.os.Build.MODEL;
		
		EasyTracker.getTracker().sendEvent("Devices", "Started Application", deviceModel, (long)0);
	}
	
	public static ImageManager getImageManager() {
	    return imageManager;
	}
	
	public static PlexappFactory getPlexFactory() {
		return plexFactory;
	}
	
	public static SerenityLoaderSettings getLoaderSettings() {
		return settings;
	}
	
	public static boolean isGoogleTV(Context context) {
	    final PackageManager pm = context.getPackageManager();
	    return pm.hasSystemFeature("com.google.android.tv");
	}
	
	public static ConcurrentHashMap<String, Device> getPlexMediaServers() {
		return plexmediaServers;
	}
	
	
}