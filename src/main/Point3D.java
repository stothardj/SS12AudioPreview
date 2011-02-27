package main;

public class Point3D {
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Point3D(Point3D other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	public String toString() {
		return "{ X: " + x + ", Y: " + y + ", Z: " + z + " }";
	}
	public void scale(float s) {
		x *= s;
		y *= s;
		z *= s;
	}
	public float x, y, z;
}
