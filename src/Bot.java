import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Bot {

	private Pattern patternTag, patternLink;
	private Matcher matcherTag, matcherLink;

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";

	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	private DefaultHttpClient httpclient;

	public Bot() throws Exception {

		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);

		httpclient = new DefaultHttpClient();

	}

	public void login(String mainURL, String login, String password) throws Exception {
		try {

			HttpPost httpost = new HttpPost(mainURL + "/login");
			System.out.println("login request " + httpost.getURI());
			httpost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpost.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.3");
			httpost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpost.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
			httpost.setHeader("Cache-Control", "max-age=0");
			httpost.setHeader("Connection", "keep-alive");
			httpost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			httpost.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("login", login));
			nvps.add(new BasicNameValuePair("pass", password));

			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps);
			httpost.setEntity(urlEncodedFormEntity);

			HttpResponse response = httpclient.execute(httpost);

			EntityUtils.consume(response.getEntity());

			


		} finally {
		}
	}

	public void battleDay(String mainURL) throws Exception {

		try {

			HttpGet httpget = new HttpGet(mainURL + "/b/opp");
			httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.3");
			httpget.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
			httpget.setHeader("Cache-Control", "max-age=0");
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5");

			System.out.println("executing request " + httpget.getURI());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpget, responseHandler);

			CookieStore cookieStore = httpclient.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();
				}
			}
			ArrayList<HtmlLink> listHref = grabHTMLLinks(response);

			int count = 0;
			for (HtmlLink link : listHref) {
				if (link.linkText.startsWith("Attack!") && count != 3) {
					battle(link, mainURL);
					count++;
				}
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			// httpclient.getConnectionManager().shutdown();
		}
	}

	private void battleMission(String mainURL) throws Exception {
		// TODO Auto-generated method stub
		try {
			HttpGet httpget = new HttpGet(mainURL + "/hq");
			httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.3");
			httpget.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
			httpget.setHeader("Cache-Control", "max-age=0");
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5");

			System.out.println("executing request " + httpget.getURI());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpget, responseHandler);

			CookieStore cookieStore = httpclient.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();
				}
			}
			ArrayList<HtmlLink> listHref = grabHTMLLinks(response);

			int count = 0;
			for (HtmlLink link : listHref) {
				if (link.getLink().startsWith("/b/mission") && count != 3) {
					battle(link, mainURL);
					count++;
				}
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			// httpclient.getConnectionManager().shutdown();
		}
	}

	private void battleRaid(String mainURL) throws Exception {
		// TODO Auto-generated method stub
		try {
			// httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(mainURL + "/hq");
			httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.3");
			httpget.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
			httpget.setHeader("Cache-Control", "max-age=0");
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5");

			System.out.println("executing request " + httpget.getURI());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpget, responseHandler);

			CookieStore cookieStore = httpclient.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();
					// System.out.println(name + "-" + value);
				}
			}
			ArrayList<HtmlLink> listHref = grabHTMLLinks(response);

			for (HtmlLink link : listHref) {
				if (link.getLink().startsWith("/b/raid")) {
					for (int i = 0; i < 5; i++) {
						battle(link, mainURL);
					}
				}
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			// httpclient.getConnectionManager().shutdown();
		}
	}

	public void battle(HtmlLink link, String mainURL) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(mainURL + link.getLink());
		httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.3");
		httpget.setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
		httpget.setHeader("Cache-Control", "max-age=0");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5");
		System.out.println("executing request " + httpget.getURI());
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		httpclient.execute(httpget, responseHandler);
	}

	public static void main(String arg[]) {
		try {

			// FileReader fileReader = new
			// FileReader("C://minitroopers.property");
			FileReader fileReader = new FileReader("minitroopers.property");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String twink = null;
			while ((twink = bufferedReader.readLine()) != null) {

				Bot bot = new Bot();
				try {
					StringTokenizer tokenizer = new StringTokenizer(twink, "^");
					int i = 0;
					String url = null;
					String login = null;
					String password = null;
					while (tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();
						if (i == 0) {
							url = token;
						} else if (i == 1) {
							login = token;
						} else if (i == 2) {
							password = token;
						}
						i++;
					}
					if (i > 1) {
						bot.login(url, login, password);
					}
					bot.battleMission(url);
					bot.battleDay(url);
					bot.battleRaid(url);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				finally {
					bot.httpclient.getConnectionManager().shutdown();
				}

			}

			bufferedReader.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<HtmlLink> grabHTMLLinks(final String html) {

		ArrayList<HtmlLink> result = new ArrayList<HtmlLink>();
		matcherTag = patternTag.matcher(html);
		while (matcherTag.find()) {
			String href = matcherTag.group(1); // href
			String linkText = matcherTag.group(2); // link text
			matcherLink = patternLink.matcher(href);
			while (matcherLink.find()) {
				String link = matcherLink.group(1); // link
				result.add(new HtmlLink(link, linkText));
			}
		}
		return result;
	}

}
