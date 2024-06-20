//package graphs;

import java.awt.geom.*;
import java.util.ArrayList;
import java.lang.*;

public class Edge extends Line2D.Double
{

	//note that an edge only containe the coordinates and IDs of its vertices. no elements info are stored.


	int id1, id2;

	//the following varisbles are for computing cross number ,sum of cross angles and min cross angle
	// on a path

	int crossNo;
	double crossSum, crossMin;

	public Edge()
	{
		super();
		id1 = -1;
		id2 = -1;
		crossNo  =  0;
		crossSum =  0.0;
		crossMin  =  0.0;
	}

	public Edge (Vertex v1, Vertex v2)
	{
		super(v1.getX(),v1.getY(), v2.getX(), v2.getY());
		id1 = v1.getId();
		id2 = v2.getId();
		crossNo  =  0;
		crossSum =  0.0;
		crossMin  =  0.0;
	}



	public int getId1()
	{
		return id1;
	}

	public int getId2()
	{
		return id2;
	}

	public void setId1(int id)
	{
		id1 = id;
	}

	public void setId2(int id)
	{
		id2 = id;
	}

	//===

	public void setNo(int no)     //number of crossings
	{
		crossNo=no;
	}
	public void setSum(double sum)  //sum of crossing angles
	{
		crossSum=sum;
	}
	public void setMin(double min)   //min angle of crossings
	{
		crossMin=min;
	}
	public int getNo()
	{
		return crossNo;
	}
	public double getSum()
	{
		return crossSum;
	}
	public double getMin()
	{
		return crossMin;
	}



	//===



	public double getLength()
	{
		return super.getP1().distance(super.getP2());
	}



}