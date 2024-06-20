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

public class RomeGraphs
{

	//fixed means that the repeated times are pre decided
	//means that algorithm stops when the repeated times are reached.

	public static void main(String[] args)
	{

		File myDir = new File("C:\\Documents and Settings\\!whua5569\\Desktop\\rome");

		if( myDir.exists() && myDir.isDirectory())
		{
			File[] files = myDir.listFiles();
			int j=0;
			System.out.println(files.length);

			int n=0;

			for(int i=0; i < files.length; i++)
			{


				Graph g = readBenchmark(files[i]);

				if (g.size()>50)
					continue;

				n++;

				if (n % 500.0 == 0.0)
				{
					j=n/500;
					System.out.println(n);
				}


				try
				{
					BufferedWriter out = new BufferedWriter(new FileWriter("rome"+j+".txt", true));   //true: means to append
					out.write("***Count= "+(n)+"**********\n");
					out.close();
				}
				catch (IOException e)
				{
					 e.printStackTrace();
				}

			    //System.out.println(files[i]);
			    //System.out.println(files.length);



				g.write("rome"+j+".txt");
				//g.printGraph();

			}

		}
	}

     public static Graph readBenchmark(File fileName)
     {

		 Graph g = new Graph();

		 try
		 {
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String str;

				ArrayList<Edge> es = new ArrayList<Edge>();

                int size=0;

				while ((str = in.readLine().trim()) != null)
				{

					if (str.indexOf("</graph>") != -1)  //the string is in the line
						break;

					if (str.indexOf("<node id=") != -1)
						size++;

					if (str.indexOf("<edge id=") != -1)
					{

						int i1=9+str.indexOf("source=\"n");
						int i2=str.indexOf("\" target=");

						//System.out.println(str);
						String sss1= str.substring(i1,i2);
						//System.out.println(sss1);

						int i3=9+str.indexOf("target=\"n");
                        int i4=str.indexOf("\"/>");


						String sss2= str.substring(i3,i4);
						//System.out.println(sss2);

						Edge e = new Edge();
						e.setId1(Integer.parseInt(sss1));
						e.setId2(Integer.parseInt(sss2));

						es.add(e);
					}
				}

				for (int i=0; i<size; i++)
					g.addVertex(new Vertex(i));


				//es.removeRange(edgeNumber+1,199);
				ArrayList<Vertex> vs = g.getVertices();

				for (int i=0; i<es.size(); i++)
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

         g.randMove();

         return g;

	 }




}