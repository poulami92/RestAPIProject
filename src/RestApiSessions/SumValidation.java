package RestApiSessions;
import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	
	@Test
	public void sumOfCourse() {
		
		JsonPath js = new JsonPath(payload.CoursePrice());
		int totalAmt=js.getInt("dashboard.purchaseAmount"); 
		int courseCount = js.getInt("courses.size()");    
		
        int sum =0;
		
		for(int i=0;i<courseCount;i++) {   
			
			int copies = js.getInt("courses["+i+"].copies");
			int price = js.getInt("courses["+i+"].price");
			
			sum = sum+(copies*price);
				
		}
		System.out.println(sum);
		Assert.assertEquals(sum, totalAmt);
		
	}

}
