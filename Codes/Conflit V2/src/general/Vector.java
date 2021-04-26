package general;

public class Vector {
	
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	public double distance(Vector vector) {
		return Math.pow(Math.pow(this.x - vector.x, 2) + Math.pow(this.y - vector.y, 2), 0.5);
	}
	
	public Vector add(Vector vector) {
		return new Vector(vector.x + this.x, vector.y + this.y);
	}
	
	public Vector remove(Vector vector) {
		return new Vector(- vector.x + this.x, - vector.y + this.y);
	}
	
	public Vector multiply(double coeff) {
		return new Vector(this.x * coeff, this.y * coeff);
	}
	
	public boolean isInBounds() {
		return (x >= 0 & y >= 0 & x <= Controller.width & y <= Controller.height);
	}
	
	public double norm() {
		return Math.pow(Math.pow(this.x, 2) + Math.pow(this.y, 2), 0.5);
	}
	
	public Vector normalize(double norm) {
		return this.multiply(norm / this.norm());
	}
	
	public Vector copy() {
		return new Vector(this.x, this.y);
	}
}
