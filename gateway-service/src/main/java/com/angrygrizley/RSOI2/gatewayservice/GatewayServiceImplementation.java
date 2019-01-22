package com.angrygrizley.RSOI2.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String reviewsServiceUrl = "http://localhost:8070";
    final private String gamesServiceUrl = "http://localhost:8071";
    final private String usersServiceUrl = "http://localhost:8072";

    @Override
    public String getUsers() throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getUserById(Long userId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/" + userId);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public void deleteUser(Long userId) throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(usersServiceUrl + "/users/" + userId);
        httpClient.execute(request);
    }

    @Override
    public String getReviewsByUser(Long userId) throws IOException {
        String url = reviewsServiceUrl + "/reviews/byuser/" + userId;
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    @Override
    public String getGamesWithReviews() throws IOException, JSONException{
        String url = gamesServiceUrl + "/games";
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));


        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        JSONArray games = new JSONArray(response.toString());

        JSONArray jsonArray = new JSONArray();


        List<String> temp = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < games.length(); i++) {
            //get the JSON Object
            JSONObject obj = games.getJSONObject(i);
            String sfname = obj.getString("id");
            // temp.add(sfname);


            url = reviewsServiceUrl +"/reviews/bygame/" + sfname;
            website = new URL(url);
            connection = website.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            response = new StringBuilder();
            StringBuilder reviews = new StringBuilder(",\n\"reviews\":\n");
            response.append(obj);
            while ((inputLine = in.readLine()) != null)
                reviews.append(inputLine);
            in.close();
            reviews.append("\n");
            response.insert(response.length()-1, reviews);
            if (i != games.length()-1)
                response.append(",");
            //jsonObject = new JSONObject(response.toString());

            result.append(response);
        }
        result.append("]");
        System.out.println();

        // RestTemplate
        // Jackson / gson
//        for (String a: temp) {
//
//        }

        System.out.print(result);

        return result.toString();
    }

    @Override
    public void addUser(String user) throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(usersServiceUrl + "/users");
        StringEntity params = new StringEntity(user);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        httpClient.execute(request);
    }


    @Override
    public void createReview(String review) throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{
            HttpPost request = new HttpPost(reviewsServiceUrl + "/reviews");
            StringEntity params = new StringEntity(review, "UTF-8");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            JSONObject obj = new JSONObject(review);
            Long gameId = obj.getLong("gameId");
            request = new HttpPost(gamesServiceUrl + "/games/" + gameId + "/add_review");
            response = httpClient.execute(request);

            HttpGet request2 = new HttpGet(reviewsServiceUrl + "/reviews/bygame/" + gameId);
            HttpResponse response2 = httpClient.execute(request2);
            String responseString2 = EntityUtils.toString(response2.getEntity(), "UTF-8");
            JSONArray revsArray = new JSONArray(responseString2);
            double rating = 0;
            for (int i = 0; i < revsArray.length(); i++){
                rating += revsArray.getJSONObject(i).getDouble("rating");
            }
            double averageRating = rating/revsArray.length();

            HttpPost request3 = new HttpPost(gamesServiceUrl + "/games/" + gameId +"/setRating/" + String.valueOf(averageRating));
            HttpResponse response3 = httpClient.execute(request3);
        } catch (Exception ex) {
            // обработка исключения
        } finally {
            httpClient.close();
        }
    }

    @Override
    public void deleteReview(Long reviewId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{
            String url = reviewsServiceUrl + "/reviews/" + reviewId;
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));


            StringBuilder game = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                game.append(inputLine);
            in.close();
            JSONObject obj = new JSONObject(game.toString());
            Long gameId = obj.getLong("gameId");


            HttpDelete request = new HttpDelete(reviewsServiceUrl + "/reviews/" + reviewId);
            HttpResponse response = httpClient.execute(request);

            HttpPost request3 = new HttpPost(gamesServiceUrl + "/games/" + gameId + "/delete_review");
            response = httpClient.execute(request3);

            HttpGet request2 = new HttpGet(reviewsServiceUrl + "/reviews/bygame/" + gameId);
            HttpResponse response2 = httpClient.execute(request2);
            String responseString2 = EntityUtils.toString(response2.getEntity(), "UTF-8");
            JSONArray revsArray = new JSONArray(responseString2);
            double rating = 0;
            for (int i = 0; i < revsArray.length(); i++){
                rating += revsArray.getJSONObject(i).getDouble("rating");
            }
            double averageRating = rating/revsArray.length();

            HttpPost request4 = new HttpPost(gamesServiceUrl + "/games/" + gameId +"/setRating/" + averageRating);
            HttpResponse response4 = httpClient.execute(request4);
        } catch (Exception ex) {
            // обработка исключения
        } finally {
            httpClient.close();
        }
    }


    @Override
    public String getReviewsForGame(Long gameId, PageRequest p) throws IOException{
        String url = reviewsServiceUrl + "/reviews/bygame/" + gameId +
                "?page=" + p.getPageNumber() + "&size=" + p.getPageSize();
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    @Override
    public String getGameById(@PathVariable Long gameId) throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/game/" + gameId);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }


    @Override
    public String getGamesByRating() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/gamesbyrating");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }
}
