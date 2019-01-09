package it.fabrix.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author fabrix
 */
public class HttpClient {

	private final String USER_AGENT = "Mozilla/5.0";

	public String sendGet(String host, int timeout) throws Exception {
		URL url = new URL(String.format("https://%s", host));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(timeout);
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		return String.format("%s %s %s",con.getRequestMethod(), con.getResponseCode(), con.getResponseMessage());
	}

	public String sendJsonPost(String urlString, String json) throws IOException {
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(json);
		wr.flush();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

}
