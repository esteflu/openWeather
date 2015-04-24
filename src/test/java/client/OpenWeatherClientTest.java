package client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import response.WeatherResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stefanlundberg on 15-04-23.
 */
public class OpenWeatherClientTest {

    private final static String realURL = "http://api.openweathermap.org/data/2.5/";
    private final static String stubURL = "http://localhost:1111/data/2.5/";
    private final static String appid = "1bd046ecb31e6e86ab00c3fc60283bd9";
    private final int GOTH_CITY_ID = 2689287;
    private OpenWeatherClient openWeatherClient;
    private JSONArray expected;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(1111);

    @Before
    public void setup() {
        expected = buildExpectedJSONArray();
    }


    /*
     * This test will call the real webservice
     */
    @Test
    public void get_real_weather_forecast_in_gothenburg() throws IOException, JSONException {
        //Given
        openWeatherClient = new OpenWeatherClient(realURL, appid, HttpClientBuilder.create().build());

        //When
        WeatherResponse weatherAtCity = openWeatherClient.getWeatherAtCity(GOTH_CITY_ID, true);

        //Then
        assertEquals(expected.length(), weatherAtCity.getNames().length());
    }

    /*
     * This test will call a stubbed version of the real webservice
     */
    @Test
    public void get_stubbed_weather_forecast_in_gothenburg() throws IOException, JSONException {
        //Given
        configureStubServer();
        openWeatherClient = new OpenWeatherClient(stubURL, appid, HttpClientBuilder.create().build());

        //When
        WeatherResponse weatherAtCity = openWeatherClient.getWeatherAtCity(GOTH_CITY_ID, false);

        //Then
        assertEquals(expected.length(), weatherAtCity.getNames().length());
    }

    private void configureStubServer() {
        stubFor(get(urlMatching("/data/2.5/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{ \"city\": \"city_value\", " +
                                    "\"cnt\": \"cnt_value\", " +
                                    "\"cod\": \"cod_value\", " +
                                    "\"message\": \"message_value\", " +
                                    "\"list\": \"list_value\" }")));
    }

    private JSONArray buildExpectedJSONArray() {
        return new JSONArray()
                .put("city")
                .put("cnt")
                .put("cod")
                .put("message")
                .put("list");
    }
}
