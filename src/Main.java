import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import ch10.graphs.WeightedGraph;

public class Main {

	public static WeightedGraph<Intersection> streetMap;
	public static ArrayList<Intersection> vertices;
	
	public static void main(String[] args) throws FileNotFoundException {
		File kansas = new File("src/data/kansas.csv");
		File newyork = new File("src/data/newyork.csv");

		Scanner s = new Scanner(System.in);
		System.out.println("Would you like to use the map program? (y/n)");
		String response = s.next();
		while (!response.equals("n")) {
			streetMap = new WeightedGraph<Intersection>();
			vertices = new ArrayList<Intersection>();
			System.out.println("Which map would you like to use?");
			System.out.println("Select 1 for Kansas, select 2 for New York");
			response = s.next();
			if (response.equals("1")) {
				System.out.println("You selected Kansas");
				vertices = populateVertices(kansas);
			} else if (response.equals("2")) {
				System.out.println("You selected New York");
				vertices = populateVertices(newyork);
			} else {
				System.out.println("response not understood");
			}
			System.out.println("Would you like to try again? (y/n)");
			response = s.next();
		}
		s.close();
	}
	
	public static Intersection getIntersection(int st1, int st2, int st3) {
		Intersection i = new Intersection(st1, st2, st3);
		for (int y=0; y<vertices.size(); y++) {
			if (vertices.get(y).equals(i)) {
				return vertices.get(y);
			}
		}
		return null;
	}
	
	public static boolean sharesEdge(Intersection one, Intersection two, WeightedGraph<Intersection> map) {
		if (one.equals(two) || one.hasDeadEnd() && two.hasDeadEnd()) return false;
		int[] oneStreets = one.getStreets();
		int[] twoStreets = two.getStreets();
		int vertDif = Math.abs(oneStreets[1] - twoStreets[1]);
		int horizDif = Math.abs(oneStreets[0] - twoStreets[0]);
		if (oneStreets[0] == twoStreets[0] && vertDif == 1) return true;
		if (oneStreets[1] == twoStreets[1] && horizDif == 1) return true;
		if (oneStreets[0] == twoStreets[0] && vertDif > 1) { 
			for (int i=Math.min(oneStreets[1], twoStreets[1])+1; i<Math.max(oneStreets[1], twoStreets[1]); i++) {
				if (getIntersection(oneStreets[0], i, 0) != null ||
					getIntersection(oneStreets[0], i, 9) != null ||
					getIntersection(oneStreets[0], i, 6) != null) return false;
			}
			return true;
		}
		if (oneStreets[1] == twoStreets[1] && horizDif > 1) {
			for (int i=Math.min(oneStreets[0], twoStreets[0])+1; i<Math.max(oneStreets[0], twoStreets[0]); i++) {
				if (getIntersection(i, oneStreets[1], 0) != null ||
					getIntersection(i, oneStreets[1], 9) != null ||
					getIntersection(i, oneStreets[1], 6) != null) return false;
			}
			return true;
		}
		if (oneStreets[0] == twoStreets[2] && vertDif == 1) return true;
		return false;
	}

	public static ArrayList<Intersection> populateVertices(File file) throws FileNotFoundException {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		Scanner input = new Scanner(file, "UTF-8");
		String buff;
		String[] buffA;
		int[] parsed = new int[3];
		while (input.hasNext()) {
			buff = input.nextLine();
			buffA = buff.split(",");
			for (int l=0; l<3; l++) {
				if (buffA[l].length() == 2) buffA[l] = buffA[l].substring(1);
				parsed[l] = Integer.parseInt(buffA[l]);
			}
		
			Intersection i = new Intersection(parsed[0], parsed[1], parsed[2]);
			vertices.add(i);
			intersections.add(i);
			streetMap.addVertex(i);
		}
		input.close();
		translate(file.getName(), intersections);
		populateEdges(intersections);
		
		return intersections;
	}

	public static void populateEdges(ArrayList<Intersection> intersections) {
		int counter = 0;
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.size(); j++) {
				if (i != j && sharesEdge(intersections.get(i), intersections.get(j),streetMap)) {
					counter++;
					streetMap.addEdge(intersections.get(i), intersections.get(j), 1);
					//System.out.println(counter + "edge: " + intersections.get(i) + "   " + intersections.get(j));
				}
			}
		}
		findPath();
	}
	
	public static void findPath() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter 1st Intersection (3 digit number)");
		int startInt = scanner.nextInt();
		while (Integer.toString(startInt).length() != 3 || getIntersection(startInt/100, (startInt%100-startInt%10)/10, startInt%10) == null) {
			System.out.println(Integer.toString(startInt).length());
			System.out.println(startInt/100 + "" + (startInt%100-startInt%10)/10 + "" + startInt%10);
			System.out.println("Please enter a recognized 3 digit number (ex:123)");
			startInt = scanner.nextInt();
		}
		
		System.out.println("Please enter 2nd Intersection (3 digit number)");
		int endInt = scanner.nextInt();
		while (Integer.toString(endInt).length() != 3 || getIntersection(endInt/100, (endInt%100-endInt%10)/10, endInt%10) == null) {
			System.out.println("Please enter a recognized 3 digit number (ex:123)");
			startInt = scanner.nextInt();
		}
		
		Intersection start = getIntersection(startInt/100, (startInt%100-startInt%10)/10, startInt%10);
		Intersection end = getIntersection(endInt/100, (endInt%100-endInt%10)/10, endInt%10);
		System.out.println("Shortest distance: " + streetMap.getShortestDistance(start, end));
		//scanner.close();
	}
	
	public static void translate(String city, ArrayList<Intersection> intersections) throws FileNotFoundException {
		File file = new File("src/data/" + city.substring(0, city.length()-4) + "Translation.csv");
		ArrayList<String> rows = new ArrayList<String>();
		ArrayList<String> cols = new ArrayList<String>();
		Scanner input = new Scanner(file, "UTF-8");
		String buff;
		String[] buffA;
		while (input.hasNext()) {
			buff = input.nextLine();
			buffA = buff.split(",");
			for (int i=0; i<2; i++) {
				if (buffA[i].length() == 1) buffA[i] = "";
			}
			rows.add(buffA[0]);
			cols.add(buffA[1]);
		}
		input.close();
		int cnt = 1;
		for (Intersection intersection : intersections) {
			int[] names = intersection.getStreets();
			System.out.print("Intersection " + intersection + ": " + rows.get(names[0]-1) + " ");
			if (names[1] != 0) System.out.print(cols.get(names[1]-1) + " ");
			if (names[2] != 0 && names[2] != 9) System.out.print(rows.get(names[2]-1));
			System.out.print("\t\t");
			if (cnt%2==0) System.out.println();
			cnt++;
		}
		System.out.println();
	}

}
