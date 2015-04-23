package response;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by stefanlundberg on 15-04-23.
 */
public class WeatherResponse {

    private JSONObject jsonObject;

    public WeatherResponse(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getNames() {
        return jsonObject.names();
    }
}
