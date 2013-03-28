package strava.shared;

import java.io.*;
import java.net.*;
import java.util.*;


public class Club {

	private int id;
	private String name;
	private ArrayList<Member> members;

	public Club(int i){
		id = i;
		name = createName();
		members = createMembers(); 
	}
	
	public ArrayList<Member> getMembers(){
		return this.members;
	}

	private ArrayList<Member> createMembers() {

		//create arraylist to store members
		ArrayList<Member> members = new ArrayList<Member>();

		//use ID to find club using searchMembers(), return String
		String memb = "";

		try {
			memb = searchMembers();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// parse the query result to get desired information
		return parseMembers(memb);
	}

	private String createName(){

		//use ID to find club, return String
		String name = "";


		try {
			name = searchName();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//parse query result to find club name
		return parseName(name);
	}

	private String searchMembers() throws MalformedURLException, IOException {

		//Create URL
		URL query = new URL("http://www.strava.com/api/v1/clubs/" + id + "/members");

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

	private String searchName() throws MalformedURLException, IOException {

		//Create URL
		URL query = new URL("http://www.strava.com/api/v1/clubs/" + this.id);

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

	private ArrayList<Member> parseMembers(String s){

		//check for empty string
		if (s.equals("") || s == null) {
			return new ArrayList<Member>();
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid clubs/"+this.id+"\"}")){
			return new ArrayList<Member>();
		}

		//initiate aray
		ArrayList<Member> members = new ArrayList<Member>();

		int startID;
		int endID;
		
		int startName;
		int endName;

		//trim string to be only members
		s = s.substring(s.indexOf("["), s.lastIndexOf("]")+1);

		while (s.indexOf("\"id\":") >= 0) {
			
			//find substring of id
			startID = s.indexOf("\"id\":") + 5;
			endID = s.indexOf(",", startID);
			
			//find substring of name
			startName = s.indexOf("\"name\":") + 8;
			endName = s.indexOf("}", startName) - 1;
			
			//parse id to number, create new member
			String num = s.substring(startID,endID);
			String name = s.substring(startName, endName);
			members.add(new Member(Integer.parseInt(num), name));
			System.out.println("Added new member: " + name + ", " + num);
			
			//Used for Testing Purposes
			//System.out.println("Added new Member" + name + ": " + num);
			
			//trim substring to find next member
			s = s.substring(endName + 2, s.length());
		}

		return members;
	}

	private String parseName(String s) {
		
		//check for empty string
		if (s.equals("") || s == null) {
			return "";
		}

		//check for error
		if (s.equals("{\"error\":\"Invalid clubs/"+this.id+"\"}")){
			return "";
		}

		//find name
		int start = s.indexOf("\"name\":") + 8;
		int end = s.indexOf("\",");


		return s.substring(start,end);
	}

	public String toString(){
		return "{\"club\":{\"id\":"+this.id+",\"name\":\""+ this.name+"\",\"members\":{" + this.members + "}}";
	}

}
