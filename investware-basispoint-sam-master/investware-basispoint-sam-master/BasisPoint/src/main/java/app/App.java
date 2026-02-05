package app;

import java.util.HashMap;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {

            // convert serialized body to string[]
            if (input.getBody() == null) {
                String message = "{ \"message\": \"Invalid body\" }";
                return response
                        .withStatusCode(400)
                        .withBody(message);
            }

            String body = input.getBody();

            // Convert the stringified JSON in body to a JSON object
            JsonElement jsonBody = JsonParser.parseString(body);
            String[] bodyArray = { jsonBody.toString().replace("\"", "") };

            // Process the JSON object as needed
            // For example, you can convert it to a string array or use it directly
            RunBond runBond = new RunBond();
            String resultStr = runBond.runBond(bodyArray);

            return response
                    .withStatusCode(200)
                    .withBody(resultStr);
        } catch (Exception e) {
            return response
                    .withBody(
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Internal server error")
                    .withStatusCode(500);
        }
    }
}