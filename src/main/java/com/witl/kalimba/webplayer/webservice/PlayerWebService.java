package com.witl.kalimba.webplayer.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.witl.kalimba.webplayer.common.Payment;
import com.witl.kalimba.webplayer.common.Transaction;
import com.witl.kalimba.webplayer.common.User;
import com.witl.kalimba.webplayer.common.UserJDBCTemplate;
import com.witl.kalimba.webplayer.dao.PaymentDao;
import com.witl.kalimba.webplayer.dao.TransactionDao;

@RestController
@ComponentScan({ "com.witl.kalimba.webplayer" })
public class PlayerWebService {
	
	@Autowired
	private UserJDBCTemplate userDAO;

	private String tokenId;
	private String listOfSongs;
	private Payment payment;
	private String PnrID;

	@GET
	//@Path("/getDownloadValidation")
	//@Produces("application/json")
	@RequestMapping("/getDownloadValidation")
	
	public @ResponseBody String getDownloadValidation(@QueryParam("PnrID") String PnrID,@QueryParam("tnsId") String tnsId){
		System.out.println("INside rest controller");
		//transactionDao = new TransactionDao();
		String res=userDAO.validateTransaction(PnrID, tnsId);
		
		String result="";
		if(res==""){
			result="failure";
		}
		else
		{
			result=res;
		}
	 
	    return result;
		
	}
	
	@GET
	//@Path("/getDownloadValidation")
	//@Produces("application/json")
	@RequestMapping("/setUserDb")
	
	public @ResponseBody String setUserDb(@QueryParam("email_id") String email_id,@QueryParam("name") String name,@QueryParam("first_name")String first_name,@QueryParam("last_name")String last_name,@QueryParam("gender")String gender,@QueryParam("birthday")String birthday,@QueryParam("location")String location,@QueryParam("hometown")String hometown,@QueryParam("relationship")String relationship,@QueryParam("timezone")String timezone,@QueryParam("provider")String provider,@QueryParam("provider_id") String provider_id,@QueryParam("user_type")String user_type){
		System.out.println("INside rest controller");
		//transactionDao = new TransactionDao();
		
		
			User user = new User();
			user.setEmail(email_id);
			user.setName(name);
			user.setFirst_Name(first_name);
			user.setLast_Name(last_name);
			user.setGender(gender);
			user.setBirthday(birthday);
			user.setLocation(location);
			user.setHometown(hometown);
			user.setRelationship(relationship);
			user.setTimezone(timezone);
			user.setProvider(provider_id);
			user.setProvider_id(Integer.parseInt(provider_id));
			user.setUserType(user_type);
			userDAO.create(user);
		    return "updated";
		
	}

	/*
	 * private void getEmployees() { final String uri =
	 * "http://localhost:8080/KalimbaWebPlayer/download";
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	 * HttpEntity<String> entity = new HttpEntity<String>("parameters",
	 * headers);
	 * 
	 * ResponseEntity<String> result = restTemplate.exchange(uri,
	 * HttpMethod.POST, entity, String.class);
	 * 
	 * 
	 * }
	 */

}
