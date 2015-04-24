package client;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import response.WeatherResponse;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by stefanlundberg on 15-04-23.
 */
public class OpenWeatherClient {

    private final String serviceBaseURL;
    private static final String APPID_HEADER = "x-api-key";
    private final String appid;
    private final HttpClient httpClient;

    public OpenWeatherClient(String serviceBaseURL, String appid, HttpClient httpClient) {
        this.serviceBaseURL = serviceBaseURL;
        this.appid = appid;
        this.httpClient = httpClient;
    }

    public WeatherResponse getWeatherAtCity (int cityId, boolean withProxy) throws IOException, JSONException {
        String subUrl = String.format(Locale.ROOT, "forecast/city?id=%d", Integer.valueOf(cityId));
        JSONObject response = doGet(subUrl, withProxy);
        return new WeatherResponse(response);
    }

    private JSONObject doGet(String subUrl, boolean withProxy) throws IOException, JSONException {
        HttpGet httpGet = new HttpGet(serviceBaseURL + subUrl);
        httpGet.addHeader(APPID_HEADER, this.appid);
        if (withProxy) {
            httpGet.setConfig(getProxyConfig());
        }
        HttpResponse response = httpClient.execute(httpGet);
        String json = EntityUtils.toString(response.getEntity());
        return new JSONObject(json);
    }

    private RequestConfig getProxyConfig() {
        HttpHost proxy = new HttpHost("pxgot2.srv.volvo.com", 8080, "http");
        return RequestConfig.custom()
                .setProxy(proxy)
                .build();
    }
}