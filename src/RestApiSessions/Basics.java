package RestApiSessions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;

import files.payload;
import files.reusablemethods;

public class Basics {

	public static void main(String[] args) throws IOException {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		
// Add Place API --> Verify Response Body Parameter and Response Header Parameter ---> Store PlaceId
		
		
		 String response= given()                              
    	.log().all()
		.queryParam("key","qaclick123")
		.header("Content-Type","application/json")                      //input request header details
//		.body(payload.AddPlace())
		.body(getStringFromFile("C:\\Users\\Pd\\eclipse-workspace\\DemoProject\\src\\files\\addPlace.json"))        //Access json file directly for request payload
		
	    .when()
		.post("maps/api/place/add/json")
		
	    .then()
	    .log().all()
	    .assertThat().statusCode(200)
	    .body("scope",equalTo("APP"))                         //validating response body --> scope parameter
	    .header("server","Apache/2.4.18 (Ubuntu)")            //validation response header --> server parameter
	    .extract().asString();                                //store response body in response variable 
		 
		 System.out.println(response);

		 JsonPath js =new JsonPath(response);                 //convert string to json format
		 String placeId=js.getString("place_id");             //fetching place id from response body
		 
		 System.out.println(placeId);
		 
		 
// Update Address API ---> Verify Response Msg
		 
		 
		 String newAddress ="70 winter walk, USA";
		 	 
		 given()
		 .log().all()
	//	 .queryParam("key","qaclick123")
		 .header("Content-Type","application/json")   
		 .body("{\r\n"
		 		+ "\"place_id\":\""+placeId+"\",\r\n"
		 		+ "\"address\":\""+newAddress+"\",\r\n"
		 		+ "\"key\":\"qaclick123\"\r\n"
		 		+ "}\r\n"
		 		+ "")
		 
		 .when()
		 .put("maps/api/place/update/json")
		 
		 .then().log().all()
		 .assertThat().statusCode(200)
		 .body("msg", equalTo("Address successfully updated"));
	
		 
		 
		 
// Get Updated Address API --> Verify Address is Updated
		 
		 
		String getPlaceResponse= given().log().all()
		 .queryParam("key","qaclick123")
		 .queryParam("place_id",placeId)
//		 .header("Content-Type","application/json") 
		 
		 .when()
		 .get("/maps/api/place/get/json")
		 
		 .then().log().all()
		 .assertThat().statusCode(200)
		 .extract().asString();
//		 .body("address", equalTo(newAddress));
		
		JsonPath js1 =reusablemethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
		 
		 
	}
	
	public static String getStringFromFile(String path) throws IOException {
		
		Path p = Paths.get(path);                                              //Convert string to Path object
		String payload =new String(Files.readAllBytes(p));                     //Return contents of file in array byte format and convert it to string
		return payload;
		
	}

}
