import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

public class Walker {
	
	double x,y;
	int lastTimeHome;
	Color color;
	HashMap<Integer, Point> territory;
	
	
	Walker(double a, double b){
		x=a;
		y=b;
		lastTimeHome=0;
		color = Color.getHSBColor((float)Math.random()*360, 1, 1);
		territory = new HashMap<Integer, Point>();
	}
	
	
	Point move(moveType type) {
		Random random = new Random();
		switch(type) {
		
		case CROSS:
			
			int rand = random.nextInt(4);
			
			switch (rand) {
				case 0: return new Point(x+1, y);
				case 1: return new Point(x-1, y);
				case 2: return new Point(x, y+1);
				case 3: return new Point(x, y-1);
				
				default: return this.toPoint();
		}
			
		case STAR:

			return new Point((int) (x+Math.pow(-1, random.nextInt(2))), (int) (y+Math.pow(-1, random.nextInt(2))));
		
		case CONTINUOUS:
			
			double theta = Math.random()*2*Math.PI;
			return new Point(x+2*Math.cos(theta), y+2*Math.sin(theta));
			
		case RANDOMWALK:
			
			double alpha =  Math.log(1-random.nextDouble()*(1-Math.exp(-2*Math.PI*(0.01*Math.pow(0.9, lastTimeHome)))))/(-0.01*Math.pow(0.9, lastTimeHome));
			Point out = new Point(x+Math.cos(alpha), y+Math.sin(alpha));
			
			if(this.territoryContains(type, out)) lastTimeHome=0;
			else lastTimeHome++;
			
			return out;
			
		default:
			return this.toPoint();
		}
			
	}
	
	boolean territoryContains(moveType type, Point p) {
		
		if(type==moveType.CONTINUOUS || type==moveType.RANDOMWALK) {
			for(Point v : territory.values()) {
				if(Math.pow(v.x-p.x,2) + Math.pow(v.y-p.y,2)<=1) return true;
			}
			return false;
		}
				
		else {
			for(Point v : territory.values()) {
				if(v.x==p.x && v.y==p.y) return true;
			}
			return false;
		}
	}
	
	Point toPoint() {
		return new Point(x,y);
	}
	
	void moveTo(Point p){
		x=p.x;
		y=p.y;
	}
	
	public String toString() {
		return "[ "+x+" ; "+y+" ]";
	}
}