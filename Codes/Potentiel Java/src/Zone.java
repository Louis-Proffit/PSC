import java.util.LinkedList;

public class Zone {
	
	public Point[] bound;
	public int len;
	public double priority;
	public double decreasingSpaceInfluence = 0.5;
	public double potentialIncreasePerStep = 1;
	
	public Zone(Point[] bound, double priority) {
		this.priority = priority;
		// Le dernier point est également le premier
		this.len = bound.length;
		this.bound = new Point[len + 1];
		for (int i = 0; i < len ; i++) {
			this.bound[i] = bound[i];
		}
		this.bound[len] = bound[0];
	}

	public boolean isInBound(Point point) {
		int crosses = 0;
		for (int i = 0 ; i < len ; i++) {
			if (Math.abs(bound[i].y - point.y) < Controller.tolerance) return isInBound(new Point(point.x, point.y + Controller.tolerance));
			if (Math.abs(bound[i + 1].y - bound[i].y) < Controller.tolerance) {
				if (Math.abs(point.y - bound[i].y) < Controller.tolerance) return isInBound(new Point(point.x, point.y + Controller.tolerance));
				else continue;
			}
			double t = (point.y - bound[i].y) / (bound[i + 1].y - bound[i].y);
			if (0 <= t & t < 1) {
				if (point.x < (1 - t) * bound[i].x + t * bound[i + 1].x) {
					crosses +=1;
				}
			}
		}
		if (crosses % 2 == 0) return false; 
		return true;
	}
	
	public LinkedList<Integer[]> getInteriorPoints(double width, double height, int n, int m){
		LinkedList<Integer[]> result = new LinkedList<Integer[]>();
		double[] extremeCoordinates = this.getExtremeCoordinates();
		int minI = (int)(extremeCoordinates[0] / width * n + 1);
		int maxI = (int)(extremeCoordinates[1] / width * n);
		int minJ = (int)(extremeCoordinates[2] / height * m + 1);
		int maxJ = (int)(extremeCoordinates[3] / height * m);
		for (int i = minI; i <= maxI; i++) {
			for (int j = minJ ; j <= maxJ ; j++) {
				if (this.isInBound(new Point(i * width / n, j * height / m)))
					result.add(new Integer[] {i, j});
			}
		}
		return result;
	}
	
	public double[] getExtremeCoordinates() {
		double minX = bound[0].x;
		double maxX = bound[0].x;
		double minY = bound[0].y;
		double maxY = bound[0].y;
		for (int i = 0 ; i < len ; i++) {
			if (bound[i].x < minX) minX = bound[i].x;
			if (bound[i].x > maxX) maxX = bound[i].x;
			if (bound[i].y < minY) minY = bound[i].y;
			if (bound[i].y > maxY) maxY = bound[i].y;
		}
		return new double[] {minX, maxX, minY, maxY};
	}
}
