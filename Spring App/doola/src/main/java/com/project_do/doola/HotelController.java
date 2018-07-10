package com.project_do.doola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController

public class HotelController {
	
	@Autowired
	
	@RequestMapping("/")
	public String healthcheck() {
		return "Ola Amigos!!";
	}
	
	@RequestMapping("/get")
	public String getPerson(@RequestParam(name="latlong", required=false, defaultValue="Unknown") String latlong) throws JSONException{
		
		String [] aStr = latlong.split(" ", 5);
		System.out.println("hi");
		final String uri = "https://www.oyorooms.com/api/search/hotels?additional_fields=category_info%2Ccancellation_policies%2Cbest_image%2Croom_pricing%2Cavailability%2Camenities%2Crestrictions%2Ccategory%2Ccaptains_info%2Cnew_applicable_filters%2Cadditional_charge_info%2Cimages%2Chotel_images%2Cguest_ratings&available_room_count%5Bcheckin%5D={checkin}&available_room_count%5Bcheckout%5D={checkout}&available_room_count%5Bmin_count%5D=1&fields=id%2Cname%2Ccity%2Cstreet%2Ccategory%2Cgeo_location%2Ccategory%2Chotel_type%2Calternate_name%2Cshort_address&filters%5Bcoordinates%5D%5Blatitude%5D={lat}&filters%5Bcoordinates%5D%5Blongitude%5D={lon}&filters%5Ball_room_categories%5D=true&format_response%5Bbatch%5D%5Bcount%5D=20&format_response%5Bbatch%5D%5Boffset%5D=0&format_response%5Bsort_params%5D%5Bsort_on%5D=&format_response%5Bsort_params%5D%5Bascending%5D=true&pre_apply_coupon_switch=true&rooms_config=1%2C0%2C0&source=Web+Booking";
		Map<String,Object> urv = new HashMap<>();
		Variables.inDate = aStr[2];
		System.out.println(uri);
	
		Variables.outDate = aStr[3];
		String lat = aStr[0];
		String lon = aStr[1];
		String phno = aStr[4];
		urv.put("checkin", Variables.inDate);
		urv.put("checkout", Variables.outDate);
		urv.put("lat", lat);
		urv.put("lon", lon);
		String gh="";
		
		Variables.date.put(phno,new String[] {Variables.inDate,Variables.outDate});
	    RestTemplate restTemplate = new RestTemplate();
	   
	    
	    String result =  restTemplate.getForObject(uri, String.class, urv);
	    JSONObject jsonObj = new JSONObject(result);
	    System.out.println("helo");
	    System.out.println(result);
	    JSONArray hotel_array = jsonObj.getJSONArray("hotels");
	    if(hotel_array.length()==0)
	    	return "No Hotels Around you!"; 
	    for(int i=0;i<1;i++){
	    	ArrayList<String> arr = new ArrayList<String>();
            JSONObject obj=hotel_array.getJSONObject(i);
            Variables.addr.put(obj.getString("id"),new String[] {obj.getString("street"),obj.getString("alternate_name")});
            JSONArray arr1 = obj.getJSONArray("reduced_room_pricing");
            JSONObject obj1=obj.getJSONObject("ratings");
            arr.add(obj1.getString("value"));
            arr.add(obj.getString("id"));
            arr.add(obj.getString("alternate_name"));
            arr.add(arr1.getString(0));
            String hj = obj.getString("distance");
            Float f = Float.valueOf(hj);
            String dis ="";
            int j=0;
            if(f<1) {
            	 j = (int) (f*1000);
            	 dis = Integer.toString(j)+" m";
            }
            
            else {
            Float g = (float) (Math.round((f*100))/100.0);
            dis = Float.toString(g)+" km";
            System.out.println(g);
            }
            arr.add(dis);
            
            gh = gh+arr.get(2)+"\n"+"OYO_ID: "+arr.get(1)+"#"+arr.get(4)+"#"+arr.get(0)+"#"+"₹ "+arr.get(3)+"#"+"!";   
        }
	    
	    System.out.println(gh);
	    
	    return gh;
	}

	
	@RequestMapping("*")
	@ResponseBody
	public String fallbackMethod(){
		return "Item send a valid request";
	}

	

}
