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
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = reviewsServiceUrl + "/reviews/byuser/" + userId;
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);

        String result = EntityUtils.toString(response.getEntity());

        return result;
    }

    @Override
    public String getGamesWithReviews() throws IOException, JSONException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = gamesServiceUrl + "/games";
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        String stringResponse = EntityUtils.toString(response.getEntity());

        JSONArray games = new JSONArray(stringResponse);

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < games.length(); i++) {
            //get the JSON Object
            JSONObject game = games.getJSONObject(i);
            String gameId = game.getString("id");
            url = reviewsServiceUrl +"/reviews/bygame/" + gameId;

            request = new HttpGet(url);
            response = httpClient.execute(request);
            String gameReviews = EntityUtils.toString(response.getEntity());

            StringBuilder gameWithReviews = new StringBuilder();
            gameWithReviews.append(game);
            gameWithReviews.insert(gameWithReviews.length()-1, gameReviews);

            if (i != games.length()-1)
                gameWithReviews.append(",");

            result.append(gameWithReviews);
        }
        result.append("]");
        System.out.println();
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
            HttpGet request = new HttpGet(reviewsServiceUrl + "/reviews" + reviewId);
            HttpResponse response = httpClient.execute(request);
            String stringResponse = EntityUtils.toString(response.getEntity());

            JSONObject obj = new JSONObject(stringResponse);
            Long gameId = obj.getLong("gameId");

            HttpDelete request1 = new HttpDelete(reviewsServiceUrl + "/reviews/" + reviewId);
            HttpResponse response1 = httpClient.execute(request1);

            HttpPost request2 = new HttpPost(gamesServiceUrl + "/games/" + gameId + "/delete_review");
            response1 = httpClient.execute(request2);

            HttpGet request3 = new HttpGet(reviewsServiceUrl + "/reviews/bygame/" + gameId);
            HttpResponse response3 = httpClient.execute(request3);
            String responseString3 = EntityUtils.toString(response3.getEntity(), "UTF-8");
            JSONArray revsArray = new JSONArray(responseString3);
            double rating = 0;
            for (int i = 0; i < revsArray.length(); i++){
                rating += revsArray.getJSONObject(i).getDouble("rating");
            }
            double averageRating = rating/revsArray.length();

            HttpPost request4 = new HttpPost(gamesServiceUrl + "/games/" + gameId +"/setRating/" + averageRating);
            response1 = httpClient.execute(request4);
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
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
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

    @Override
    public String editReview(String review) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(reviewsServiceUrl + "/reviews/edit");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String editUser(String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(reviewsServiceUrl + "/users/edit");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String editGame(String game) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(reviewsServiceUrl + "/games/edit");
        HttpResponse response = httpClient.execute(request);

        return EntityUtils.toString(response.getEntity());
    }
}
