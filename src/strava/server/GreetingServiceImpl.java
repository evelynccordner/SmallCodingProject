package strava.server;


import java.text.DecimalFormat;
import java.util.ArrayList;

import strava.client.GreetingService;
import strava.shared.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
		//Create club, using input
		int id = Integer.parseInt(input);
		Club club = new Club(id);

		//Get list of all members from the club, and sort list by elevation
		ArrayList<Member> members = club.getMembers();
		ArrayList<Member> sortedMembers = quickSort(members);


		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);

		//Print results
		
		String results = "";
		
		int i = 1;
		for (Member m : sortedMembers) {
			double elevation = m.getElevation();
			int rides = m.getNumRides();
			double avg;
			if (rides == 0) {
				avg = 0.0;
			}
			else{
				avg = elevation / rides;
			}

			results = results + "<br><br> " + i + ": " + m.getName() + "     <br>Elevation Gained: " + df.format(elevation) + "     <br>Rides: " + rides + "     <br>Average Elevation Gain per Ride: " + df.format(avg);
			i++;
		}
		System.out.println(results);
		
		return results;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	//Method to sort club members by elevation gained this months
	public static ArrayList<Member> quickSort(ArrayList<Member> members) {

		//if list size <= 1, list is already sorted
		if (members.size() <= 1 ) {
			return members;
		}

		//chose and remove pivot
		int pivot = members.size() / 2;
		Member pivM = members.get(pivot);
		members.remove(pivM);

		double pivValue = pivM.getElevation();

		//every value smaller than the pivot should be placed in "less",
		//every value larger than pivot should be placed in "greater"
		ArrayList<Member> less = new ArrayList<Member>();
		ArrayList<Member> greater = new ArrayList<Member>();

		for (Member m : members) {
			if (m.getElevation() < pivValue ) {
				less.add(m);
			}
			else {
				greater.add(m);
			}
		}


		//recursively sort less and greater
		ArrayList<Member> recurse = new ArrayList<Member>();

		recurse.addAll(quickSort(greater));
		recurse.add(pivM);
		recurse.addAll(quickSort(less));

		return recurse;
	}
}
