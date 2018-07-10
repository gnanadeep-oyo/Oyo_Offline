package com.project_do.doola;

import org.springframework.stereotype.Component;

@Component
public class Hotel {
	
	private String coordinates;
	
	public String setString(String c,String d,String e) {
		 String x = "{\n" + 
				"\"source\":\"website\",\n" + 
				"\"user[name]\":\"Gnan\",\n" + 
				"\"user[country_code]\":\"+91\",\n" + 
				"\"user[phone]\":\"8686765400\",\n" + 
				"\"user[email]\":\"pikachugd@gmail.com\",\n" + 
				"\"booking\":{\n" + 
				"\"single\":\"1\",\n" + 
				"\"double\":\"0\",\n" + 
				"\"triple\":\"0\",\n" + 
				"\"extra\":\"0\",\n" + 
				"\"auto_upgrade\":\"2\",\n" + 
				"\"[guests_config][0][adults]\":\"1\",\n" + 
				"\"[guests_config][0][children]\":\"0\",\n" + 
				"\"[guests_config][0][guests]\":\"1\",\n" + 
				"\"hotel_id\":\""+c+"\",\n" + 
				"\"checkin\":\""+d+"\",\n" + 
				"\"checkout\":\""+e+"\",\n" + 
				"\"noofdays\":\"1\",\n" + 
				"\"tripleoccupancy\":\"false\",\n" + 
				"\"source\":\"Web Booking\",\n" + 
				"\"utm_source\":\"\",\n" + 
				"\"room_category_id\":\"1\"\n" + 
				"},\n" + 
				"\"ta_notes\":\"1,1,0\"\n" + 
				"}";
		return x;
	}
	public String getCoordinates() {
		return coordinates;
	}

}
