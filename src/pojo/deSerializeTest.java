 package pojo;
 
 import io.restassured.RestAssured;
 import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

 import static io.restassured.RestAssured.*;
 import static org.hamcrest.Matchers.*;

 import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
 import org.testng.annotations.DataProvider;
 import org.testng.annotations.Test;

public class deSerializeTest {

	public static void main(String[] args) throws InterruptedException   {
		
		String[] expectedCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
		
		/*
		 * System.setProperty("webdriver.chrome.driver",
		 * "E://Downloads//Selenium//chromedriver_win32//chromedriver.exe"); WebDriver
		 * driver = new ChromeDriver(); driver.get(
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=fdgshhh"
		 * ); driver.findElement(By.xpath("//input[@type='email']")).sendKeys(
		 * "poulami92datta@gmail.com");
		 * driver.findElement(By.xpath("//span[text()='Next']")).click();
		 * 
		 * Thread.sleep(3000);
		 * driver.findElement(By.xpath("//input[@type='password']")).sendKeys(
		 * "gti8262core");
		 * driver.findElement(By.xpath("//span[text()='Next']")).click();
		 * 
		 * String authResponseUrl = driver.getCurrentUrl();
		 */
		
		
	//--------------------- Generate Authorization Code---------------------------------------
		
		
		String authResponseUrl= "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2F0AX4XfWj5r8CN_hiGm8dCI2KLqpIaUM-oZ6zDnSv3hvKBQt_TwfwCxMBjzF64INPkgyYrnA&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		
		//Response url after hitting google authorization server
		
		String partialCode=authResponseUrl.split("code=")[1];
		String authCode=partialCode.split("&scope")[0];
		System.out.println(authCode);
		
		
	//----------------------------- Generate Access Token---------------------------------------
		
		
		String tokenResponse= given().log().all().urlEncodingEnabled(false)            //Prevent automatic encoding of special chars in url 
	    .queryParam("code",authCode)
	    .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
	    .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
	    .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
	    .queryParam("grant_type", "authorization_code")
	    
	    .when()
	    .post("https://www.googleapis.com/oauth2/v4/token")
	    
	    .then().log().all()
	    .extract().asString();
		
		JsonPath js =new JsonPath(tokenResponse);
		String accessToken = js.getString("access_token");
		
		
	//----------------------------- Get Course Details---------------------------------------
		
		
		GetCourse gc =given().log().all()
		.queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)          //specify response type is in JSON
		
		.when()
		.get("https://rahulshettyacademy.com/getCourse.php")
		
		.then().log().all()
		.extract().as(GetCourse.class);                                      //store response in POJO class
		
		
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		
		String courseTitle =gc.getCourses().getApi().get(1).getCourseTitle();
		System.out.println(courseTitle);
		
		int size=gc.getCourses().getApi().size();

		for(int i=0;i<size;i++)
		{
			if(gc.getCourses().getApi().get(i).getCourseTitle().equals("SoapUI Webservices testing"))
			{
				System.out.println(gc.getCourses().getApi().get(i).getPrice());
				break;
			}
			
		}
		
     //Get course titles of webAutomation
		
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> webAutomation = gc.getCourses().getWebAutomation();
		
		for(WebAutomation wa :webAutomation )
		{
			a.add(wa.getCourseTitle());
		}
		
		List<String> expecledList=Arrays.asList(expectedCourseTitles);
		Assert.assertTrue(a.equals(expecledList));
	}

}
