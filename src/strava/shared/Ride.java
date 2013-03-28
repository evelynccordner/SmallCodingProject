package strava.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class Ride {

	private int id;
	private String name;
	private double elevationGain;

	public Ride(int i) {
		id = i;
		name = createName();
		elevationGain = createElevationGain();
	}

	public double getElevation(){
		return this.elevationGain;
	}

	private String createName(){
		String query = "";


		try {
			query = searchRide();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return parseName(query);
	}

	private double createElevationGain(){
		String query = "";


		try {
			query = searchRide();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return parseElevationGain(query);
	}

	/**
	private Date createDate(){
		String query = "";


		try {
			query = searchRide();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return parseDate(query);
	}
	**/
	private String searchRide() throws MalformedURLException, IOException {

		//Create URL
		URL query = new URL("http://www.strava.com/api/v1/rides/" + this.id);

		//Create Reader
		BufferedReader in = new BufferedReader(
				new InputStreamReader(query.openStream()));

		//Capture Query Results
		String inputLine;
		String URLread = "";

		while((inputLine = in.readLine()) != null){
			URLread = URLread + inputLine;
		}

		return URLread;
	}

	private String parseName(String s) {

		//check for empty string
		if (s.equals("") || s == null) {
			return "";
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid rides/"+this.id+"\"}")){
			return "";
		}

		//find name
		int start = s.indexOf("\"name\":") + 8;
		int end = s.indexOf("\",",start);


		return s.substring(start,end);
	}

	private double parseElevationGain(String s) {

		//check for empty string
		if (s.equals("") || s == null) {
			return 0;
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid rides/"+this.id+"\"}")){
			return 0;
		}

		//find name
		int start = s.indexOf("\"elevationGain\":") + 16;
		int end = s.indexOf(",",start);


		if (start < 0 || end < 0){
			return 0;
		}

		return Double.parseDouble(s.substring(start,end));
	}
	/**
	private Date parseDate(String s) {

		//check for empty string
		if (s.equals("") || s == null) {
			return new Date(0,0,0);
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid rides/"+this.id+"\"}")){
			return new Date(0,0,0);
		}

		//find name
		int start = s.indexOf("\"startDate\":") + 13;
		int end = s.indexOf("T",start);

		if (start < 0){
			return new Date(0,0,0);
		}

		String dt = s.substring(start,end);		

		if (dt.length() != 10) {
			return new Date(0,0,0);
		}

		int year = Integer.parseInt(dt.substring(0,4));
		int month = Integer.parseInt(dt.substring(5,7));
		int day = Integer.parseInt(dt.substring(8,10));

		if (start < 0){
			return new Date(0,0,0);
		}

		return new Date(year,month,day);


	}
	**/

	public String toString(){
		return "{\"ride\":{\"id\":" + this.id + ",\"elevationGain\":"+ this.elevationGain + ",\"name\":\"" + this.name + "\"}}";
	}


}
