package com.cst.connect.helper;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

public class RssParser {

	Context mContext;

	public RssParser(Context context) {
		this.mContext = context;
	}
	
	public String getXML(HttpPost httpPost) {

		String line = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			line = "Internet Connection Error >> " + e.getMessage();
		}
		return line;
	}
	
	public final Document XMLfromString(String xml) {

		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			// if (UTF8) {
			/* Κώδικας για ανακοινωσεις teilar */
			// ByteArrayInputStream encXML = new ByteArrayInputStream(
			// xml.getBytes("ISO-8859-1"));
			// doc = db.parse(encXML);
			// } else {
			/* Κώδικας για ανακοινωσεις cs.teilar */
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);
			// }

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
		return doc;

	}

	public String getElementValue(Node elem) {
		Node kid;

		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (kid = elem.getFirstChild(); kid != null;) {
					Log.d("node Value", kid.getNodeValue());
					return kid.getNodeValue();
				}
			}
		}

		return "";
	}

	

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return getElementValue(n.item(0));
	}

}
