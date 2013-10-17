package com.cst.connect.helper;

import java.util.ArrayList;
import java.util.HashMap;

public interface AsyncTaskListener {
	
	public void onTaskComplete(ArrayList<HashMap<String, String>> result);
}
