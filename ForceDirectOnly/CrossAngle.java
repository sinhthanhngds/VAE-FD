//package graphs;

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import java.lang.*;

public class CrossAngle
{

	public static void  computeAngleForceElements(Graph g)
	{

		//prepare for computation
		//**********************************
	 	//clear angle elements for each vertex
	 	//clear rotate elements for each vertex
	 	//clear vertex info stored in edges
	 	g.clearGraphDynamics();

	 	g.buildEdges();


		//****************
		//****************
		//compute rotate forces

		long tr = System.currentTimeMillis();

		computeRotateForceElements(g);

		g.timeRotate += System.currentTimeMillis()-tr;

		//****************
		//****************



	 	ArrayList<Edge> edges = g.getEdges();


	 	int crossNumber = 0;
	 	double degreeSum = 0.0;
	 	double degreeMin =90.0;
	 	double degreeTemp= 0.0;

		//*******************************************
		//compute elements of angle forces for each pair of segments


		Edge lineOne, lineTwo;
		AngleForceElement ele1, ele2, ele3, ele4;

		CrossForceElement c1, c2, c3, c4;

		//==========
		ArrayList<Double> angleSize = new ArrayList<Double>();

		//==========


		long tc = System.currentTimeMillis();

		int crossno1, crossno2;
		double crosssum1, crossmin1, crosssum2,crossmin2;

		for (int i = 0; i < edges.size()-1; i++)
		{
			lineOne = edges.get(i);

			crossno1  = lineOne.getNo();
			crosssum1 = lineOne.getSum();
			crossmin1 = lineOne.getMin();

			for (int j = i+1; j < edges.size(); j++)
			{
				lineTwo = edges.get(j);

				crossno2  = lineTwo.getNo();
				crosssum2 = lineTwo.getSum();
				crossmin2 = lineTwo.getMin();


				//******************
				//1) two lines may share one end point: lineOne.getId1() == lineTwo.getId1()
				//2) two differents nodes may have the same coordinates
				//3) two edges completely overlap
				//4) two lines may partially overlap
				//5) one edge contains another
				//6) the interset point is an end point of another edge
				//7) either edge length =0
				//all the above will be tesed as true by intersectsLline(), but would not be considered for angle force
				//these conditions are handled in the following line of code:
				//if (lineOne.getP1().equals(lineTwo.getP1()) || lineOne.getP1().equals(lineTwo.getP2()) || lineOne.getP2().equals(lineTwo.getP1()) || lineOne.getP2().equals(lineTwo.getP2()))
					//continue;   //do nothing about angle force


				boolean overlap = (lineOne.ptSegDist(lineTwo.getP1())==0.0 ||lineOne.ptSegDist(lineTwo.getP2())==0.0 || lineTwo.ptSegDist(lineOne.getP1())==0.0 || lineTwo.ptSegDist(lineOne.getP2())==0.0);

				if (lineOne.intersectsLine(lineTwo) && (overlap == false))
				{


					String str = "("+lineOne.getId1()+","+lineOne.getId2()+") - ("+lineTwo.getId1()+","+lineTwo.getId2()+")";


					double cosOfAngle = computeCosine (lineOne.getP1(),lineOne.getP2(),lineTwo.getP1(),lineTwo.getP2());
					//System.out.println("cosine=  "+cosOfAngle+"\n");

					if (Double.isInfinite(cosOfAngle) || Double.isNaN(cosOfAngle))
					{
						//System.out.println("cosAngle:   *********************! \n");
						continue;
					}


					//if (false)

					if (g.COSINE_STRENGTH != 0.0)
					{


						//boolean clockwise = computeClockwise(lineOne.getP1(),lineTwo.getP1(),lineOne.getP2());
						//System.out.println("clockwise=  "+clockwise+"\n");
						boolean clockwise=true;

						//start to compute unitOrtho and assign force element to each vertex associated
						//if clockwise is true, ponits on lineOne have clockwise unit forces, and anti-clockwise on lineTwo

						ele1 = new AngleForceElement(cosOfAngle, computeUnitVector(lineTwo.getP1(),lineTwo.getP2(),clockwise), str);
						g.getVertex(lineOne.getId1()).addElement(ele1);

						ele2 = new AngleForceElement(cosOfAngle, computeUnitVector(lineTwo.getP2(),lineTwo.getP1(),clockwise),str);
						g.getVertex(lineOne.getId2()).addElement(ele2);


						ele3 = new AngleForceElement(cosOfAngle, computeUnitVector(lineOne.getP1(),lineOne.getP2(),!clockwise),str);
						g.getVertex(lineTwo.getId1()).addElement(ele3);

						ele4 = new AngleForceElement(cosOfAngle, computeUnitVector(lineOne.getP2(),lineOne.getP1(),!clockwise),str);
						g.getVertex(lineTwo.getId2()).addElement(ele4);

					    //System.out.println(str + "  Cosine force: " + crossNumber +" cosines: " + cosOfAngle +"\n");
					}

					crossNumber++;
					degreeTemp = Math.toDegrees(Math.acos(Math.abs(cosOfAngle)));
					degreeSum += degreeTemp;
					degreeMin = Math.min(degreeMin,degreeTemp);

					//======
					angleSize.add(new Double(degreeTemp));
					//======


					//set edge related crossing number and angle

					crossno1 ++;
					crosssum1 += degreeTemp;
					if (crossmin1 == 0.0)
						crossmin1 = degreeTemp;
					else
						crossmin1 = Math.min(crossmin1,degreeTemp);

					crossno2 ++;
					crosssum2 += degreeTemp;
					if (crossmin2 == 0.0)
						crossmin2 = degreeTemp;
					else
						crossmin2 = Math.min(crossmin2,degreeTemp);

                    edges.get(j).setNo(crossno2);
                    edges.get(j).setSum(crosssum2);
                    edges.get(j).setMin(crossmin2);
					//************

				}
			}

			edges.get(i).setNo(crossno1);
			edges.get(i).setSum(crosssum1);
			edges.get(i).setMin(crossmin1);

		}



		g.timeCross += System.currentTimeMillis()-tc;  //must use +=, since anytime this method
													//is called, time spent has to be added

		if (crossNumber !=0)
		{
			//======
			double dm = degreeSum/crossNumber;

			double sqrSum=0.0;

			for (int i=0; i<angleSize.size(); i++)
			{
				sqrSum += (angleSize.get(i).doubleValue()-dm)*(angleSize.get(i).doubleValue()-dm);
			}

			double dd = Math.sqrt(sqrSum/angleSize.size());

			g.setDegreeMean(dm);
			g.setDegreeDeviation(dd);


			//======

			g.setNumOfCrosses(crossNumber);
			g.setSumOfDegrees(degreeSum);
			g.setMinCrossAngle(degreeMin);


		}

		Vertex pt;
		Edge   lineThree;
		double d, l, r;

		Point2D p1,p2, p3;
		PointEdgeElement peEle, peEle1;

		double minDist=0;
		boolean firstTime = true;

		//if (false)

		for (int i = 0; i < g.size(); i++)
		{
			pt = g.getVertex(i);

			//System.out.print("C: " + i +"\n");

			for (int j = 0; j < edges.size(); j++)
			{
				lineThree = edges.get(j);

				if ((pt.getId() == lineThree.getId1()) || (pt.getId() == lineThree.getId2()))
					continue;

				p1 = lineThree.getP1();
				p2 = lineThree.getP2();


				d  = lineThree.ptSegDist(pt);

				if (firstTime)
				{
					firstTime =false;
					minDist = d;
				}
				else
				{
					if (d<minDist)
					{
						minDist = d;
					}
				}



				//System.out.print("d: " + d +"\n");

				/****

				if (d<0.00001)
					d = 0.0000001;


				*****/

/*

Subject 1.02: How do I find the distance from a point to a line?


Let the point be C (Cx,Cy) and the line be AB (Ax,Ay) to (Bx,By).
Let P be the point of perpendicular projection of C on AB.  The parameter
r, which indicates P's position along AB, is computed by the dot product
of AC and AB divided by the square of the length of AB:

(1)    AC dot AB
	r = ---------
		||AB||^2

r has the following meaning:

	r=0      P = A
	r=1      P = B
	r<0      P is on the backward extension of AB
	r>1      P is on the forward extension of AB
	0<r<1    P is interior to AB

The length of a line segment in d dimensions, AB is computed by:

	L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 + ... + (Bd-Ad)^2)

so in 2D:

	L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )

and the dot product of two vectors in d dimensions, U dot V is computed:

	D = (Ux * Vx) + (Uy * Vy) + ... + (Ud * Vd)

so in 2D:

	D = (Ux * Vx) + (Uy * Vy)

So (1) expands to:

		(Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
	r = -------------------------------
					  L^2

The point P can then be found:

	Px = Ax + r(Bx-Ax)
	Py = Ay + r(By-Ay)

And the distance from A to P = r*L.

Use another parameter s to indicate the location along PC, with the
following meaning:
	  s<0      C is left of AB
	  s>0      C is right of AB
	  s=0      C is on AB

Compute s as follows:

		(Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay)
	s = -----------------------------
					L^2


Then the distance from C to P = |s|*L.

*/

				if (g.POINTEDGE_STRENGTH != 0.0 && d < 0.0001)
				{
					l  = lineThree.getLength();
					r  = ((p1.getY()-pt.getY())*(p1.getY()-p2.getY())-(p1.getX()-pt.getX())*(p2.getX()-p1.getX()))/l/l;
					//System.out.print("l: " + l +"\n");

					if (r>0 && r<1)
					{

						//compute cross point
						p3 = new Point2D.Double(p1.getX()+r*(p2.getX()-p1.getX()),p1.getY()+r*(p2.getY()-p1.getY()));

						//System.out.print("Px: " + p3.getX() +"  Py: " + p3.getY() +"\n");
						peEle = new PointEdgeElement(d,computeUnitVector(p3, pt, true), lineThree.getId1()+"-"+lineThree.getId2());

						pt.addPointEdgeEle(peEle);

						peEle1 = new PointEdgeElement(2d,computeUnitVector(pt, p3, true), lineThree.getId1()+"-"+lineThree.getId2());

						g.getVertex(lineThree.getId1()).addPointEdgeEle(peEle1);
						g.getVertex(lineThree.getId2()).addPointEdgeEle(peEle1);


					}
				}


			}

		}


		g.setMinPtEdgDist(minDist);




	}



    // compute cosine between vector a->b and vector c->d
	public static double computeCosine(Point2D a, Point2D b, Point2D c, Point2D d)
	{
			double dotProduct = (b.getX() - a.getX())*(d.getX() - c.getX()) + (b.getY() - a.getY())*(d.getY() - c.getY());

	        double d1 = a.distance(b);
	        double d2 = c.distance(d);


			//if (d1<0.0000001 || d2<0.0000001)     //if one of the lines is too short, no cosines will be computed
			//	d1 =0.0;

			//System.out.println("dot= "+ dotProduct +"  d1=  "+ d1 +"  d2=  "+ d2);

	       	double cos = dotProduct/(d1*d2);

	       //	System.out.println("cosine=  " + cos );

			//if (Double.isInfinite(cos) || Double.isNaN(cos))
			//{
				//System.out.println("cosAngle:   *********************!"+ d1+","+d2+"\n");
			//}
			return cos;


    }

	//compute a unit vector of a vector a->b
    public static Point2D.Double computeUnitVector(Point2D a, Point2D b, boolean cw)
    {
	        double d = a.distance(b);

			if (d<0.00001)
				d = 0.00001;


			//the following is for new cosine force. clockwise or not doesn't matter anymore.

			return new Point2D.Double((b.getX() - a.getX())/d, (b.getY() - a.getY())/d);

    }

    //determine the relative position of three points p1,p2,p3: clockwise or anti-colockwise
    public static boolean computeClockwise(Point2D P1, Point2D P2, Point2D P3)
    {

		//the following only applies when the three points are different and not on the same line
		//condition overlap == false make sure the above is always the case
		Double E1x = P1.getX() - P2.getX();
		Double E1y = P1.getY() - P2.getY();
		Double E2x = P3.getX() - P2.getX();
		Double E2y = P3.getY() - P2.getY();

		if ((E1x * E2y - E1y * E2x) >= 0)
			return true;             //clockwise
		else
			return false;



    }


	public static void computeRotateForceElements(Graph g)
	{
		ArrayList<Vertex> vertices = g.getVertices();

		//follwoing varibles are used to compute the measures
		double min = 2 * Math.PI;
		double fin = 0.0;
		double devi= 0.0;

		int num = 0;  //number of non-one-edge nodes


		for (Vertex u: vertices)  //1
		{

			int id = u.getId();
			ArrayList<Integer> adjNodes = u.getAdjacentNodes();

			int degree = adjNodes.size();

			if (degree < 2)
				continue;

			num++;


			ArrayList<Vertex> adjVertices = new ArrayList<Vertex>();


			//fill the adjVertices with unit vectors from u to the adj points

			for (Integer I: adjNodes)
			{
				Point2D.Double uv = computeUnitVector(u, g.getVertex(I.intValue()),true);
				adjVertices.add(new Vertex(I.intValue(),uv.getX(), uv.getY()));
			}


			//then order the adjacent vertices clockwisely
			//order unit factors with y>0 from left to right
			//order unit factors with y<0 from right to left
			ArrayList<Vertex> orderVertices1 = new ArrayList<Vertex>();
			ArrayList<Vertex> orderVertices2 = new ArrayList<Vertex>();

			int m,n;

			for (Vertex v: adjVertices)
			{
				if (v.getY()>0)
				{
					if (orderVertices1.size() ==0)
						orderVertices1.add(v);
					else
					{
						m=0;
						for (int i=0; i<orderVertices1.size(); i++)
						{
							if (v.getX() < orderVertices1.get(i).getX())
							{
								orderVertices1.add(i,v);
								m=1;
								break;
							}

						}
						if (m==0)
							orderVertices1.add(v);
					}
				}
				else
				{
					if (orderVertices2.size() ==0)
						orderVertices2.add(v);
					else
					{
						n=0;
						for (int i=0; i<orderVertices2.size(); i++)
						{
							if (v.getX() > orderVertices2.get(i).getX())
							{
								orderVertices2.add(i,v);
								n=1;
								break;
							}

						}
						if (n==0)
							orderVertices2.add(v);
					}

				}
			}


            //combine both together, and order them clockwisely
			for (Vertex v: orderVertices2)
			{
				orderVertices1.add(v);
			}


			//then order the origional adjacent vertices clockwisely
			ArrayList<Vertex> orderVertices = new ArrayList<Vertex>();

			for (Vertex v: orderVertices1)
			{
				orderVertices.add(g.getVertex(v.getId()));
			}



			// print the node id in clockwise order
			String str="Node " + id + " adjacent nodes in clockwise: ";
			for (int i=0; i<orderVertices.size(); i++)
				str += orderVertices.get(i).getId() + "  ";
			str +="\n";
			//System.out.println(str);



			//compute rotate force elements

			double goodAngle = 2.0*Math.PI/degree;

			double initialAngle = 0.0;

			double sumAngle =0.0;
			double maxAngle =0.0;
			double minAngle = 2.0*Math.PI;

			double deviAngle = 0.0;
			Vertex v1, v2;

			double sin;

			Vertex maxAngleV1 = new Vertex();
			Vertex maxAngleV2 = new Vertex();


			RotateForceElement rot1, rot2;

			//when measuring angle between vectors clockwise, there exists only an angle more than 180 dgree if any
			//if this is the case, then this is a problem, since the angle we get will be the one that is less than 180.
			//and this may cause problem in terms of the force direction



			//////*************
			//set up variables to compute the central point
			double cenX=0.0;
			double cenY=0.0;


			//first we find the two vertices that form the the biggest angle
			//and compute the angular resolution measures by the way
			for (int i=1; i<=orderVertices.size(); i++)
			{
				v1 = orderVertices.get(i-1);

				cenX += v1.getX();
				cenY += v1.getY();


				if (i == orderVertices.size())   //the end node and first node are clockwise
					v2 = orderVertices.get(0);
				else
					v2 = orderVertices.get(i);

				initialAngle = Math.acos(computeCosine(u,v1,u,v2));

				if (initialAngle>maxAngle)
				{
					maxAngle   = initialAngle;
					maxAngleV1 = v1;    //the vertex of the fist vector is recorded
					maxAngleV2 = v2;
				}

				if (initialAngle<minAngle)
					minAngle = initialAngle;
			}


			//************************************************
			//compute a force that pulls  u to the barry centre of its adj nodes

			//Point2D.Double centre = new Point2D.Double(cenX/degree, cenY/degree);

			//RotateForceElement rotU = new RotateForceElement(u.distance(centre),computeUnitVector(u, centre, true),"");

			//u.addRotateEle(rotU);


			//**************
			//***************

			boolean rotateYN = true;             //rotate force

			//boolean rotateYN = false;            //acute force


			//if the node only has two edges, compute the force once based on the smaller angle


			if (orderVertices.size() == 2)
			{
				v1 = orderVertices.get(0);
				v2 = orderVertices.get(1);

				initialAngle = Math.acos(computeCosine(u,v1,u,v2));

				sumAngle += initialAngle;

				deviAngle += (initialAngle - goodAngle)*(initialAngle - goodAngle);




				//*****************************************
				sin = Math.sin((goodAngle-initialAngle)/2);


				//sin = (goodAngle-initialAngle)/180;  //this option has been tested, no good


				str = "("+u.getId()+","+v1.getId()+") - ("+u.getId()+","+v2.getId()+")";

				if (computeClockwise(u, v1, v2) == true)
				{

					if (rotateYN == true)
					{
						rot1 = new RotateForceElement(sin,computeRotateVector(u,v1,false),str);
					    rot2 = new RotateForceElement(sin,computeRotateVector(u,v2,true),str);
					}
					else
					{

						rot1 = new RotateForceElement(sin,computeAcuteVector(u,v1,false,initialAngle),str);
						rot2 = new RotateForceElement(sin,computeAcuteVector(u,v2,true,initialAngle),str);
					}

					v1.addRotateEle(rot1);
					v2.addRotateEle(rot2);
				}
				else
				{


					if (rotateYN == true)
					{
						rot1 = new RotateForceElement(sin,computeRotateVector(u,v1,true),str);
						rot2 = new RotateForceElement(sin,computeRotateVector(u,v2,false),str);
					}
					else
					{
						rot1 = new RotateForceElement(sin,computeAcuteVector(u,v1,true,initialAngle),str);
						rot2 = new RotateForceElement(sin,computeAcuteVector(u,v2,false,initialAngle),str);
					}

					v1.addRotateEle(rot1);
					v2.addRotateEle(rot2);

				}
			}
			else
			{
				for (int i=1; i<=orderVertices.size(); i++)
				{
					v1 = orderVertices.get(i-1);

					if (i == orderVertices.size())   //the end node and first node are clockwise
						v2 = orderVertices.get(0);
					else
						v2 = orderVertices.get(i);

					initialAngle = Math.acos(computeCosine(u,v1,u,v2));


					// ideal angle > initial angle, repulsive force on v1 and v2, which means
					// anti-clickwise on v1 and clockwise force on v2
					//otherwise, attravtive


                   //*****************************************
					sin = Math.sin((goodAngle-initialAngle)/2);


					//sin = (goodAngle-initialAngle)/180;



					str = "("+u.getId()+","+v1.getId()+") - ("+u.getId()+","+v2.getId()+")";

					//when sin<0, the following will become attractive force
					//when sin>0, rot1 is repulsive
					//however, for vectors that form biggest angle, should always be attractive in terms of the clockwise angle
					//which means that we store the value of sin negitive always

					//note that suppose v1 and v2 are clockwise, we always apply counter-cw force on v1
					// and cw force on v2. the direction is determined by the value of sin
					//

					if (v1.getId() == maxAngleV1.getId())
					{
						sin = -1 * Math.abs(sin);
						//System.out.println("v1 id = " +v1.getId());
					}
					else
					{
						sumAngle += initialAngle;   //sum the angles except the biggest one

						deviAngle += (initialAngle - goodAngle)*(initialAngle - goodAngle);  //compute the sum of difference square except the biggest one
					}

					if (rotateYN == true)
					{

						rot1 = new RotateForceElement(sin,computeRotateVector(u,v1,false),str);
						rot2 = new RotateForceElement(sin,computeRotateVector(u,v2,true),str);
					}
					else
					{

						rot1 = new RotateForceElement(sin,computeAcuteVector(u,v1,false,initialAngle),str);
						rot2 = new RotateForceElement(sin,computeAcuteVector(u,v2,true,initialAngle),str);
					}


					//apply force in the direction of the other edge is not very good
					//rot1 = new RotateForceElement(sin,computeUnitVector(v2,u,false),str);
					//rot2 = new RotateForceElement(sin,computeUnitVector(v1,u,true),str);

					v1.addRotateEle(rot1);
					v2.addRotateEle(rot2);


				}
			}

			//the biggest angle is 2*Math.PI minus the sume of the rest angles
			deviAngle += ((2*Math.PI-sumAngle)-goodAngle)*((2*Math.PI-sumAngle)-goodAngle);   //add the variance for the biggest angle

			//compute the variance and assign to the same variable
			deviAngle = Math.sqrt(deviAngle/degree);


			//compute the angular resolution measures
			devi += deviAngle;
			fin  += goodAngle-minAngle;
			if (minAngle<min)
				min = minAngle;

			//System.out.println("maxAngle= " +Math.toDegrees(maxAngle) +"--"+Math.toDegrees(2*Math.PI-sumAngle));
		}   //1

		g.setDeviVertexAngle(Math.toDegrees(devi/num));
		g.setMinVertexAngle(Math.toDegrees(min));
		g.setFinVertexAngle(Math.toDegrees(fin/num));

	}

	//compute a unit vector orthogonal to a vector a->b in closewise or anti-clockwise
    public static Point2D.Double computeRotateVector(Point2D a, Point2D b, boolean cw)
    {
	        double d = a.distance(b);


			if (cw == false)
				return new Point2D.Double((-b.getY() + a.getY())/d, (b.getX() - a.getX())/d);
			else
				return new Point2D.Double((b.getY() - a.getY())/d, (-b.getX() + a.getX())/d);

    }

    //rotate a vector a->b by angle ta in closewise or anti-clockwise
    //return the resulting unit vector
	public static Point2D.Double computeAcuteVector(Point2D a, Point2D b, boolean cw, double ta)
	{
			double d = a.distance(b);

			double x1=b.getX()-a.getX();
			double y1=b.getY()-a.getY();

			double x2,y2;

			if (ta>=0.0 && ta<Math.PI/2.0)
				ta=(Math.PI-ta)/2.0;
				//ta=(Math.PI+ta)/2.0;
			else
				ta=ta/2.0;
				//ta=Math.PI-ta/2.0;

			if (cw == false)
			{
				x2=x1*Math.cos(ta)-y1*Math.sin(ta);
				y2=y1*Math.cos(ta)+x1*Math.sin(ta);
			}
			else
			{
				x2=x1*Math.cos(ta)+y1*Math.sin(ta);
				y2=y1*Math.cos(ta)-x1*Math.sin(ta);
			}

			return new Point2D.Double(x2/d, y2/d);
			//return new Point2D.Double(x2, y2);

	}



}
