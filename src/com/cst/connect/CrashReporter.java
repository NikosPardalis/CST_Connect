package com.cst.connect;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import android.app.Application;

//acra_users_id == dDJob3VmcXFVUTBZYW1CWWN3UVczVkE6MQ
//acra_debug_id == dEY0VWxVZWo5NDlBbmVJMFU2aU11NFE6MQ

@ReportsCrashes(formKey = "dEY0VWxVZWo5NDlBbmVJMFU2aU11NFE6MQ") 
public class CrashReporter extends Application{
	
	@Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
}
