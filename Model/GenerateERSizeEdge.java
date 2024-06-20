//package graphs;


//sizeEdge means that this program is to generate graph with a predefined size and edge numbers

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

public class GenerateERSizeEdge {

	public static void main(String[] args)
	{

		for (int m=1; m<2; m++)
		{

			String strModel="";

			if (m==1)
				strModel="er";
			else if (m==2)
				strModel="watts";
			else if (m==3)
				strModel="eppstein";



			String g0Str="===Initial graphs; " + strModel +" model with size = 30 and edge#=40;\n";


			int graphNumber = 20;


			for (int i = 0; i <graphNumber; i++)
			{

				//int N = (int)(10+Math.random() * (100-10+1));
				int N = 30;
				int E = 40;

				Graph g0;

				boolean connected = false;
				do {
					g0 = new Graph();

					if (m==1)
						//********************
						//*******************
						// notice here it is erGraphEdge not erGraph
						g0.erGraphEdge(N,E);

					else if (m==2)
						g0.wattsGraph(N);
					else if (m==3)
						g0.eppsteinGraph(N);

					connected = g0.connected();

				} while (!connected || g0.getDensity() < E);




				g0Str += "***Count= " + (i+1) + "**********\n" + g0.toString()+"\n";
			}



			 try {
					BufferedWriter out;
					out = new BufferedWriter(new FileWriter(strModel+"Initial.txt", true));
					out.write(g0Str);
					out.close();

			 } catch (IOException e) {
				 System.out.println(e);
			 }


		 }

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
						{
							//************
							//************
							//set initial placements for each vertex
							g.randMove();
							//************

							gs.add(g);
						}

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

				//************
				//************
				//set initial placements for each vertex
				g.randMove();
				//************


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


	 public static double mean(ArrayList<Double> p)
	 {
		 double sum = 0; // sum of all the elements
		 for (int i=0; i<p.size(); i++)
		 {
			sum += p.get(i).doubleValue();
		 }
	     return sum / p.size();
	 }

	 public static double median(ArrayList<Double> p)
	 {
		double[] array = new double[p.size()];
		for (int i=0; i<p.size(); i++)
		{
			array[i] = p.get(i).doubleValue();
		}

	 	Arrays.sort(array); // sort the elements

	 	int result = array.length % 2;   //reminder

	 	double median = 0;

	 	// Definition: Median is the Middle number in an odd number of entries array
	 	// Median is the avg of the two number in the center of an even number array

	 	if (result == 0)
	 	{
	 		int rightNumber = array.length / 2;
	 		int leftNumber = rightNumber - 1;
	 		median = (array[rightNumber] + array[leftNumber]) / 2;
	 	}
	 	else // Odd number of items, choose the center one

	 		median = array[array.length / 2];

	 	return median;
	 }


}
