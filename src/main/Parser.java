package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Parser {
	public ArrayList<Triangle> triangles;
	public ArrayList<Point3D> vertices;
	public Parser(String filename) {
		triangles = new ArrayList<Triangle>();
		vertices = new ArrayList<Point3D>();
		File file = new File(filename);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				char t = line.charAt(0);
				if(t == 'v' || t == 'f') {
					
					String args[] = line.split(" ");
					
					if( t == 'v') {
						float f1, f2, f3;
						f1 = Float.parseFloat(args[args.length - 3]);
						f2 = Float.parseFloat(args[args.length - 2]);
						f3 = Float.parseFloat(args[args.length - 1]);
						vertices.add(new Point3D(f1,f2,f3));
					} else {
						Point3D p1, p2, p3;
						p1 = vertices.get(Integer.parseInt(args[args.length - 3]) - 1);
						p2 = vertices.get(Integer.parseInt(args[args.length - 2]) - 1);
						p3 = vertices.get(Integer.parseInt(args[args.length - 1]) - 1);
						triangles.add(new Triangle(p1,p2,p3));
					}
					
				}
			}
		} catch (Exception e) {
			System.err.println("Parser error");
			e.printStackTrace();
		}
	} 
}
