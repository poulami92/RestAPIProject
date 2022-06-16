package RestApiSessions;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

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
		
		RequestSpecification reqSpec= new RequestSpecBuilder()
	    .setBaseUri("https://rahulshettyacademy.com")
	    .addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		
		ResponseSpecification resSpec=new ResponseSpecBuilder()
				.expectContentType(ContentType.JSON)
				.expectStatusCode(200).build();
				
				
		 RequestSpecification request=given()
		.spec(reqSpec)
		.body(addPlaceReqBody);
		
		 Response response=request.when()
		.post("maps/api/place/add/json");
		
		 String resBody=response.then().spec(resSpec)
		.extract().asString();
		
		System.out.println(resBody);
	}

}
