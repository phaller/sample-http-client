package client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class OurPostResponseHandler implements ResponseHandler<String> {

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		// we receive the new ID for the book
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Error!");
			return "" + response.getStatusLine().getStatusCode();
		} else {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		}
	}

}
