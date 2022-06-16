package RestApiSessions;



import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		
		//Login To JIRA
		
		//store session id against API login request whenever new session is created and we can use session object for all subsequent request
		
		SessionFilter session = new SessionFilter();                     
		
		 given().log().all()
		.header("Content-Type","application/json")
		.body("{ \"username\": \"poulami92\", \"password\": \"gti8262core\" }")
		.filter(session)
		
		.when()
		.post("rest/auth/1/session")
		
		.then().log().all()
		.assertThat().statusCode(200);
		
		
		//Add Comment to existing issue
		
		 String message = "Adding comment from automation";
		 String issueid="RES-1";
		 
		  String commentDetails =given().log().all() 
		  .pathParam("issueId",issueid)
		  .header("Content-Type","application/json") .body("{\r\n" +
		  "    \"body\": \""+message+"\",\r\n" +
		  "    \"visibility\": {\r\n" + "        \"type\": \"role\",\r\n" +
		  "        \"value\": \"Administrators\"\r\n" + "    }\r\n" + "}")
		  .filter(session)
		  
		  .when() 
		  .post("rest/api/2/issue/{issueId}/comment")                                         //issueId=same variable used as path parameter
		  
		  .then().log().all() 
		  .assertThat().statusCode(201)
		  .extract().asString();
		  
		  JsonPath js = new JsonPath(commentDetails);
		  String commentId= js.getString("id");
		  
		  
		  	  
		  
		  //Add Attachement
		  
		  given().log().all() .pathParam("issueId","10203")
		  .header("X-Atlassian-Token","no-check")
		  .header("Content-Type","multipart/form-data")                                       //pass this header when attaching file 
		  .multiPart("file", new
		  File("C:\\Users\\Pd\\eclipse-workspace\\DemoProject\\src\\files\\jira.txt"))       //pass jira.txt file as input with request to attach .filter(session)
		  .filter(session)
		  
		  .when()
		  .post("rest/api/2/issue/{issueId}/attachments")
		  
		  .then().log().all() 
		  .assertThat().statusCode(200);
		 
		
		
		//Get issue Details 
		
		String issuedetails =given().log().all()
		.queryParam("fields", "comment")              //return comments field in response
		.pathParam("issueId","10203")
		.filter(session)
		
		.when()
		.get("rest/api/2/issue/{issueId}")
		
		.then().log().all()
		.assertThat().statusCode(200)
		.extract().asString();
		
		js = new JsonPath(issuedetails);
		
		// Get no of comments added to this issue
		
		int commentCount = js.getInt("fields.comment.comments.size()");
		
		for(int i=0;i<commentCount;i++) {
			
			String Id= js.getString("fields.comment.comments["+i+"].id");
			if(Id.equals(commentId)) {
				
				String commentBody=js.getString("fields.comment.comments["+i+"].body");
				Assert.assertEquals(message, commentBody);
				break;
			}
					
		}
	}

}
