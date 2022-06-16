package pojo;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

public class serializeTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		addPlace addPlaceReqBody = new addPlace();
		
		Location loc = new Location();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		
		ArrayList<String> types = new ArrayList<String>();
		types.add("shoe park");
		types.add("shop");
		
		addPlaceReqBody.setLocation(loc);
		addPlaceReqBody.setAccuracy(50);
		addPlaceReqBody.setName("Asansol Inn");
		addPlaceReqBody.setPhone_number("993308563");
		addPlaceReqBody.setAddress("Asansol");
		addPlaceReqBody.setTypes(types);
		addPlaceReqBody.setWebsite("http://rahulshettyacademy.com");
		addPlaceReqBody.setLanguage("French-IN");
		
		String response=given()
		.queryParam("key", "qaclick123")
		.body(addPlaceReqBody)
		
		.when()
		.post("maps/api/place/add/json")
		
		.then().assertThat().statusCode(200)
		.extract().asString();
		
		System.out.println(response);
	}

}
