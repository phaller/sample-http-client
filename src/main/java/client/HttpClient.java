package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/*
Links:
 
# Spark

http://sparkjava.com/documentation
http://sparkjava.com/tutorials/maven-setup
https://github.com/perwendel/spark/blob/master/README.md#examples
https://github.com/perwendel/spark

# Apache HttpClient

http://hc.apache.org/httpcomponents-client-ga/quickstart.html
http://hc.apache.org/httpcomponents-client-ga/examples.html
https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/
http://hc.apache.org/httpcomponents-client-ga/tutorial/html/

# Gson

current: 2.8.0

https://github.com/google/gson
https://github.com/google/gson/blob/master/UserGuide.md
 
Book:
https://www.coursera.org/learn/algorithms-part1
https://www.coursera.org/learn/algorithms-part2
http://algs4.cs.princeton.edu/home/
*/
class OurResponseHandler implements ResponseHandler<String> {
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		System.out.println("status line is: " + response.getStatusLine().toString());

		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Error!");
			return "" + response.getStatusLine().getStatusCode();
		} else {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		}
	}
}

public class HttpClient {

	// Utility object for converting to and from JSON
	private static Gson gson = new Gson();

	private static void sendGetRequestWithParameters(CloseableHttpClient httpclient) throws ClientProtocolException, IOException {
		try {
			URI uri = new URIBuilder()
				.setScheme("http")
		        .setHost("localhost")
		        .setPort(4567)
		        .setPath("/book")
		        .setParameter("author", "fine")
		        .build();
			HttpGet httpGet = new HttpGet(uri);

			ResponseHandler<String> responseHandler = new OurResponseHandler();

			System.out.println("Now sending the HTTP GET request...");
			String responseBody = httpclient.execute(httpGet, responseHandler);
			System.out.println("--------");
			System.out.println("response body: " + responseBody);

			// Book.class has type Class<Book> (package java.lang)
			Book receivedBook = gson.fromJson(responseBody, Book.class);

			System.out.println("The title is: " + receivedBook.getTitle());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	private static void sendGetRequest(CloseableHttpClient httpclient) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet("http://localhost:4567/hello");

		ResponseHandler<String> responseHandler = new OurResponseHandler();

		System.out.println("Now sending the HTTP GET request...");
		String responseBody = httpclient.execute(httpGet, responseHandler);
		System.out.println("--------");
		System.out.println("response body: " + responseBody);

		// Book.class has type Class<Book> (package java.lang)
		Book receivedBook = gson.fromJson(responseBody, Book.class);

		System.out.println("The title is: " + receivedBook.getTitle());		
	}

	private static void sendPostRequest(CloseableHttpClient httpclient) throws ClientProtocolException, IOException {
		System.out.println("NOW we're doing a POST request! :-)");

		HttpPost httppost = new HttpPost("http://localhost:4567/addbook");

		Book bookToAdd = new Book("Sedgewick", "Algorithms", 500);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("book", gson.toJson(bookToAdd)));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nvps));

			System.out.println("Now sending the HTTP POST request...");

			String responseBody = httpclient.execute(httppost, response -> {
				// we receive the new ID for the book
				if (response.getStatusLine().getStatusCode() != 200) {
					System.out.println("Error!");
					return "" + response.getStatusLine().getStatusCode();
				} else {
					HttpEntity entity = response.getEntity();
					return EntityUtils.toString(entity);
				}
			});
			
			System.out.println("--------");
			System.out.println("ID of newly created book: " + responseBody);
		} catch (UnsupportedEncodingException e) {
			System.out.print("Could not encode parameters for POST request");
		}
	}

	public static void main(String[] args) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			sendGetRequest(httpclient);
			sendGetRequestWithParameters(httpclient);
			sendPostRequest(httpclient);
		} catch (ClientProtocolException cpe) {
			System.out.println("Problem executing HTTP request");
			cpe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Problem executing HTTP request");
			ioe.printStackTrace();			
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				System.out.println("Problem closing HTTP client");
				e.printStackTrace();
			}
		}
		
	}

}
