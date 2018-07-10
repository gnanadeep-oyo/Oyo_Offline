package com.project_do.doola;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class BookingController {
	
	@Autowired
	private Hotel hotel;

	@RequestMapping("/booking")
	public  String doBooking(@RequestParam(name="code", required=false, defaultValue="Unknown") String code) throws JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("access_token", "dUxaRnA5NWJyWFlQYkpQNnEtemo6bzdvX01KLUNFbnRyS3hfdEgyLUE6SGNCQU14cVJvNUQ1eVY5eks1Nm06NzcyOTAxNzI5OTpDb25zdW1lcl9HdWVzdA==");
		headers.add("content-type", "application/json");
		RestTemplate restTemplate = new RestTemplate();
		String [] aCode = code.split(" ", 2);
		System.out.println(Variables.date.get(aCode[1])[0]+"Hello");
		String js =hotel.setString(aCode[0],Variables.date.get(aCode[1])[0],Variables.date.get(aCode[1])[1]);
		//Variables.date.remove(aCode[1]);
	
		String url = "http://api.oyorooms.com//api/v2/bookings";
		HttpEntity<?> request = new HttpEntity<Object>(js, headers);
		System.out.println("hi happy2");
		String jx = restTemplate.postForObject(url, request, String.class);
		System.out.println("aover");
		JSONObject ob = new JSONObject(jx);   
		String amt = ob.getString("invoice_no");

		String payable_amount = ob.getString("payable_amount");

	    
	    for (Map.Entry<String,String[]> entry : Variables.addr.entrySet()) 
        System.out.println("Key = " + entry.getKey() +
                         ", Value = " + entry.getValue());
	    
		String lt = amt+" at "+Variables.addr.get(aCode[0])[1]+".  \n" + 
				"Payable Amount = "+payable_amount+"\n"+ 
				"Address:"+Variables.addr.get(aCode[0])[0]+"";
		System.out.println(lt);
		
		return lt;
		
	}

}
