package tic_tac_toe_Game;



//import com.fasterxml.jackson.databind.util.JSONPObject;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.util.IOUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.apache.commons.io.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




public class GamePlayingE2EScenario extends TestBase {
	
	
	
	Faker fake = new Faker();
	
	public static String  userid=null;
	public static String usertoken=null;
	public static String gameid=null;
	public static String gametoken=null;
	public static int x;
	public static int y;
	
	@Test(priority=1)
    public void checkCreatingUser() throws IOException{

		FileInputStream fileinputstream=new FileInputStream(new File(".\\jsonfiles\\user.json"));
		String payload=org.apache.commons.io.IOUtils.toString(fileinputstream,"utf-8")	
		
		.replace("Nickname",fake.name().username())
		.replace("Email",fake.internet().emailAddress());
		
	
		System.out.println(payload);
		
	    String response=given()
		.when()
		.contentType(ContentType.JSON)
		.when()
		.body(payload)
		.post("/user")
		.then()
		.statusCode(201).log().body().extract().asString();
	
	
	
	JsonPath jsonPath = JsonPath.from(response);
	
	userid = jsonPath.getString("userId");
	System.out.println(userid);
	usertoken=jsonPath.getString("userToken");
	System.out.println(usertoken);
			
			

    }
	//How to call Post Method using Json Object in RestAssured API
	
	@Test(dependsOnMethods = {"checkCreatingUser"})
	    public void checkCreatingGame() throws IOException{

		FileInputStream fileinputstream=new FileInputStream(new File(".\\jsonfiles\\game.json"));
		String payload=org.apache.commons.io.IOUtils.toString(fileinputstream,"utf-8")	
		.replace("UserToken",usertoken);
		System.out.println(payload);
		
	    String response=given()
		.when()
		.contentType(ContentType.JSON)
		.when()
		.body(payload)
		.post("/connect")
		.then()
		.statusCode(201).log().body().extract().asString();
	
// get the response
	JsonPath jsonPath = JsonPath.from(response);
	
	gametoken = jsonPath.getString("gameToken");
	System.out.println(gametoken);
	gameid=jsonPath.getString("gameId");
	System.out.println(gameid);
			
      
	    }
	
	@Test(dependsOnMethods = {"checkCreatingGame"})
    public void checkMakingMove() throws IOException, InterruptedException{

		
		//Thread.sleep(30000);
	FileInputStream fileinputstream=new FileInputStream(new File(".\\jsonfiles\\play.json"));
	String payload=org.apache.commons.io.IOUtils.toString(fileinputstream,"utf-8")	
	
	.replace("Usertoken",usertoken)
	.replace("Gametoken",gametoken);
	
	
	System.out.println(payload);
    boolean retryflag=true;
  
  int retrycount=0;
  
  while(retryflag) {
	try {
    String response=given()
	.when()
	.contentType(ContentType.JSON)
	.when()
	.body(payload)
	.post("/play")
	.then()
	.log().body().extract().asString();
//print the response
    System.out.println(response);
// Expected Result
    JsonPath  ReqjsonPath = JsonPath.from(payload);
  int postionX=ReqjsonPath.getInt("positionX");
	System.out.println(postionX);
  int postionY=ReqjsonPath.getInt("positionY");
  System.out.println(postionY);
  int [] pointarr1= {postionX,postionY} ; 
// Actual
     JsonPath ResjsonPath = JsonPath.from(response);

     x=ResjsonPath.getJsonObject("coordinates[0].x");
     System.out.println(x);
     y=ResjsonPath.getJsonObject("coordinates[0].y");
     System.out.println(y);
     int [] pointarr2= {x,y} ; 
		
  // Assertion
     
    assertEquals(pointarr2, pointarr1);
    retryflag=false;
    break;
    
	}catch(Exception ex) {
		retryflag=true;
		retrycount++;
	}
    }
  
	System.out.print(retrycount);
	}
	
}


