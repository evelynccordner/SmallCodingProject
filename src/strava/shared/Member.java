package strava.shared;

import java.io.*;
import java.net.*;
import java.util.*;


public class Member {

	public static String date = "2013-03-01";

	private int id;
	private String name;
	private ArrayList<Ride> rides;
	private double elevationGain;
	private int numRides;

	//"query to get rides for competition: http://www.strava.com/api/v1/rides?athleteId="+"startDate=2013-03-01"

	public Member(int i, String s) {
		id = i;
		name = s;
		rides = createRides();
		elevationGain = findElevation(); 
		numRides = rides.size();
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getElevation(){
		return this.elevationGain;
	}
	
	public int getNumRides(){
		return this.numRides;
	}
	
	private double findElevation() {
		
		//loop through rides, sum elevation gain
		double elevation = 0.0;
		
		for (Ride r : this.rides) {
			elevation = elevation + r.getElevation();
		}
		
		return elevation;
	}

	private ArrayList<Ride> createRides(){
		
		//query rides, return results as string
		String ridesString = "";

		
		try {
			ridesString = searchRides();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 

		//parse query result to create an array of Rides
		return parseRides(ridesString);
	}

	private String searchRides() throws MalformedURLException, IOException {

		String fullquery = "";
		String URLread = "";
		
		//use offset to ensure that all rides are obtained 
		int offset = 0;
		
		//build query, continue looping through offsets until empty set is returned.
		while (! URLread.equals("{\"rides\":[]}")){
			
			if (URLread.equals("{\"error\":\"Invalid athleteId\"}")) {
				break;
			}
			
			//add new query results to existing results
			fullquery = fullquery + URLread;
			URLread = "";

			//Create URL
			URL query = new URL("http://www.strava.com/api/v1/rides?athleteId="+this.id+"&startDate=2013-03-01&offset=" + offset);

			//Create Reader
			BufferedReader in = new BufferedReader(
					new InputStreamReader(query.openStream()));

			//Capture Query Results
			String inputLine;

			while((inputLine = in.readLine()) != null){
				URLread = URLread + inputLine;
			}
			
			offset += 50;
		}

		return fullquery;
	}
	
	private ArrayList<Ride> parseRides(String s){

		//check for empty string
		if (s.equals("") || s == null) {
			return new ArrayList<Ride>();
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid clubs/"+this.id+"\"}")){
			return new ArrayList<Ride>();
		}

		//initiate aray
		ArrayList<Ride> members = new ArrayList<Ride>();

		int startID;
		int endID;

		while (s.indexOf("\"id\":") >= 0) {
			
			//find substring of id
			startID = s.indexOf("\"id\":") + 5;
			endID = s.indexOf(",", startID);
			
			//parse id to number, create new member
			String num = s.substring(startID,endID);
			members.add(new Ride(Integer.parseInt(num)));
			System.out.println("Added new Ride: " + num);
			
			//trim substring to find next member
			s = s.substring(endID, s.length());
		}

		return members;
	}

	public String toString(){
		return "{\"id\":"+ this.id + ",\"name\":\"" + this.name + "\"}";
	}
	
	public void printRides(){
		System.out.println(this.rides);
	}
	

}
