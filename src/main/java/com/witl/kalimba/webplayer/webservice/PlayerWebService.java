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
		int res=userDAO.validateTransaction(PnrID, tnsId);
		
		String result="";
		if(res>0){
			result="success";
		}
		else
		{
			result="failure";
		}
	 
	    return result;
		
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
