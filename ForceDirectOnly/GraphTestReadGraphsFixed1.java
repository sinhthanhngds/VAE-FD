
//************






//new, min crossing angle added
// new, min point to edge distance added







//








//package graphs;

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

public class GraphTestReadGraphsFixed1 {

		//fixed means that the repeated times are pre decided
		//means that algorithm stops when the repeated times are reached.


		//in this program, random graphs are pre generated and initial layout has been pre set.
		//therefore this is no need to save again: g0Str is commented out


	public static void main(String[] args)
	{



		for (int m=1; m<2; m++)
		{

			/*
			String strModel="";
			String strMedian="";

			if (m==1)
				strModel="er";
			else if (m==2)
				strModel="watts";
			else if (m==3)
				strModel="eppstein";    */

			String fileName = "Rome9_ini";


			ArrayList<Graph> graphs = read(fileName +".txt");


			String strResults4 = "id size density cross angleM angleD minAng edgeM edgeD minVertx finVertx devVertx minPtEdg" +"\n";

			//strResults += "cross1 cross2 angleM1 angleM2 angleD1 angleD2 minAng1 minAng2 ";
			//strResults += "edgeM1 edgeM2 edgeD1 edgeD2 minVert1 minVert2 finVert1 finVert2 devVert1 devVert2 TF1 TR1 TC1 TF2" +"\n";

			String g0Str4="===based on " + fileName + "  8000 iterations" +"\n";

			//String g1Str="===Resulting graphs based on new method, with rotate force; " + strModel +" model \n";
			//String g2Str="===Resulting graphs based on classic method, without rotate force; " + strModel +" model \n";


			String strError  ="";

			int graphNumber = graphs.size();


			int repeatTimes = 8000;


			for (int i = 0; i <graphNumber; i++)
			{


				Graph g0 = graphs.get(i);


				//new method with consine force
				g0.COSINE_STRENGTH = 0.0;
				g0.ROTATE_STRENGTH = 0.0;     //acute or rotate: remember to set in crossAngle.java rotateYN

				CrossAngle.computeAngleForceElements(g0);


				boolean good=true;

				for (int j=1; j<=repeatTimes;j++)
				{

					double a = g0.computeAllForces();
					if (a < 0.0)  //NaNs are found in force values
					{
						good=false;
						break;
					}


					CrossAngle.computeAngleForceElements(g0);

					//System.out.println("m=  " +m +"   i=  "+i+"------ j=   "+j);

				}


      			if (good==false)  //to create a new graph to replace the old one or move to the enxt one
				{
					strError = "model= " + fileName + " graph count =  "+(i+1)+"\n";
					continue;
				}


				g0Str4 += "***Count= " + (i+1) + "**********\n" + g0.toString()+"\n";

				strResults4 +=  (i+1) +"," + g0.size() +","+g0.getDensity() +",";  //density : no of edges

				strResults4 +=  g0.getNumOfCrosses() + ","+ g0.getDegreeMean() +","+ g0.getDegreeDeviation() +","+ g0.getMinCrossAngle() +","+g0.getEdgeMean()+","+g0.getEdgeDeviation()+",";
				strResults4 +=  g0.getMinVertexAngle()+","+g0.getFinVertexAngle()+","+g0.getDeviVertexAngle() +","+g0.getMinPtEdgDist() +"\n";



			}

	         //append to a file
			 try {
					BufferedWriter out;
					out = new BufferedWriter(new FileWriter(fileName+"-8000.txt", true));
					out.write(g0Str4);
					out.close();

					out = new BufferedWriter(new FileWriter(fileName+"-8000_Aesthetic.txt", true));
					out.write(strResults4);
					out.close();


					if (strError.trim().length() != 0)
					{
						out = new BufferedWriter(new FileWriter(fileName + "_Error.txt", true));
						out.write(strError);
						out.close();
					}



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
