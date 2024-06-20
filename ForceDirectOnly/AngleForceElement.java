

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class AngleForceElement
{

     double cosine;
     Point2D.Double orthoVector;

     String crossEdges;       //a String representation of edges that cross


     //constructors

     public AngleForceElement()
     {
		 cosine = 0;
		 orthoVector  = new Point2D.Double(0.0,0.0);
		 crossEdges = "";
	 }


     public AngleForceElement(double cos, Point2D.Double ov, String ed)
     {
	 		 cosine = cos;
	 		 orthoVector = new Point2D.Double(ov.getX(), ov.getY());
	 		 crossEdges = new String(ed);

	 }


	 public void setCosine(double cos)
	 {
		 cosine = cos;
	 }

	 public void setOrthoVector(Point2D.Double ov)
	 {
		 orthoVector.setLocation(ov.getX(), ov.getY());
	 }

	 public double getCosine()
	 {
		 return cosine;
	 }

	 public Point2D.Double getOrthoVector()
	 {
		 return orthoVector;
	 }


	 public String getCrossEdges()
	 {
		 return new String(crossEdges);
	 }


	 public String toString()
	 {

		 return "crossEdges: " + crossEdges + " angleCosine: " + cosine + " Vertor: (" + orthoVector.getX() + ", " + orthoVector.getY() + ")\n";
		 //return " cosine: " + cosine + " Vertor: (" + orthoVector.getX() + ", " + orthoVector.getY() + ")\n";

	 }


}