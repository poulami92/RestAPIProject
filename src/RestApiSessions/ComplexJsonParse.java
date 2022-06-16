package RestApiSessions;
import org.testng.Assert;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js = new JsonPath(payload.CoursePrice());
		int count = js.getInt("courses.size()");                                 //get no of courses
		System.out.println(count);
		
		int totalAmt=js.getInt("dashboard.purchaseAmount");                      //get purchase amount
		System.out.println(totalAmt);
		
		String title1stCourse = js.getString("courses[0].title");                //get title of 1st course 
		System.out.println(title1stCourse);
		
		for(int i=0;i<count;i++) {                                              //get all the courses title and their prices
			
			System.out.println(js.getString("courses["+i+"].title"));
			System.out.println(js.getInt("courses["+i+"].price"));
			
		}
		
		for(int i=0;i<count;i++) {                                              //print no of RPA copies
			
			if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA"))
			{
				System.out.println("No of RPA copies :");
				System.out.println(js.getInt("courses["+i+"].copies"));
				break;
			}
		
		}
		
		 //Verify total purchase amount with sum of all the courses price 
		
		int sum =0;                                                            
		
		for(int i=0;i<count;i++) {   
			
			int copies = js.getInt("courses["+i+"].copies");
			int price = js.getInt("courses["+i+"].price");
			
			sum = sum+(copies*price);
				
		}
		
		Assert.assertEquals(sum, totalAmt);

	}

}
