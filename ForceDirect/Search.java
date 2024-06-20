// 1) read graphs
// 2) determine start and end nodes and sp
// 3) determine max width and height for each pair of new and old drawings for each graph

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

import java.lang.*;

import java.io.*;

import java.net.*;

import java.util.ArrayList;

public class Search {

    private static int START;
    private static int END;

    private static int pathLength;

    private static int sp1=0;
    private static int sp2=0;

    static		boolean found = false;

    private static Path path ;
    private static ArrayList<Path>    paths;

    private static ArrayList<Graph> graphs;

    public static void main(String[] args) {



		//the text includes 200 new and 200 old graphs
		//1-200 are new, 201-400 are old

		//if we use er graphs, it is not always we can find 3-5 sp length, but is seems that it is easy for rome graphs
		//we use rome graphs and size from 10-50 first to test,
		//then use size from 30-50
		//then use er graphs
		//we donot clean those node-edge overlap graphs, but we might do this later

		int numberOfGraphs = 200;   //this should be the sum of new and old drawings

		graphs    = read("rome0New.txt");


		//for computing paths and scale, we only need first half of graphs (= i < numberOfGraphs/2)

		paths   = new ArrayList();

		for (int i = 0; i < numberOfGraphs/2; i++)
		{


			    Graph graph = new Graph();

				graph = graphs.get(i);
				//graph.printGraph();


				//for each graph, the strting node is different
				int k = (int)(0 + ((graph.size()-1)-0+1)*Math.random());

				path    = new Path();



				//int k=11;

				for (int m = 0; m < graph.size(); m++)
				{


					START=m+k;
					if (START > graph.size()-1)
						START = START - graph.size();

					for (int n = 0; n < graph.size(); n++)
					{
						if (n == START)
							continue;

						END=n;

						//System.out.print("start= "+START+"  end= "+END+"\n");
						//System.out.print("m= "+m+"  n= "+n+"\n");


						//**********
						//**********
						//initialize variable for each run
						//**********
						sp1=0;
						sp2=0;
						pathLength=5;
						found = false;
						ArrayList<Integer> visited = new ArrayList();
						//***********************************************

						visited.add(new Integer(START));
						//new Search().breadthFirst(graph, visited);

						//System.out.print(" size=  "+ visited.size()+ " "+ visited.get(0).intValue()+"\n");
						breadthFirst(graph, visited);



						//System.out.print("sp1= "+sp1+"  sp2= "+sp2+"\n");

						if (sp1 != sp2 && sp2>2)
						{
							found = true;
						    break;
						}
					}

					if (found)  break;
				}

				if (path.getLength()<3)
				{
					System.out.print("i=  " + i + "  k=  " + k +"\n");
				}

				computeScale(graph, graphs.get(i+numberOfGraphs/2), path);

				paths.add(path);
	    }

	    for (int i=0; i<paths.size(); i++)
	    {
			System.out.print("i=  "+i +"  ");

	        paths.get(i).printString();
		}





    }

    //screen: 1024x768
    //box: 700x600
    //topleft point of the box (100,100)
    //topright: (800,100)
    //bottomleft: (100,700)
    //bottomright: (800,700)
    //centrePoint of the box: cp = (450,400)

    private static double boxWidth = 700;
    private static double boxHeight = 600;
    private static void mapGraph(Graph g, Path p, double bw, double bh, Point2D.Double cp)
    {
		 double boxScale;
		 if (p.getHeight()/bh <= p.getWidth()/bw )
		 {
			 boxScale = bw/p.getWidth();
		 }
		 else
		 {
			 boxScale = bh/p.getHeight();
		 }

		 Point2D.Double gcp = g.getCentrePoint();

		 for (int i=0; i<g.size(); i++)
		 {
			 //scale first, moce to the centre of the graph, then move to the centre of box
			 //note that the sign of y coordinats of vertices are changed without flipping the graph
			 double x = g.getVertex(i).getX()*boxScale-gcp.getX()*boxScale+cp.getX();
			 double y = -g.getVertex(i).getY()*boxScale+gcp.getY()*boxScale+cp.getY();
			 g.getVertex(i).setLocation(x,y);
		 }

	}


    private static void computeScale(Graph g1, Graph g2, Path p)
    {
		ArrayList<Vertex>  vs1 = g1.getVertices();
		ArrayList<Vertex>  vs2 = g2.getVertices();
		double x, y, minX1=0,minY1=0,maxX1=0,maxY1=0,minX2=0,minY2=0,maxX2=0,maxY2=0.0;

		for (int i=0; i<g1.size(); i++)
		{
			if (i==0)
			{
				maxX1 = g1.getVertex(0).getX();
				minX1 = g1.getVertex(0).getX();
				maxY1 = g1.getVertex(0).getY();
				minY1 = g1.getVertex(0).getY();
			}
			else
			{
				x = g1.getVertex(i).getX();
				y = g1.getVertex(i).getY();
				if (x < minX1) minX1 = x;
				if (x > maxX1) maxX1 = x;
				if (y < minY1) minY1 = y;
				if (y > maxY1) maxY1 = y;

			}

		}

		for (int i=0; i<g2.size(); i++)
		{
			if (i==0)
			{
				maxX2 = g2.getVertex(0).getX();
				minX2 = g2.getVertex(0).getX();
				maxY2 = g2.getVertex(0).getY();
				minY2 = g2.getVertex(0).getY();
			}
			else
			{
				x = g2.getVertex(i).getX();
				y = g2.getVertex(i).getY();
				if (x < minX2) minX2 = x;
				if (x > maxX2) maxX2 = x;
				if (y < minY2) minY2 = y;
				if (y > maxY2) maxY2 = y;

			}

		}

		if (Math.abs(maxX1-minX1)>Math.abs(maxX2-minX2))
			p.setWidth(Math.abs(maxX1-minX1));
		else
			p.setWidth(Math.abs(maxX2-minX2));

		if (Math.abs(maxY1-minY1)>Math.abs(maxY2-minY2))
			p.setHeight(Math.abs(maxY1-minY1));
		else
			p.setHeight(Math.abs(maxY2-minY2));
	}

    private static void breadthFirst(Graph graph, ArrayList<Integer> visited)
    {



        ArrayList<Integer> nodes = graph.getVertex(visited.get(visited.size()-1).intValue()).getAdjacentNodes();


       /*for (int i=0; i<nodes.size(); i++)
        {
			System.out.print(nodes.get(i).intValue()+"  ");

		}
		System.out.print("\n");
		*/

        // examine adjacent nodes
        for (Integer node : nodes) {
			//System.out.print("size= "+ visited.size() + "  id=  " + node.intValue() +" visted= "+visited.get(visited.size()-1).intValue() +"\n");

            if (visited.contains(node)) {
                continue;
            }

            if ((visited.size()) > pathLength)
            {
            	break;
			}
            if (node.equals(new Integer(END))) {
				visited.add(node);
				if (visited.size()-1< pathLength)
                {
                	pathLength = visited.size()-1;

				}
                //printPath(visited);

				sp1=sp2;
				sp2=pathLength;


				path = new Path();
				path.addPath(visited);

				visited.remove(visited.size()-1);



				//System.out.print("length= "+(pathLength)+"\n");

                break;
            }
        }
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (Integer node : nodes)
        {

            if (visited.contains(node) || node.equals(new Integer(END)))
            {

                continue;
            }
            if ((visited.size()) > pathLength)
            	continue;
            visited.add(node);

            breadthFirst(graph, visited);
            visited.remove(visited.size()-1);
        }
    }

    private static void printPath(ArrayList<Integer> visited)
    {
        for (Integer node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
    }


	public static ArrayList<Graph> read(String fileName)
	{

		ArrayList<Graph> gs = new ArrayList<Graph>();

		try
		{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String str;


				Graph g = new Graph();

				boolean empty = true;

				while ((str = in.readLine()) != null)
				{
					str = str.trim();
					if (str.length() ==0 || str.indexOf("===") != -1)
					   continue;
					if (str.indexOf("Count") != -1)
					{
						if (!empty)
							gs.add(g);

						System.out.println("id===" + gs.size());

						empty = false;

						g = new Graph();

						continue;
					}


					Scanner sLine = new Scanner(str);

					int vId = sLine.nextInt();

					double x = sLine.nextDouble();
					double y = sLine.nextDouble();

					Vertex v = new Vertex(vId, x, y);


					while (sLine.hasNextInt())
					{
						v.addAdjacentNode(sLine.nextInt());
					}

					g.addVertex(v);

				}

				gs.add(g);

				in.close();
				System.out.println("================" + gs.size()+"======================\n");
		}
		catch (IOException e)
		{
			 e.printStackTrace();
		}


		return gs;
	}
}
