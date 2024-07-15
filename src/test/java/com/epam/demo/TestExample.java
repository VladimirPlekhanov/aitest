package com.epam.demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

public class TestExample {

    // 1. Shuffle new deck using 'https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1'
    // 2. Verify response code is 200
    // 3. Verify response matches pre-defined values after the calling endpoint
    //{
    //    "success": true,
    //    "deck_id": "3p40paa87x90",
    //    "shuffled": true,
    //    "remaining": 52
    //}
    // Response requirements:
    // 'deck_id' is a String UUID with the length 12
    // 'success' and 'shuffled' are boolean and equal to true in case operation was successful
    // remaining is integer and equals for a deck size 52 cards
    @Test(description = "Shuffle the Cards")
    public void shuffleCardsTest() {
        SoftAssertions softAssertions = new SoftAssertions();
        // Call: https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1
        Response response = RestAssured.given()
                                       .baseUri("https://deckofcardsapi.com/api")
                                       .basePath("deck/new/shuffle")
                                       .queryParam("deck_count", 1)
                                       .get()
                                       .then()
                                       .log().all().extract().response();
        // verify response code is 200
        Assertions.assertThat(response.getStatusCode())
                  .as("Incorrect status code")
                  .isEqualTo(HttpStatus.SC_OK);
        JsonPath responseJson = response.jsonPath();
        // Response:
        //{
        //    "success": true,
        //    "deck_id": "3p40paa87x90",
        //    "shuffled": true,
        //    "remaining": 52
        //}

        // 'success' and 'shuffled' are equal to true in case operation was successful
        softAssertions.assertThat(responseJson.getBoolean("success"))
                      .as("'success' field has incorrect value")
                      .isEqualTo(true);
        // 'deck_id' is a String UUID with the length 12
        softAssertions.assertThat(responseJson.getString("deck_id"))
                      .as("'deck_id' field has incorrect value")
                      .hasSize(12);
        // 'success' and 'shuffled' are equal to true in case operation was successful
        softAssertions.assertThat(responseJson.getBoolean("shuffled"))
                      .as("'shuffled' field has incorrect value")
                      .isEqualTo(true);
        // 'remaining' is integer and equals for a deck size 52 cards
        softAssertions.assertThat(responseJson.getInt("remaining"))
                      .as("'remaining' field has incorrect value")
                      .isEqualTo(52);
        softAssertions.assertAll();
    }
}
