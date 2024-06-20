//package graphs;

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;

import java.io.*;

import java.io.IOException;
import java.util.Scanner;

import java.lang.*;

public class Graph {




	public double EPSILON = 0.001;
	public double SPRING_STRENGTH = 1.0;   //1.0;
	public double GRAVITY_STRENGTH = 1.0;

	public double SPRING_LENGTH = 1.0;

	public double POINTEDGE_STRENGTH = 0.0;   //to decide the force between a node and a edge

	public double COSINE_STRENGTH = 1.0;  //0.6   //force for large corssing angles
	public double ROTATE_STRENGTH = 1.0;  //0.6    //force for better vertex angular resolution

	public int    NUM_ITERATIONS = 5000;

	public double CROSS_STRENGTH = 0.0;  //0.6

	public long timeForce = 0;
	public long timeCross = 0;
    public long timeRotate= 0;

	public long timePointEdge = 0;

	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;


	//graph measurement

	private int numOfCrosses;
	private double sumOfDegrees;
	private double degreeMean;
	private double degreeDeviation;

	private double density;
	private double edgeMean;
	private double edgeDeviation;
	private double minCrossAngle;   //min edge crossing angle

	private double minVertexAngle;

	private double deviVertexAngle;   //average variance
	private double finVertexAngle;    //difference between the smalleat angle and te optimal angle at a vertex v
									  //, averaged overall vertices

	private double minPtEdgDist;





	public Graph()
	{
		vertices = new ArrayList<Vertex>();
		density      = 0.0;


		edges = new ArrayList<Edge>();

		numOfCrosses = 0;
		sumOfDegrees = 0.0;

		edgeMean     = 0.0;
		edgeDeviation= 0.0;

		minCrossAngle  =0.0;
		degreeMean     =0.0;
		degreeDeviation=0.0;

		minVertexAngle =0.0;
		deviVertexAngle=0.0;
		finVertexAngle =0.0;

		minPtEdgDist=0.0;


	}

	//generate a random sparse graph with n vertices using ER model
	public void erGraph(int n)
	{

		//create vertices
		for (int i=0; i<n; i++)
			vertices.add(new Vertex(i));

		//set locations for each vertex
	 	randMove();


	 	//create edges for sparse graphs based on Erdos-Renyi model
	 	int numEdges = (int)(n + (3*n-n+1)*Math.random());  // numEdges <=3n and >=n


	 	//int numEdges = (int)(2*n + (3*n-2*n+1)*Math.random());  // numEdges <=3n and >=2n

		int maxEdges = n*(n-1)/2;

		if (numEdges > maxEdges)
			numEdges = maxEdges;

		density = 2.0 * numEdges/n/(n-1);

		//create a graph by n and p
		double p = density;
		//System.out.println("prob p=" + p);
		numEdges=0;

		if (n>1)
		{
			for (int i=0; i<n-1; i++){
				for (int j=i+1; j<n; j++)
				{
					if(Math.random()<p)
					{
		    			//System.out.println("v1=  "+i +"  v2:  "+j);
						vertices.get(i).addAdjacentNode(vertices.get(j));
						vertices.get(j).addAdjacentNode(vertices.get(i));
						numEdges++;
					}
				}
			}
		}
		//System.out.println("numEdges=" + numEdges);
		density = 2.0 * numEdges/n/(n-1);   //using G(n,p) model, actual number of edges may be slightly different from the initial numEdges


    }

//generate a random graph with n vertices and e edges
	public void erGraphEdge(int n, int e)
	{

		//create vertices
		for (int i=0; i<n; i++)
			vertices.add(new Vertex(i));

		//set locations for each vertex
	 	randMove();


	 	int numEdges = e;


	 	//int numEdges = (int)(2*n + (3*n-2*n+1)*Math.random());  // numEdges <=3n and >=2n

		int maxEdges = n*(n-1)/2;

		if (numEdges > maxEdges)
			numEdges = maxEdges;

		density = 2.0 * numEdges/n/(n-1);

		//create a graph by n and p
		double p = density;
		//System.out.println("prob p=" + p);
		numEdges = 0;

		boolean exit=false;

		for (int i=0; i<n-1 && !exit; i++){
			for (int j=i+1; j<n && !exit; j++)
			{
				if(Math.random()<p)
				{
					//System.out.println("v1=  "+i +"  v2:  "+j);
					vertices.get(i).addAdjacentNode(vertices.get(j));
					vertices.get(j).addAdjacentNode(vertices.get(i));
					numEdges++;
				}

				if (numEdges == e)       //do want want graphs whose edge number is more than e. so exit now when I already have e edges
					exit=true;
			}
		}


		//System.out.println("numEdges=" + numEdges);
		//density = 2.0 * numEdges/n/(n-1);   //using G(n,p) model, actual number of edges may be slightly different from the initial numEdges

		//for visual complexy, I only care about number edges, and i do not want to write a separate method for getting edge#

		density = numEdges;  // at this stage it is possible numEdges < e;

    }


	//generate a random sparse graph with n vertices using Watts model
	//according to algorithm in wiki
	public void wattsGraph(int n)
	{

		//create vertices
		for (int i=0; i<n; i++)
			vertices.add(new Vertex(i));

		//set locations for each vertex
		randMove();


		//create edges for sparse graphs based on Erdos-Renyi model
	 	//int numEdges = (int)(n + (3*n-n+1)*Math.random());  // numEdges <=3n and >=n

	 	int numEdges = (int)(2*n + (3*n-2*n+1)*Math.random());  // numEdges <=3n and >=2n

		int maxEdges = n*(n-1)/2;

		if (numEdges > maxEdges)
			numEdges = maxEdges;

		density = 2.0 * numEdges/n/(n-1);


	    double meanDegree = 2 * (int) (numEdges*1.0/n);

	   // System.out.println("degree=  "+meanDegree);

	    //build lattice
	    numEdges=0;
	    for (int i=0; i<n; i++)
	    {
			for (int j=1; j<=(meanDegree/2); j++)
			{
				int k = i + j;
				if (k>n-1)
					k = k-(n-1)-1;
				vertices.get(i).addAdjacentNode(vertices.get(k));
				int l = i-j;
				if (l<0)
					l = (n-1)+l+1;
				vertices.get(i).addAdjacentNode(vertices.get(l));

				numEdges ++;

				//System.out.println("i= "+i+ " j= "+j +" l= "+l +"  k= "+k+"\n");
			}

		}

		//System.out.println("edge number= "+numEdges +"\n");

		density = 2.0 * numEdges/n/(n-1);
		double p = density;   //just a personal taste

		//rewire the lattice
		for (int i=0; i<n; i++)
		{
			ArrayList<Integer> adjNodes = vertices.get(i).getAdjacentNodes();

			if (adjNodes.size() == n-1)
				continue;

			ArrayList<Integer> adjNodesCopy = new ArrayList<Integer>();
			for (Integer I: adjNodes)
			{
				adjNodesCopy.add(new Integer(I.intValue()));
			}

			for (Integer I: adjNodesCopy)
			{
				int j = I.intValue();
				if (j > i && Math.random() < p)
				{
					int m;
					boolean notFound = true;

					while (notFound)
					{
						m =  (int)(0 + (n-1-0+1)*Math.random());

						if ((m == i) || (adjNodes.indexOf(new Integer(m)) != -1) )
							continue;


						//System.out.println("i= "+ i +" remove "+ j +" add "+ m +"\n");

						adjNodes.remove(I);
						vertices.get(j).getAdjacentNodes().remove(new Integer(i));

						adjNodes.add(new Integer(m));
						vertices.get(m).addAdjacentNode(i);
						notFound = false;

					}

				}

				if (adjNodes.size() == n-1)
					break;
			}


		}


    }


	//generate a random sparse graph with n vertices using Eppstein power law model

	public void eppsteinGraph(int n)
	{


		//create vertices
		for (int i=0; i<n; i++)
			vertices.add(new Vertex(i));

		//set locations for each vertex
	 	randMove();

		//************************

		//Build initial graph randomly
		//************************
	 	//create edges for sparse graphs based on Erdos-Renyi model
	 	int numEdges = (int)(n + (3*n-n+1)*Math.random());  // numEdges <=3n and >=n


	 	//int numEdges = (int)(2*n + (3*n-2*n+1)*Math.random());  // numEdges <=3n and >=2n

		int maxEdges = n*(n-1)/2;

		if (numEdges > maxEdges)
			numEdges = maxEdges;

		density = 2.0 * numEdges/n/(n-1);

		//create a graph by n and p
		double p = density;
		//System.out.println("prob p=" + p);
		numEdges=0;

		if (n>1)
		{
			for (int i=0; i<n-1; i++){
				for (int j=i+1; j<n; j++)
				{
					if(Math.random()<p)
					{
		    			//System.out.println("v1=  "+i +"  v2:  "+j);
						vertices.get(i).addAdjacentNode(vertices.get(j));
						vertices.get(j).addAdjacentNode(vertices.get(i));
						numEdges++;
					}
				}
			}
		}
		//System.out.println("numEdges=" + numEdges);
		density = 2.0 * numEdges/n/(n-1);   //using G(n,p) model, actual number of edges may be slightly different from the initial numEdges

		double maxDegree = 0.0;

		//start the process of inserting and deleting edges, r=200
		for (int i=0; i<100; i++)
		{
			maxDegree = 0.0;
			for (Vertex v: vertices)
			{
				maxDegree = Math.max(v.getAdjacentNodes().size(),maxDegree);
			}

			int indexV = 0;
			int degreeV = 0;
			Vertex v = null;
			ArrayList<Integer> eV = new ArrayList<Integer>();
			do {
				indexV = (int)(0+(n-1-0+1)*Math.random());
				v = getVertex(indexV);
				eV = v.getAdjacentNodes();
				degreeV = eV.size();
			} while (degreeV == 0);

			Integer I = eV.get((int)(0+(degreeV-1-0+1)*Math.random()));

			int indexX = (int)(0+(n-1-0+1)*Math.random());
      		Vertex x = getVertex(indexX);

      		Vertex y = null;
      		int indexY = 0;
      		int degreeY = 0;
      		ArrayList<Integer> eY = new ArrayList<Integer>();
      		do {
				indexY = (int)(0+(n-1-0+1)*Math.random());
				y = getVertex(indexY);
				eY = y.getAdjacentNodes();
				degreeY = eY.size();
			} while (Math.random() > ((degreeY+1)/maxDegree));


			if ((eY.indexOf(new Integer(indexX)) == -1 ) && indexX != indexY)
			{
				eV.remove(I);
				getVertex(I.intValue()).getAdjacentNodes().remove(new Integer(indexV));
				x.addAdjacentNode(indexY);
				y.addAdjacentNode(indexX);
			}


		}

    }




	// initiate a graph from a text file
	public Graph(String fileName)
	{
		numOfCrosses = 0;
		sumOfDegrees = 0.0;
		density      = 0.0;
		edgeMean     = 0.0;
		edgeDeviation= 0.0;

		minCrossAngle  =0.0;
		degreeMean     =0.0;
		degreeDeviation=0.0;

		minVertexAngle =0.0;
		deviVertexAngle=0.0;
		finVertexAngle =0.0;

		minPtEdgDist=0.0;


		edges = new ArrayList<Edge>();

		try{
			vertices = new ArrayList<Vertex>();
			File f = new File(fileName);
			Scanner sFile = new Scanner(f);

			while (sFile.hasNextLine())
			{

				String vertexLine = sFile.nextLine();

				//if (vertexLine.trim().length() ==0)
					//continue;

				if (vertexLine.trim().length() ==0 || vertexLine.trim().indexOf("===") != -1 ||vertexLine.trim().indexOf("***") != -1)
					   continue;


				Scanner sLine = new Scanner(vertexLine);

				int id = sLine.nextInt();

				double x = sLine.nextDouble();
				double y = sLine.nextDouble();

 				Vertex v = new Vertex(id, x, y);


				while (sLine.hasNextInt())
				{
					v.addAdjacentNode(sLine.nextInt());
				}

				vertices.add(v);
			}

		}
		catch (IOException e)
		{
			System.out.println("Errors: " + e);
			System.exit(1);
		}
	}

	//generate a random graph of denisty d with n vertices
	public Graph(int n, double d)
	{

		numOfCrosses = 0;
		sumOfDegrees = 0.0;
		density      = 0.0;
		edgeMean     = 0.0;
		edgeDeviation= 0.0;

		minCrossAngle  =0.0;
		degreeMean     =0.0;
		degreeDeviation=0.0;

		minVertexAngle =0.0;
		deviVertexAngle=0.0;
		finVertexAngle =0.0;

		minPtEdgDist=0.0;



		edges = new ArrayList<Edge>();

		vertices = new ArrayList<Vertex>();



		//if d==0.0, it is assumed that the edge number will be randomly created

		if (d<=0.0 ||d>1.0)
			d=0.0;


		for (int i=0; i<n; i++)
			vertices.add(new Vertex(i));

		//set locations for each vertex
		randMove();


		//*****************
		//determine edge number
		int maxEdgeNumber, minEdgeNumber, edgeNumber;

		minEdgeNumber = n-1;    //a least number of edges needed to make the graph connected


		//if d==0.0, it is assumed that the edge number will be randomly created
		if (d==0)
		{
			maxEdgeNumber = n * (n-1)/2;
			edgeNumber = (int)(minEdgeNumber + Math.random() * (maxEdgeNumber - minEdgeNumber +1));  // a random number between max and min included
		}
		else
		{
			edgeNumber = (int)(d * n * (n-1)/2.0);
			if (edgeNumber<minEdgeNumber)
				edgeNumber = minEdgeNumber;
		}

		density = 2.0 * edgeNumber/n/(n-1);

		System.out.println("min= "+minEdgeNumber+"  edgeNO:  "+edgeNumber);

		//connect all vertices together with one path
		//minEdgeNumber edges are needed to do so
		for (int j=0; j<n-1; j++)
		{
			vertices.get(j).addAdjacentNode(vertices.get(j+1));
			vertices.get(j+1).addAdjacentNode(vertices.get(j));
		}

		//connect vertices randomly
		int v1=0, v2=0;
		ArrayList<String> strEdges = new ArrayList<String>();


		//store vertex ID pairs that are possible to be used for edges
		for (int i=2; i<n; i++)
			for (int j=0; j<i-1; j++)
				strEdges.add(""+i+"-"+j);


		//randomly pick vertex paris
		for (int i =0; i < (edgeNumber-minEdgeNumber); i++)
		{
			int index = (int) (0 + (strEdges.size()-1-0+1) * Math.random());
			String str = strEdges.get(index);

			strEdges.remove(index);

			v1 = Integer.valueOf(str.substring(0,str.indexOf('-'))).intValue();
			v2 = Integer.valueOf(str.substring(str.indexOf('-')+1)).intValue();


		    System.out.println("v1=  "+v1 +"  v2:  "+v2);

		    vertices.get(v1).addAdjacentNode(vertices.get(v2));
			vertices.get(v2).addAdjacentNode(vertices.get(v1));
		}

	}

	public void randMove()
	{
		for (Vertex u: vertices) u.randomMove(1.0);
	}


	public void setParameters(double p1,double p2,double p3,double p4,double p5,double p6,int p7, double p8)
	{
		EPSILON = p1;

		SPRING_STRENGTH = p2;
		GRAVITY_STRENGTH = p3;


		SPRING_LENGTH = p4;
		POINTEDGE_STRENGTH = p8;

		COSINE_STRENGTH = p5;
		ROTATE_STRENGTH = p6;

		NUM_ITERATIONS = p7;

	}



	//*****************
	//set  and get graph parameters
	//*****************

	public void setDeviVertexAngle(double d)
	{
		deviVertexAngle=d;
	}

	public void setFinVertexAngle(double f)
	{
		finVertexAngle =f;
	}


	public void setMinVertexAngle(double m)
	{
		minVertexAngle =m;
	}


	public double getDeviVertexAngle()
	{
		return deviVertexAngle;
	}

	public double getFinVertexAngle()
	{
		return finVertexAngle;
	}


	public double getMinVertexAngle()
	{
		return minVertexAngle;
	}



	public void setNumOfCrosses(int n)
	{
		numOfCrosses = n;
	}

	public void setSumOfDegrees(double c)
	{
		sumOfDegrees = c;
	}

	public void setMinCrossAngle(double m)
	{
		minCrossAngle = m;
	}

	public double getMinCrossAngle()
	{
		return minCrossAngle;
	}

	public void setMinPtEdgDist(double p)
	{
		minPtEdgDist = p;
	}

	public double getMinPtEdgDist()
	{
		return minPtEdgDist;
	}



	public int getNumOfCrosses()
	{
		return numOfCrosses;
	}

	public double getSumOfDegrees()
	{
		return sumOfDegrees;
	}

	public double getDegreeMean()
	{
		return degreeMean;
	}

	public double getDegreeDeviation()
	{
		return degreeDeviation;
	}


	public void setDegreeMean(double dm)
	{
		degreeMean = dm;
	}

	public void setDegreeDeviation(double dd)
	{
		degreeDeviation = dd;
	}




	public void setDensity(double d)
	{
		density = d;
	}

	public double getDensity()
	{
		return density;
	}


	public double getEdgeMean()
	{
		return edgeMean;
	}

	public double getEdgeDeviation()
	{
		return edgeDeviation;
	}



	//********************


	public void addVertex(Vertex v)
	{
		vertices.add(v);
	}



	public ArrayList<Vertex> getVertices()
	{
		return vertices;
	}

	public ArrayList<Edge> getEdges()
	{
		return edges;
	}


	// number of vertices
	public int size()
	{
		return vertices.size();
	}


	//get a vertex according to id
	public Vertex getVertex(int id)
	{
		return vertices.get(id);

	}

	public Point2D.Double getCentrePoint()
	{
		double maxX=0.0, maxY=0.0, minX=0.0, minY=0.0;

		for (int i=0; i<vertices.size(); i++)
		{
			double x = vertices.get(i).getX();
			double y = vertices.get(i).getY();
			if (i==0)
			{
				maxX=x;
				maxY=y;
				minX=x;
				minY=y;
			}
			else
			{
				if (x < minX) minX = x;
				if (x > maxX) maxX = x;
				if (y < minY) minY = y;
				if (y > maxY) maxY = y;

			}
		}

		return new Point2D.Double((minX+maxX)/2.0,(minY+maxY)/2.0);
	}




	//*****************************
	//Forces
	//*******************************


	String str="";


	//Only adjacent nodes are attractive to each other
	public Point2D.Double springForce(Vertex u)
	{
		double fx = 0;
		double fy = 0;

		for (Integer I: u.getAdjacentNodes())
		{
			int id = I.intValue();

			double d = u.distance(getVertex(id));

			double magnitude = SPRING_STRENGTH*(d - SPRING_LENGTH);


			if (d<0.00001)
				d = 0.00001;

			fx += magnitude*(getVertex(id).getX() - u.getX())/d;
			fy += magnitude*(getVertex(id).getY() - u.getY())/d;

			//System.out.println("sfx  =" +  fx +"   sfy= " +fy +" \n" );

		}
		return new Point2D.Double(fx,fy);
	}


	//all nodes repel each other
	public Point2D.Double gravityForce(Vertex u)
	{
		double fx = 0;
		double fy = 0;
		for (Vertex v: vertices)
	    {
			if (!(u.equals(v)))
			{
				double d = u.distance(v);

				double magnitude = GRAVITY_STRENGTH/(d*d);

				if (d<0.00001)
					d = 0.00001;

				fx += magnitude*(u.getX()-v.getX())/d;
				fy += magnitude*(u.getY()-v.getY())/d;
                //System.out.println("gfx  =" +  fx +"   gfy= " +fy +" \n" );
			}
		}
		return new Point2D.Double(fx,fy);
	}


	public Point2D.Double pointEdgeForce(Vertex u)
	{
		double fx       = 0.0;
		double fy       = 0.0;
		double magnitude;

		ArrayList<PointEdgeElement> eles = u.getPointEdgeEles();

		for (int i = 0; i < eles.size(); i++)
		{
				PointEdgeElement ele = eles.get(i);


				//magnitude = ele.getDist();

				double d = ele.getDist();

				if (d<0.00001)
					d = 0.00001;


				magnitude = POINTEDGE_STRENGTH /d;


				fx += magnitude * ele.getOrthoVector().getX();
				fy += magnitude * ele.getOrthoVector().getY();

				//System.out.println("cfx  =" +  fx +"   cfy= " +fy +" \n" );
		}
		return new Point2D.Double(fx,fy);
	}



	//compute crossing angle force
	public Point2D.Double angleForce(Vertex u)
	{
		double fx       = 0.0;
		double fy       = 0.0;
		double magnitude;

		ArrayList<AngleForceElement> eles = u.getElements();

		for (int i = 0; i < eles.size(); i++)
		{
				AngleForceElement ele = eles.get(i);
				magnitude = COSINE_STRENGTH * ele.getCosine();

				//System.out.println("magnitude  =" +  magnitude +" \n" );

				fx += magnitude * ele.getOrthoVector().getX();
				fy += magnitude * ele.getOrthoVector().getY();

				//System.out.println("afx  =" +  ele.getOrthoVector().getX() +"   afy= " +ele.getOrthoVector().getY() +" \n" );
		}
		return new Point2D.Double(fx,fy);
	}

	//compute cross force
	public Point2D.Double crossForce(Vertex u)
	{
		double fx       = 0.0;
		double fy       = 0.0;
		double magnitude;

		ArrayList<CrossForceElement> eles = u.getCrossEles();

		for (int i = 0; i < eles.size(); i++)
		{
				CrossForceElement ele = eles.get(i);
				magnitude = CROSS_STRENGTH * ele.getCosine();

				fx += magnitude * ele.getCrossVector().getX();
				fy += magnitude * ele.getCrossVector().getY();

				//System.out.println("cfx  =" +  fx +"   cfy= " +fy +" \n" );
		}
		return new Point2D.Double(fx,fy);
	}

	//compute rotate force
	public Point2D.Double rotateForce(Vertex u)
	{
		double fx       = 0.0;
		double fy       = 0.0;
		double magnitude;

		ArrayList<RotateForceElement> eles = u.getRotateEles();

		for (int i = 0; i < eles.size(); i++)
		{
				RotateForceElement ele = eles.get(i);
				magnitude = ROTATE_STRENGTH * ele.getSine();

				fx += magnitude * ele.getRotateVector().getX();
				fy += magnitude * ele.getRotateVector().getY();

				//System.out.println("cfx  =" +  fx +"   cfy= " +fy +" \n" );
		}
		return new Point2D.Double(fx,fy);
	}




	public double computeAllForces()
	{

		long tf = System.currentTimeMillis();


		double maxF = 0.0;

		for (Vertex u: vertices)
		{
			Point2D.Double sf = springForce(u);

			Point2D.Double gf = gravityForce(u);

			Point2D.Double af;
			Point2D.Double rf;

			Point2D.Double pef;

			if (POINTEDGE_STRENGTH != 0.0)
			{
				pef = pointEdgeForce(u);
			}
			else
			{
				pef = new Point2D.Double(0.0,0.0);
			}

			if (COSINE_STRENGTH != 0.0)
			{
				af = angleForce(u);
			}
			else
			{
				af = new Point2D.Double(0.0,0.0);
			}

			if (ROTATE_STRENGTH != 0.0)
			{
				rf = rotateForce(u);
			}
			else
			{
				rf = new Point2D.Double(0.0,0.0);
			}

			Point2D.Double cf;

			if (CROSS_STRENGTH != 0.0)
			{

				cf = crossForce(u);
			}
			else
			{

				cf = new Point2D.Double(0.0,0.0);
			}



			//System.out.println("v: (" + u.getX() +"," + u.getY() +") cfx= " +  cf.getX() +" cfy=" +cf.getY() +" \n" );


			double fx = sf.getX() + gf.getX() + af.getX() +cf.getX()+rf.getX()+pef.getX();
			double fy = sf.getY() + gf.getY() + af.getY() +cf.getY()+rf.getY()+pef.getY();

			if (Double.isInfinite(fx) || Double.isNaN(fx)  || Double.isInfinite(fy) || Double.isNaN(fy))
			{
				fx = 0.0;
				fy = 0.0;

				return -1.0;
		    }

			u.setLocation(u.getX()+ EPSILON*fx,u.getY()+ EPSILON*fy);

			if (Math.abs(fx) > maxF)
				maxF = Math.abs(fx);
			if (Math.abs(fy) > maxF)
				maxF = Math.abs(fy);

			//System.out.println("v: (" + u.getX() +"," + u.getY() +") fx= " +  fx +" fy=" +fy +" \n" );
			//System.out.println("max: " + maxF + "  fx= " +  fx +" fy=" +fy +" \n" );

		}

		timeForce += System.currentTimeMillis()-tf;    //everytime you compute the forces, the time spent has to be added, that is why use +=
		return maxF;   //maxF is max force movement

	}


	//clear graph cosines elements

	public void clearGraphDynamics()
	{
		//clear angle elements stored in vertices
		for (int i = 0; i < vertices.size(); i++)
		{
			this.vertices.get(i).clearElements();
			this.vertices.get(i).clearCrossEles();
			this.vertices.get(i).clearRotateEles();
			this.vertices.get(i).clearPointEdgeEles();

		}

		//clear edges to stroe new locations of vertices
		edges.clear();

		//set graph measurements to zero
		numOfCrosses = 0;

		edgeMean     = 0.0;
		edgeDeviation= 0.0;
		minCrossAngle  =0.0;

		minVertexAngle =0.0;
		deviVertexAngle=0.0;
		finVertexAngle =0.0;

		sumOfDegrees = 0.0;
		degreeMean     =0.0;
		degreeDeviation=0.0;

		minPtEdgDist=0.0;
	}


	public void buildEdges()
	{
		int nodeId   = -1, adjId = -1;


		//go through exch vertex to build Segments

		for (int i = 0; i < vertices.size(); i++)
		{
			nodeId = vertices.get(i).getId();

			ArrayList<Integer> adjNodes = vertices.get(i).getAdjacentNodes();

			//for each vertex, build segments based on adjacent nodes
			for (int j = 0; j < adjNodes.size(); j++)
			{
				adjId = adjNodes.get(j).intValue();

				Edge edge = new Edge(vertices.get(i), getVertex(adjId));


				//test whether an edge with opposite start and end nodes has already been added in the list
				//for undirected graphs, the two edges with opposite start and end nodes are the same
				boolean contains = false;
				for (Edge e : edges)
				{
					//it is important to compare based on id, rather than coordinates since two differernt nodes may have the same coordinates

					if (e.getId1() == edge.getId2() && e.getId2() == edge.getId1())
					{
						contains =true;
						break;
					}
				}

				if (contains == false)
					edges.add(edge);
			}
		}

		//density = 2.0*edges.size()/vertices.size()/(vertices.size()-1);

		//***********************************

		density = edges.size();    //for my visal complexity experiment. I only care number of edges

		//***********************************


		//compute mean edge length and edge deviation
		double[] edgeLength = new double[edges.size()];

		edgeMean = 0.0;

		for (int i=0; i<edges.size(); i++)
		{
			Edge e = edges.get(i);
			edgeLength[i] = e.getP1().distance(e.getP2());
			edgeMean += edgeLength[i];
		}
		edgeMean = edgeMean/edgeLength.length;

		double sqrSum=0.0;

		for (int i=0; i<edgeLength.length; i++)
		{
			sqrSum += (edgeLength[i]-edgeMean)*(edgeLength[i]-edgeMean);
		}

		edgeDeviation = Math.sqrt(sqrSum/edgeLength.length);

		//printEdges();

	}

	//print segments
	public void printEdges()
	{
		String str = "Edge List:\n";

		for (int i=0; i < edges.size(); i++)
		{

			str += i +  "   ( " + edges.get(i).getId1() +","+ edges.get(i).getId2() +") \n";
		}

		System.out.println (str);

	}


	public void printGraph()
	{

		for (int i = 0; i < vertices.size(); i++)
		{
			this.vertices.get(i).printVertex();
		}
	}


	//copy initial positions of vertices
	public Graph copy()
	{

		Graph g = new Graph();
		for (Vertex v:vertices)
			g.addVertex(v.copy());

		g.setParameters(EPSILON,SPRING_STRENGTH,GRAVITY_STRENGTH,SPRING_LENGTH,COSINE_STRENGTH,ROTATE_STRENGTH,NUM_ITERATIONS,POINTEDGE_STRENGTH);

		//other variables will not be copied

		return g;

	}

	public String toString()
	{
		String strSave="";
		for (Vertex v: vertices)
		{

			strSave += v.getId() +"  " +v.getX()+" "+v.getY()+"  ";
			for (Integer I: v.getAdjacentNodes())
				strSave += " " + I.intValue();
			strSave += "\n";
		}

		return strSave;

	}


     public void write(String fileName)
	 {

        //append to a file
		 try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));   //true: means to append
				out.write("***Count= 1**********" +"\n");
				out.write(toString() +"\n");
				out.close();

		 } catch (IOException e) {
			 System.out.println(e);
		 }

		  /* write to a file
		  try {
			  FileOutputStream fo = new FileOutputStream("answer"+""+".txt");
			  PrintWriter pw =new PrintWriter(fo,true);
			  pw.println(strAnswer);
			  fo.close();

		  } catch (IOException e) {
			  System.out.println(e);
		  }*/
	 }


	 public boolean connected()
	 {
		 boolean connected = true;

		 ArrayList<Integer> K = new ArrayList<Integer>();
		 ArrayList<Integer> L = new ArrayList<Integer>();

		 K.add(new Integer(0));
		 L.add(new Integer(0));

		 int i = 0;

		 while (i<K.size())
		 {

				Integer M = K.get(i);
				//K.remove(i);

				ArrayList<Integer> adjNodes = getVertex(M.intValue()).getAdjacentNodes();

				for (Integer N: adjNodes)
				{

					int index = L.lastIndexOf(N);
					if (index == -1)
					{
						L.add(N);
						K.add(N);
					}
				}

				i++;

				//System.out.println("i am hehe! size=" + K.size());

		 }

		 if (L.size() < size())
		 	connected =false;


		 return connected;
	 }


	/*
	public double getMinVertexAngle()
	{
		double maxCosine = -2;


		for (Vertex v: vertices)
		{

			ArrayList<Integer> adjNodes = v.getAdjacentNodes();

			int numAdj = adjNodes.size();

			for (int i=0; i<numAdj-1; i++)
			{
				Integer I = adjNodes.get(i);

				for (int j=i+1; j<numAdj; j++)
				{
					Integer J = adjNodes.get(j);

					maxCosine = Math.max(maxCosine,CrossAngle.computeCosine(v,getVertex(I.intValue()),v,getVertex(J.intValue())));
				}
			}
		}

		//System.out.println("min angle=  " + maxCosine);

		minVertexAngle = Math.toDegrees(Math.acos(maxCosine));

		return minVertexAngle;
	}*/



	public static void main(String[] args) {
		Graph g = new Graph("./file1.txt");

		//g.randMove();

		//Graph g = new Graph();
		//g.eppsteinGraph(15);
		//g.write("aa.txt");

		System.out.println(g.connected() + "\n\n");
		//g.randConnectedGraph(7,0.0);
		//Graph g = new Graph();

		//g.printGraph();
		//g.layout();
		//g.printGraph();
		//g.randMove();
		//g.randConnect(2);

							//g.printGraph();

				for (int k=0; k<0; k++)
				{
					CrossAngle.computeAngleForceElements(g);
					//g.printGraph();


					System.out.println("\n\n");
					g.computeAllForces();

					//g.printGraph();

				}


				Path p = new Path();
				p.addNode(28);
				p.addNode(29);
				p.addNode(22);
				p.addNode(12);
				p.addNode(0);

				System.out.println("here1" + "\n\n");


				CrossAngle.computeAngleForceElements(g);

				//g.getPathCrossNo(p);
				//g.getPathCrossSum(p);
				//g.getPathCrossMin(p);

				System.out.println("here2" + "\n\n");

			    System.out.println(g.getPathCrossNo(p) + "\n\n");

				System.out.println("here3" + "\n\n");

				System.out.println(g.getPathAngleMean(p) + "\n\n");

				System.out.println("here4" + "\n\n");

                 System.out.println(g.getPathMinAng(p) + "\n\n");

                 System.out.println(g.getPathContinuity(p) + "\n\n");

                 System.out.println(g.getPathGeodesic(p) + "\n\n");


	}


	public int getPathCrossNo(Path p)    //number of crossings on the path
	{
		int node1, node2;

		int coNumber = 0;

		for (int i=0; i < p.getLength(); i++)
		{
			node1 = p.getNode(i);
			node2 = p.getNode(i+1);

			for (int j=0; j < edges.size(); j++)
			{
				if ((edges.get(j).getId1() == node1 && edges.get(j).getId2() == node2) || (edges.get(j).getId2() == node1 && edges.get(j).getId1() == node2))

			        coNumber += edges.get(j).getNo();
			}

		}


		return coNumber;
	}

	public double getPathAngleMean(Path p)  //mean of the crossing angles on the path
	{
		int node1, node2;

		double angSum = 0;
		int coNumber = 0;

		for (int i=0; i < p.getLength(); i++)
		{
			node1 = p.getNode(i);
			node2 = p.getNode(i+1);

			for (int j=0; j < edges.size(); j++)
			{
				if ((edges.get(j).getId1() == node1 && edges.get(j).getId2() == node2) || (edges.get(j).getId2() == node1 && edges.get(j).getId1() == node2))
				{

			        angSum += edges.get(j).getSum();
			        coNumber += edges.get(j).getNo();

				}
			}

		}

		if (coNumber ==0)
			return 90.000;
		else
			return ((int)(angSum/coNumber*1000+0.5))/1000.0;
	}

	public double getPathMinAng(Path p)    //min crossing angle on the path
	{
		int node1, node2;

		double minAng = 0;

		for (int i=0; i < p.getLength(); i++)
		{
			node1 = p.getNode(i);
			node2 = p.getNode(i+1);

			for (int j=0; j < edges.size(); j++)
			{
				if ((edges.get(j).getId1() == node1 && edges.get(j).getId2() == node2) || (edges.get(j).getId2() == node1 && edges.get(j).getId1() == node2))
                {
			        if (edges.get(j).getMin()>0 && minAng == 0)

			        	minAng = edges.get(j).getMin();

			        else if (edges.get(j).getMin()>0 && minAng > 0)

			        	minAng = Math.min(minAng, edges.get(j).getMin());
				}
			}

		}

		if (minAng ==0)   //no crossings on the path
			return 90.000;
		else
			return ((int)(minAng*1000+0.5))/1000.0;
	}


	public double getPathContinuity(Path p)    //measuring path continuity
	{

			double paConti = 0;

			int node1, node2, node3;


			Point2D.Double v1, v2, v3;

			//System.out.println("aa"  + paConti + "\n");

			//int k=0;  i decided not to measure as average. I measure sum, if result is not good, I can always get average by
			//deviding the sum by (the path length-1)


			for (int i=0; i < p.getLength()-1; i++)
			{
				node1 = p.getNode(i);
				node2 = p.getNode(i+1);
				node3 = p.getNode(i+2);

				v1 = getVertex (node1);
				v2 = getVertex (node2);
				v3 = getVertex (node3);

				paConti += Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v2, v2, v3))));

				//k++;

				//System.out.println("bb" +paConti + "\n");
				//System.out.println(Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v2, v2, v3)))) + "\n\n");


			}

			//return paConti/k;    //measurred as an average   k=pathlength-1
			return ((int)(paConti*1000+0.5))/1000.0;
	}

	public double getPathGeodesic1(Path p)    //measuring geodesic path tendency, note that this method simply adds the distance from each node to the
	                                           //edge formed by the start and end nodes
	{
		    int node1, node2, node3, end, start;

			end = p.getEnd();
			start = p.getStart();

			Vertex v1 = getVertex (start);
			Vertex v2 = getVertex (end);


            Edge line = new Edge(v1, v2);

            Double d = 0.0;

            Vertex v;

			for (int i=1; i < p.getLength(); i++)
			{
				node1 = p.getNode(i);

				v = getVertex (node1);

				d += line.ptSegDist(v);

			}

			return d;

	}


	public double getPathGeodesic(Path p)    //measuring geodesic path tendency
	{
			int node1, node2, node3, end, start;

			end = p.getEnd();


			Point2D.Double v = getVertex(end);

			Point2D.Double v1, v2;

			double paGeod = 0;

			for (int i=0; i < p.getLength()-1; i++)
			{
				node1 = p.getNode(i);
				node2 = p.getNode(i+1);

				v1 = getVertex (node1);
				v2 = getVertex (node2);

                paGeod += Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v, v1, v2))));

                //System.out.println("aa" +Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v, v1, v2)))) + "\n");

			}

			start = p.getStart();

			v = getVertex(start);

			for (int i=p.getLength(); i > 1; i--)
			{
				node1 = p.getNode(i);
				node2 = p.getNode(i-1);

				v1 = getVertex (node1);
				v2 = getVertex (node2);

                paGeod += Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v, v1, v2))));

                //System.out.println("bb" +Math.toDegrees(Math.acos(Math.abs(CrossAngle.computeCosine(v1, v, v1, v2)))) + "\n");

			}



			return ((int)(paGeod/2.0*1000+0.5))/1000.0;    //starting from a different end, the measure is different. Therefore, I decided to sum the both values and return the average
	}




}
