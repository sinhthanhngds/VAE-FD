//package graphs;

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.awt.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import java.util.*;

import java.io.*;

import java.net.*;

public class ReformatPlanarGraphs {

	public static void main(String[] args) {
		//Graph g = new Graph("./file/planar/DH1.txt");

		//g.randMove();

		String str="ug";
		for (int i=1; i<21; i++)
		{
			try
			{
				BufferedWriter out = new BufferedWriter(new FileWriter("aa.txt", true));   //true: means to append
				out.write("***Count= "+(180+i)+"**********\n");  (180 is the count of graphs)
				out.close();
			}
			 catch (IOException e)
			 {
				 e.printStackTrace();
			 }


			Graph g = readBenchmark("ug"+i+".100");

			g.write("aa.txt");
			g.printGraph();
		}




	}

     public static Graph readBenchmark(String fileName)
     {
		 int n = 100;       //number of nodes: if noe is 100, then enter 100 here, if 80 enter 80 here
		 Graph g = new Graph();

		 try
		 {
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String str;




				for (int i=0; i<n; i++)
					g.addVertex(new Vertex(i));

				ArrayList<Edge> es = new ArrayList<Edge>();

				for (int i=0; i<300; i++)
					es.add(new Edge());

				int edgeNumber = 0;

                boolean empty = true;


                int vId=-1;
                int eId;

				while ((str = in.readLine()) != null)
				{
					str = str.trim();

					if (str.indexOf("</NODELIST>") != -1)
						break;

					if (str.length() ==0 || str.indexOf("<NODE>") != -1  || str.indexOf("</NODE>") != -1 || str.indexOf("<UNDISECTION>") != -1 || str.indexOf("<NODELIST>") != -1)
					   continue;

					if (str.indexOf("<EDGE>") == -1)
					{
						Scanner sLine = new Scanner(str);
						vId = sLine.nextInt();
					}
					else
					{
						System.out.println(str);
						String sss= str.substring(7);
						System.out.println(sss);
						Scanner sLine = new Scanner(sss);

						eId = sLine.nextInt();

						if (eId>edgeNumber)
							edgeNumber = eId;

						Edge e = es.get(eId);
						if (e.getId1() == -1)
							e.setId1(vId);
						else
							e.setId2(vId);

						es.set(eId,e);
					}
				}

				//es.removeRange(edgeNumber+1,199);
				ArrayList<Vertex> vs = g.getVertices();

				for (int i=0; i<edgeNumber; i++)
				{
					Edge e = es.get(i);
					ArrayList<Integer> adjV = vs.get(e.getId1()).getAdjacentNodes();

					if (adjV.contains(new Integer(e.getId2())))   //there are some duplicate edges in the original files
						continue;

					vs.get(e.getId1()).addAdjacentNode(e.getId2());
					vs.get(e.getId2()).addAdjacentNode(e.getId1());
				}

				in.close();
				//System.out.println("================" + gs.size()+"======================\n");
		 }
	     catch (IOException e)
	     {
			 e.printStackTrace();
         }

         return g;

	 }



 }
