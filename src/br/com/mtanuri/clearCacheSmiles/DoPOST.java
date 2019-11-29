package br.com.mtanuri.clearCacheSmiles;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DoPOST {

	private HashMap<String, String> cookies;
	private HashMap<String, String> formData;
	private String url;
	private Connection.Response response;
	private Document doc;

	public DoPOST(String url, HashMap<String, String> cookies, HashMap<String, String> formData)
			throws IOException {
		this.cookies = cookies;
		this.formData = formData;
		this.url = url;
		this.connect();
	}

	public void connect() throws IOException {
		String agent = "\"Mozilla/5.0 (Windows NT\" +\n"
				+ "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";

		response = Jsoup.connect(this.url).method(Connection.Method.POST).data(this.formData).cookies(this.cookies)
				.userAgent(agent).timeout(60000).execute();
		doc = response.parse();
		this.cookies.putAll(response.cookies());
	}

	public HashMap<String, String> getCookies() {
		return cookies;
	}

	public HashMap<String, String> getFormData() {
		return formData;
	}

	public String getUrl() {
		return url;
	}

	public Connection.Response getResponse() {
		return response;
	}

	public Document getDoc() {
		return doc;
	}

}
