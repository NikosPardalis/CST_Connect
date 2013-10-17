package com.cst.connect;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

//acra_users_id == dDJob3VmcXFVUTBZYW1CWWN3UVczVkE6MQ
//acra_debug_id == dEY0VWxVZWo5NDlBbmVJMFU2aU11NFE6MQ

@ReportsCrashes(
formKey = "",
formUri = "https://pardalis.cloudant.com/acra-cstconnect/_design/acra-storage/_update/report",
reportType = org.acra.sender.HttpSender.Type.JSON,
httpMethod = org.acra.sender.HttpSender.Method.PUT,
formUriBasicAuthLogin="thertillartogentseedless",
formUriBasicAuthPassword="XNbi1KUAWDEfxCO42IYyglsP")
public class AppInitializer extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		
		// The following line triggers the singleton of Universal Image Loader
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
