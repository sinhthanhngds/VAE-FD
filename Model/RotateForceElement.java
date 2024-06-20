

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class RotateForceElement
{

     double sine;
     Point2D.Double rotateVector;

     String rotateEdges;       //a String representation of edges that cross


     //constructors

     public RotateForceElement()
     {
		 sine = 0.0;
		 rotateVector  = new Point2D.Double(0.0,0.0);
		 rotateEdges = "";
	 }


     public RotateForceElement(double sin, Point2D.Double v, String ed)
     {
	 		 sine = sin;
	 		 rotateVector = new Point2D.Double(v.getX(), v.getY());
	 		 rotateEdges = new String(ed);

	 }


	 public void setSine(double sin)
	 {
		 sine = sin;
	 }

	 public void setRotateVector(Point2D.Double v)
	 {
		 rotateVector.setLocation(v.getX(), v.getY());
	 }

	 public double getSine()
	 {
		 return sine;
	 }

	 public Point2D.Double getRotateVector()
	 {
		 return rotateVector;
	 }


	 public String getRotateEdges()
	 {
		 return new String(rotateEdges);
	 }


	 public String toString()
	 {

		 return "rotateEdges: " + rotateEdges + " rotateSine: " + sine + " Vertor: (" + rotateVector.getX() + ", " + rotateVector.getY() + ")\n";

	 }


}