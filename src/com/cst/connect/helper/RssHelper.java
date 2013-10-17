package com.cst.connect.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RssHelper {

	String filename, url;
	boolean flag;
	Context mContext;

	public RssHelper(Context context) {
		this.mContext = context;
	}

	public String convertDate(String pubDate) {

		DateFormat inputFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = null;
		try {
			convertedDate = inputFormat.parse(pubDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return outputFormat.format(convertedDate);

	}

	public boolean attachedFile(String message) {

		if (message.contains("title='Επισυναπτόμενο αρχείο'>")) {
			return true;
		}

		return false;
	}

	public boolean hasAttachment(String message, String rss) {
		if (rss.equals("http://www.cs.teilar.gr/CS/rss.jsp")) {
			if (message.contains("<BR />")) {

				int start = message.indexOf("HREF='") + 6;
				int start_file = message.indexOf("News/") + 5;
				int end = message.indexOf("' title");
				filename = message.substring(start_file, end);
				url = message.substring(start, end);

				String pattern = "(?i)(<BR />)(.+?)(</A>)";
				message = message.replaceAll(pattern, "");

				flag = true;

			} else {
				flag = false;
			}

		} else if (rss.contains("http://www.cs.teilar.gr/CS/rss.jsp?id=")) {

			if (message.contains("<BR />")) {

				int start = message.indexOf("HREF='") + 6;
				int start_file = message.indexOf("Ann/") + 4;
				int end = message.indexOf("' title");
				filename = message.substring(start_file, end);

				url = message.substring(start, end);
				String pattern = "(?i)(<BR />)(.+?)(</A>)";
				message = message.replaceAll(pattern, "");
				flag = true;

			} else {
				flag = false;
			}

		}
		return flag;
	}

	public String getUrl() {
		return url;
	}

	public String getFilename() {
		return filename;
	}

	public String xmlCleansing(String message, String rss) {

		if (rss.equals("http://www.cs.teilar.gr/CS/rss.jsp")) {

			Pattern main_rss = Pattern
					.compile("(?i)(?s)(normal)(.+?)(0400\\;\\})");
			Matcher mmain_rss = main_rss.matcher(message);
			message = mmain_rss.replaceAll("");

		} else if (rss.contains("http://www.cs.teilar.gr/CS/rss.jsp?id=")) {

			Pattern stulianos = Pattern
					.compile("(?i)(?s)(normal)(.+?)(0400\\;\\})");
			Matcher mstulianos = stulianos.matcher(message);
			message = mstulianos.replaceAll("");

			Pattern vlachos1 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(latin\\;\\})");
			Matcher mvlachos1 = vlachos1.matcher(message);
			message = mvlachos1.replaceAll("");

			Pattern vlachos2 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(-US\\;\\})");
			Matcher mvlachos2 = vlachos2.matcher(message);
			message = mvlachos2.replaceAll("");

			Pattern vlachos3 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(JA\\;\\})");
			Matcher mvlachos3 = vlachos3.matcher(message);
			message = mvlachos3.replaceAll("");

			Pattern vlachos4 = Pattern
					.compile("(?i)(?s)(function)(.+?)(src\\;\\})");
			Matcher mvlachos4 = vlachos4.matcher(message);
			message = mvlachos4.replaceAll("");

			Pattern ventzas = Pattern
					.compile("(?i)(?s)(normal)(.+?)(Roman\"\\;\\})");
			Matcher mventzas = ventzas.matcher(message);
			message = mventzas.replaceAll("");

		}

		if (message.length() == 0) {
			message = message.replaceAll("", "Δεν υπάρχει κείμενο ανακοίνωσης");
		}

		message = Jsoup.parse(message.replaceAll("\n", "br2nl")).text();
		message = message.replaceAll("(br2nl)++", "\n")
				.replaceAll("(br2nl)++", "\n").trim();

		return message;

	}

	public Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

	// public void folderNameSet(){
	//
	// SharedPreferences folder = getSharedPreferences("Folder", 0);
	// SharedPreferences.Editor editor = folder.edit();
	// editor.putString("Folder","CST Connect Downloads");
	// editor.commit();
	// }
}
