package br.com.mtanuri.clearCacheSmiles;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DoGET {

	private HashMap<String, String> cookies;
	private String url;
	private Document doc;
	private Connection.Response response;

	public DoGET(String url, HashMap<String, String> cookies) throws IOException {
		this.url = url;
		this.cookies = cookies;
		this.connect();
	}

	public DoGET(String url) throws IOException {
		this.url = url;
		this.cookies = new HashMap<String, String>();
		this.connect();
	}

	public void connect() throws IOException {
		String agent = "\"Mozilla/5.0 (Windows NT\" +\n"
				+ "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";

		response = Jsoup.connect(url).method(Connection.Method.GET).userAgent(agent).timeout(60000).execute();
		doc = response.parse();
		this.cookies.putAll(response.cookies());
	}

	public HashMap<String, String> getCookies() {
		return cookies;
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
