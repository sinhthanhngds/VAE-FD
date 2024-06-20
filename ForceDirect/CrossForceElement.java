

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class CrossForceElement
{

     double cosine;
     Point2D.Double crossVector;

     String crossEdges;       //a String representation of edges that cross


     //constructors

     public CrossForceElement()
     {
		 cosine = 0.0;
		 crossVector  = new Point2D.Double(0.0,0.0);
		 crossEdges = "";
	 }


     public CrossForceElement(double cos, Point2D.Double v, String ed)
     {
	 		 cosine = cos;
	 		 crossVector = new Point2D.Double(v.getX(), v.getY());
	 		 crossEdges = new String(ed);

	 }


	 public void setCsine(double cos)
	 {
		 cosine = cos;
	 }

	 public void setCrossVector(Point2D.Double v)
	 {
		 crossVector.setLocation(v.getX(), v.getY());
	 }

	 public double getCosine()
	 {
		 return cosine;
	 }

	 public Point2D.Double getCrossVector()
	 {
		 return crossVector;
	 }


	 public String getCrossEdges()
	 {
		 return new String(crossEdges);
	 }


	 public String toString()
	 {

		 return "crossEdges: " + crossEdges + " crossCosine: " + cosine + " Vertor: (" + crossVector.getX() + ", " + crossVector.getY() + ")\n";
		 //return " cosine: " + cosine + " Vertor: (" + orthoVector.getX() + ", " + orthoVector.getY() + ")\n";

	 }


}