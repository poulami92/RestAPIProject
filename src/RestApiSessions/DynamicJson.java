package RestApiSessions;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payload;
import files.reusablemethods;


public class DynamicJson {
	
public static String id;	
	
  @Test(dataProvider="getData")
  public void addBook(String book,String no) {
	  
	  RestAssured.baseURI = "http://216.10.245.166";
	  
	  String response =given().log().all()
	  .header("Content-Type","application/json")                      
	  .body(payload.AddBook(book,no))
	  
	  .when()
	  .post("Library/Addbook.php")
	  
	  .then().log().all()
	  .assertThat().statusCode(200)
	  .extract().asString();
	  
	  JsonPath js = reusablemethods.rawToJson(response);
	  id =js.getString("ID");
	  
	  System.out.println(id);
  }
  
	/*
	 * @Test public void deleteBook() {
	 * 
	 * RestAssured.baseURI = "http://216.10.245.166";
	 * 
	 * given().log().all() .header("Content-Type","application/json")
	 * .body(payload.DeleteBook(id))
	 * 
	 * .when() .post("Library/DeleteBook.php")
	 * 
	 * .then().log().all() .assertThat().statusCode(200) .extract().asString();
	 * 
	 * }
	 */
  
  
  @DataProvider
  public Object[][] getData() {
	  
	 return new Object[][] {
		  {"VB","125"},
		  {"java","459"},
		  {"c#","781"},
		  {"python","724"}
	  };
  }
}
