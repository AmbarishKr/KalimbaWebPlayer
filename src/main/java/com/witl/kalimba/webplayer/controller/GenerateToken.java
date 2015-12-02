package com.witl.kalimba.webplayer.controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.witl.kalimba.webplayer.common.Payment;
import com.witl.kalimba.webplayer.common.Transaction;
import com.witl.kalimba.webplayer.dao.PaymentDao;
import com.witl.kalimba.webplayer.dao.TransactionDao;

@Controller
@ComponentScan({ "com.witl.kalimba.webplayer" })
public class GenerateToken {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private TransactionDao transactionDao;

	private String tokenId;
	private String listOfSongs;
	private Payment payment;
	private String PnrID;

	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	@ResponseBody
	public String getToken(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws SAXException, IOException, ParserConfigurationException,
			URISyntaxException {
		String items="",email="", price="";
		
		if(request.getParameter("itemsdetails")!=null){
		items = (String) request.getParameter("itemsdetails");
		}
		
		if(request.getParameter("email")!=null){
		email=request.getParameter("email");
		}
		
		listOfSongs = items;

		//System.out.println(request.getParameter("totalPrice"));
		if(request.getParameter("totalPrice")!=null){
		 price = request.getParameter("totalPrice");
		}
		float pricefloat = Float.parseFloat(price);
		pricefloat = (float) Math.floor(pricefloat * 100) / 100;
		String newprice = Float.toString(pricefloat);
		System.out.println(pricefloat);
	   
		/*request.getParameter("");
		request.getParameter("");*/

		// http cleient call
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<API3G>"
				+ "<CompanyToken>68B90B5E-25F6-4146-8AB1-C7A3A0C41A7F</CompanyToken>"
				+ "<Request>createToken</Request>"
				+ "<Transaction>"
				+ "<PaymentAmount>"
				+ newprice
				+ "</PaymentAmount>"
				+ "<PaymentCurrency>USD</PaymentCurrency>"
				+ "<CompanyRef>49FKEOA</CompanyRef>"
				+ "<RedirectURL>http://130.211.128.142/KalimbaWebPlayer/getTransaction</RedirectURL>"
				+ "<BackURL>http://130.211.128.142/KalimbaWebPlayer</BackURL>"
				+ "<CompanyRefUnique>0</CompanyRefUnique>"
				+ "<PTL>5</PTL>"
				+ "</Transaction>"
				+ "<Services>"
				+ "<Service>"
				+ "<ServiceType>949</ServiceType>"
				+ "<ServiceDescription>Kalimba Radio music purchase</ServiceDescription>"
				+ "<ServiceDate>2013/12/20 19:00</ServiceDate>" + "</Service>"
				+ "</Services>" + "</API3G>";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost httpRequest = new HttpPost("https://secure.3gdirectpay.com/API/v5/");
		httpRequest.setHeader("Content-Type", "application/xml");
		StringEntity xmlEntity;
		xmlEntity = new StringEntity(xmlString);
		httpRequest.setEntity(xmlEntity);
		HttpResponse httpresponse = httpClient.execute(httpRequest);
		String result = EntityUtils.toString(httpresponse.getEntity());
		System.out.println(result);

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(result));

		Document doc = db.parse(is);

		Node node = doc.getElementsByTagName("TransToken").item(0).getFirstChild();
		System.out.println("Value of token=" + node.getNodeValue());

		Node check = doc.getElementsByTagName("ResultExplanation").item(0).getFirstChild();
		System.out.println("check success or failure =" + check.getNodeValue());
		tokenId = node.getNodeValue();

		payment = new Payment();
		payment.setAmonut(pricefloat);
		payment.setCreatetokenstatus(check.getNodeValue());
		payment.setEmailid(email);
		payment.setSongidstring(items);
		payment.setTokenid(tokenId);

		paymentDao.save(payment);
		List songIdList=getSongList(listOfSongs);
		System.out.println(songIdList);
		//getEmployees();

		return tokenId;

	}

	@RequestMapping(value = "/getTransaction", method = RequestMethod.GET)
//	@ResponseBody
	public ModelAndView verifyTransaction(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		
		System.out.println("Iinside payment returns");
		
		
		String tnsId = request.getParameter("TransID");
		
		 PnrID=request.getParameter("PnrID");
		String CCDapproval=request.getParameter("CCDapproval");
		String companyRef=request.getParameter("CompanyRef");
		
		
		System.out.println(tnsId+"=="+PnrID+"==="+CCDapproval);
		
		
		Transaction transaction=new Transaction();
		transaction.setTnsId(tnsId);
		transaction.setPnrId(PnrID);
        transaction.setCompanyRef(companyRef);	
        transaction.setCcDapproval(CCDapproval);
        transaction.setDate(new Date());
        transaction.setPayment(payment);
        transactionDao.save(transaction);		

		return new ModelAndView("redirect:/?PnrID="+PnrID+"&tnsId="+tnsId);

	}
	
	
	private List<Integer> getSongList(String listOfSong){
		StringTokenizer st = new StringTokenizer(listOfSong,",");
		List<Integer> songIdList=new ArrayList<Integer>();
        while(st.hasMoreTokens()){
          songIdList.add(Integer.parseInt(st.nextToken()));
        }		
		return songIdList;
	}
	

	@GET
	@Path("/getDownloadValidation")
	@Produces("application/json")
	public String getDownloadValidation(@QueryParam("PnrID") String PnrID,@QueryParam("tnsId") String tnsId)
	{
	Transaction downloadDao=  transactionDao.getById(PnrID);
	  tnsId=downloadDao.getTnsId();
	 String approval=downloadDao.getCcDapproval();
	    return approval;
	}
	
	/*private void getEmployees()
	{
	    final String uri = "http://localhost:8080/KalimbaWebPlayer/download";
	     
	    RestTemplate restTemplate = new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	     
	    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
	     
	   
	}*/
	
	
	
	}
