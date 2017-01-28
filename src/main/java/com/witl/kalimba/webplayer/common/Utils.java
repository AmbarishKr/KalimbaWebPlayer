package com.witl.kalimba.webplayer.common;
import java.security.SecureRandom;
import java.math.BigInteger;


public class Utils {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SessionIdentifierGenerator sessionIdentifier= new SessionIdentifierGenerator();
		String key= sessionIdentifier.nextSessionId();
		System.out.println("Key --- "+key);

	}
	
}


	 final class SessionIdentifierGenerator {
		  private SecureRandom random = new SecureRandom();

		  public String nextSessionId() {
		   // return new BigInteger(130, random).toString(32);
		    return org.apache.commons.lang.RandomStringUtils.random(6); 
		  }
	}


