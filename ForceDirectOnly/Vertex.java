//package graphs;

import java.awt.geom.*;
import java.util.ArrayList;
import java.lang.*;

public class Vertex extends Point2D.Double
{

	int id;


	ArrayList <Integer> adjacentNodes;  //store adjacent nodes' id

	ArrayList <AngleForceElement> elements;
	ArrayList <CrossForceElement> crossEles;

	ArrayList <RotateForceElement> rotateEles;
	ArrayList <PointEdgeElement> pointEdgeEles;

	//default constructor
	public Vertex()
	{
		super();
		id  = -1;

		adjacentNodes   = new ArrayList<Integer>();

		elements = new ArrayList<AngleForceElement>();
		crossEles = new ArrayList<CrossForceElement>();

		rotateEles = new ArrayList<RotateForceElement>();
		pointEdgeEles = new ArrayList<PointEdgeElement>();


	}
	//

	public Vertex(int id)
	{
		super();
		this.id = id;

		adjacentNodes = new ArrayList<Integer>();
		elements = new ArrayList<AngleForceElement>();
		crossEles = new ArrayList<CrossForceElement>();

		rotateEles = new ArrayList<RotateForceElement>();

		pointEdgeEles = new ArrayList<PointEdgeElement>();

	}

	public Vertex(int id, double x, double y)
	{
		super(x,y);
		this.id = id;

		adjacentNodes = new ArrayList<Integer>();
		elements = new ArrayList<AngleForceElement>();
		crossEles = new ArrayList<CrossForceElement>();

		rotateEles = new ArrayList<RotateForceElement>();
		pointEdgeEles = new ArrayList<PointEdgeElement>();



	}

	//for two vertices to be equal, they should have the same Id and (x,y) coordinates
	public boolean vertexEquals(Vertex v)
	{
		return (super.equals(v)) && (this.id == v.getId());
	}

	//to make a difference between vertexEquals and pointEquals, we implement this medthod
	//this method only compares coordinates, but takes Vertex as input

	public boolean pointEquals(Vertex v)
	{
		return super.equals(v);
	}


	//**********************
	//get and set methods
	//**********************

	public int getId()
	{
			return id;
	}


	//*******************
	//get and set vertex Angle elements

	public ArrayList<AngleForceElement> getElements()
	{
		return elements;

	}


	public void addElement(AngleForceElement ele)
	{
		elements.add(ele);
	}


	public void clearElements()
	{
		elements.clear();    //evey time nodes move, elements have to be cleared and recomputed
	}


	//*******************
	//get and set vertex cross elements
	public ArrayList<CrossForceElement> getCrossEles()
	{
		return crossEles;

	}


	public void addCrossEle(CrossForceElement ele)
	{
		crossEles.add(ele);
	}


	public void clearCrossEles()
	{
		crossEles.clear();    //evey time nodes move, elements have to be cleared and recomputed
	}


	//*******************
	//get and set vertex rotate elements
	public ArrayList<RotateForceElement> getRotateEles()
	{
		return rotateEles;

	}


	public void addRotateEle(RotateForceElement ele)
	{
		rotateEles.add(ele);
	}


	public void clearRotateEles()
	{
		rotateEles.clear();    //evey time nodes move, elements have to be cleared and recomputed
	}


	//*******************
	//get and set vertex pointEdge elements
	public ArrayList<PointEdgeElement> getPointEdgeEles()
	{
		return pointEdgeEles;

	}


	public void addPointEdgeEle(PointEdgeElement ele)
	{
		pointEdgeEles.add(ele);
	}


	public void clearPointEdgeEles()
	{
		pointEdgeEles.clear();    //evey time nodes move, elements have to be cleared and recomputed
	}

	//**************************
	//get and add adjacent nodes

	// return adjacent nodes list for this vertex
	public ArrayList<Integer> getAdjacentNodes()
	{
		return adjacentNodes;
	}




	public void addAdjacentNode(Vertex u)
	{
		adjacentNodes.add(new Integer(u.getId()));
	}


	public void addAdjacentNode(int id)
	{
		adjacentNodes.add(new Integer(id));
	}


	public void randomMove(double scaleFactor)
	{
		//super.setLocation(super.getX() + scaleFactor*2.0*(0.5 - Math.random()),super.getY() + scaleFactor*2.0*(0.5 - Math.random())  );
		super.setLocation(scaleFactor*2.0*(0.5 - Math.random()),scaleFactor*2.0*(0.5 - Math.random()));

	}



	public void printVertex()
	{
		String str="id= "+ this.id +":  ("+ super.getX() +"," +  super.getY() +")  adjacent vertices: ";
		for (int i = 0; i < adjacentNodes.size(); i++)
		{
			str += adjacentNodes.get(i).intValue() +"  ";
		}

		//print associated angle elements
		str += "\n";

		for (int i = 0; i < elements.size(); i++)
		{
			str += elements.get(i).toString() +" \n";
		}

		for (int i = 0; i < crossEles.size(); i++)
		{
			str += crossEles.get(i).toString() +" \n";
		}

		for (int i = 0; i < rotateEles.size(); i++)
		{
			str += rotateEles.get(i).toString() +" \n";
		}

		for (int i = 0; i < pointEdgeEles.size(); i++)
		{
			str += pointEdgeEles.get(i).toString() +" \n";
		}



		System.out.println(str);
	}



	public Vertex copy()
	{
		Vertex v = new Vertex(this.id, super.getX(), super.getY());
		for (Integer I: adjacentNodes)
			v.addAdjacentNode(I.intValue());
		for (AngleForceElement e:elements)
			v.addElement(new AngleForceElement(e.getCosine(),e.getOrthoVector(),e.getCrossEdges()));
		for (CrossForceElement ce:crossEles)
			v.addCrossEle(new CrossForceElement(ce.getCosine(),ce.getCrossVector(),ce.getCrossEdges()));
		for (RotateForceElement re:rotateEles)
			v.addRotateEle(new RotateForceElement(re.getSine(),re.getRotateVector(),re.getRotateEdges()));
		for (PointEdgeElement pe:pointEdgeEles)
			v.addPointEdgeEle(new PointEdgeElement(pe.getDist(),pe.getOrthoVector(),pe.getPointEdge()));




		return v;

	}

	//


	public static void main (String[] args) {
		Vertex v1 = new Vertex(1);
		v1.randomMove(1);
		v1.addAdjacentNode(2);
		v1.addAdjacentNode(3);

		v1.printVertex();

	}

}

