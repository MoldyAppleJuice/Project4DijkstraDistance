import java.util.Arrays;

import ch10.graphs.WeightedGraph;


public class Intersection {
	
	private int[] streets; 
	private int horizontal, vertical, angle;
	
	public Intersection(int horiz, int vert, int ang) {
		streets = new int[3];
		horizontal = horiz;
		streets[0] = horiz;
		vertical = vert;
		streets[1] = vert;
		angle = ang;
		streets[2] = ang;
	}
	
	public int[] getStreets() {
		return streets;
	}
	
	public boolean hasDeadEnd() {
		return (angle == 9);
	}
	
	public String toString() {
		return streets[0] + "" + streets[1] + "" + streets[2];
	}
	
	public boolean equals(Intersection other) {
		return streets[0] == other.getStreets()[0] && 
			   streets[1] == other.getStreets()[1] &&
			   streets[2] == other.getStreets()[2];
	}
}
