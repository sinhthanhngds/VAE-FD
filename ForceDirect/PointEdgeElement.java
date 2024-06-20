

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class PointEdgeElement
{

     double dist;
     Point2D.Double orthoVector;

     String pointEdge;       //a String representation of edge corresponding to the element


     //constructors

     public PointEdgeElement()
     {
		 dist = 0;
		 orthoVector  = new Point2D.Double(0.0,0.0);
		 pointEdge = "";
	 }


     public PointEdgeElement(double d, Point2D.Double ov, String pe)
     {
	 		 dist = d;
	 		 orthoVector = new Point2D.Double(ov.getX(), ov.getY());
	 		 pointEdge = new String(pe);

	 }


	 public void setDist(double d)
	 {
		 dist = d;
	 }

	 public void setOrthoVector(Point2D.Double ov)
	 {
		 orthoVector.setLocation(ov.getX(), ov.getY());
	 }

	 public double getDist()
	 {
		 return dist;
	 }

	 public Point2D.Double getOrthoVector()
	 {
		 return orthoVector;
	 }


	 public String getPointEdge()
	 {
		 return new String(pointEdge);
	 }


	 public String toString()
	 {

		 return "pointEdge: " + pointEdge + " PointToEdge distance: " + dist + " Vertor: (" + orthoVector.getX() + ", " + orthoVector.getY() + ")\n";
		 //return " cosine: " + cosine + " Vertor: (" + orthoVector.getX() + ", " + orthoVector.getY() + ")\n";

	 }


}
