package tic_tac_toe_Game;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

public class TestBase {
	
	
	
	@BeforeClass
	public void setup() {
		
		RestAssured.baseURI = "https://piskvorky.jobs.cz";
		RestAssured.basePath = "/api/v1";
	}

}
