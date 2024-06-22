import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.AlexaSkillEvent;
import com.amazonaws.services.lambda.runtime.events.models.alexa.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherSkillHandler implements RequestHandler<AlexaSkillEvent, Response> {

    private static final String TABLE_NAME = "NomeDaTabela"; // Substitua pelo nome da sua tabela
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid=SUA_API_KEY";

    @Override
    public Response handleRequest(AlexaSkillEvent event, Context context) {
        String intentName = event.getRequest().getIntent().getName();
        Map<String, AttributeValue> preferredCity = null;

        try {
            preferredCity = getPreferredCity(event.getSession().getUser().getUserId());
        } catch (Exception e) {
            context.getLogger().log("Erro ao buscar cidade preferida: " + e.getMessage());
        }

        String speechOutput = "";
        String reprompt = "";
        boolean shouldEndSession = false;

        if ("GetWeatherIntent".equals(intentName)) {
            String city = event.getRequest().getIntent().getSlots().get("CitySlot").getValue();
            speechOutput = getWeatherInfo(city);

            if (preferredCity == null || !city.equals(preferredCity.get("city").getS())) {
                speechOutput += " Você gostaria de definir " + city + " como sua cidade preferida para consultas futuras?";
                reprompt = "Diga sim ou não para definir a cidade preferida.";
                shouldEndSession = false;
            } else {
                shouldEndSession = true;
            }

        } else if ("SetPreferredCityIntent".equals(intentName)) {
            String city = event.getRequest().getIntent().getSlots().get("CitySlot").getValue();
            setPreferredCity(event.getSession().getUser().getUserId(), city);
            speechOutput = "Cidade " + city + " definida como preferida. Deseja saber o clima em " + city + "?";
            reprompt = "Diga sim ou não para consultar o clima.";
            shouldEndSession = false;

        } else {
            speechOutput = "Desculpe, não entendi sua solicitação.";
        }

        return Response.builder()
                .withOutputSpeech(createPlainTextOutputSpeech(speechOutput))
                .withReprompt(createPlainTextOutputSpeech(reprompt))
                .withShouldEndSession(shouldEndSession)
                .build();
    }

    // Busca a cidade preferida do usuário no DynamoDB
    private Map<String, AttributeValue> getPreferredCity(String userId) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        GetItemRequest request = new GetItemRequest()
                .withTableName(TABLE_NAME)
                .withKey(Collections.singletonMap("userId", new AttributeValue(userId)));

        GetItemResult result = dynamoDB.getItem(request);
        return result.getItem();
    }

    // Define a cidade preferida do usuário no DynamoDB
    private void setPreferredCity(String userId, String city) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userIditem.put("city", new AttributeValue(city));

                PutItemRequest request = new PutItemRequest()
                        .withTableName(TABLE_NAME)
                        .withItem(item);

        dynamoDB.putItem(request);
    }

    // Consulta a API externa para obter as informações de clima
    private String getWeatherInfo(String city) {
        String urlString = API_URL.replace("{city}", city);
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (Scanner scanner = new Scanner(url.openStream())) {
                while (scanner.hasNext()) {
                    result.append(scanner.nextLine());
                }
            }

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(result.toString());
            String description = ((JSONObject) jsonObject.get("weather")).get("description").toString();
            double tempKelvin = (double) ((JSONObject) jsonObject.get("main")).get("temp");
            double tempCelsius = tempKelvin - 273.15; // Converte Kelvin para Celsius

            return "O clima em " + city + " é " + description + " com temperatura de " + String.format("%.1f", tempCelsius) + " graus.";

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return "Desculpe, ocorreu um erro ao obter as informações de clima.";
        }
    }

    // Cria um objeto OutputSpeech com texto simples
    private Response.OutputSpeech createPlainTextOutputSpeech(String text) {
        return Response.OutputSpeech.builder()
                .withType("PlainText")
                .withText(text)
                .build();
    }
}