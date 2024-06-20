
//************






//new, min crossing angle added







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

public class GraphTestFixedStructured {

		//fixed means that the repeated times are pre decided
		//means that algorithm stops when the repeated times are reached.


		//in this program, random graphs are pre generated and initial layout has been pre set.
		//therefore this is no need to save again: g0Str is commented out


	public static void main(String[] args)
	{



		for (int m=1; m<3; m++)
		{

			String strModel="";
			String strMedian="";

			if (m==1)
				strModel="watts";
			else if (m==2)
				strModel="eppstein";


			ArrayList<Graph> graphs = read(strModel+"Initial.txt");


			String strResults="";

            strResults += "size density cross angleM angleD minAng edgeM edgeD minVertx finVertx devVertx ";

			strResults += "cross1 cross2 angleM1 angleM2 angleD1 angleD2 minAng1 minAng2 ";
			strResults += "edgeM1 edgeM2 edgeD1 edgeD2 minVert1 minVert2 finVert1 finVert2 devVert1 devVert2 TF1 TR1 TC1 TF2" +"\n";

			String g0Str="===Initial graphs; " + strModel +" model;\n";
			String g1Str="===Resulting graphs based on new method, with rotate force; " + strModel +" model \n";
			String g2Str="===Resulting graphs based on classic method, without rotate force; " + strModel +" model \n";


			String strError  ="";

			int graphNumber = graphs.size();

			//int graphNumber = 100;

			int repeatTimes = 9000;

			//int graphNumber = 20;
			//int repeatTimes = 80;


			//ArrayList<Integer> size       = new ArrayList<Integer>();
			//ArrayList<Double>  density    = new ArrayList<Double>();

			ArrayList<Double> cross1       = new ArrayList<Double>();
			ArrayList<Double> cross2       = new ArrayList<Double>();
			ArrayList<Double> angleM1      = new ArrayList<Double>();
			ArrayList<Double> angleM2      = new ArrayList<Double>();
			ArrayList<Double> angleD1      = new ArrayList<Double>();
			ArrayList<Double> angleD2      = new ArrayList<Double>();

			ArrayList<Double> minAng1      = new ArrayList<Double>();
			ArrayList<Double> minAng2      = new ArrayList<Double>();

			ArrayList<Double> edgeM1       = new ArrayList<Double>();
			ArrayList<Double> edgeM2       = new ArrayList<Double>();
			ArrayList<Double> edgeD1       = new ArrayList<Double>();
			ArrayList<Double> edgeD2       = new ArrayList<Double>();
			ArrayList<Double> minVert1     = new ArrayList<Double>();
			ArrayList<Double> minVert2     = new ArrayList<Double>();
			ArrayList<Double> finVert1     = new ArrayList<Double>();
			ArrayList<Double> finVert2     = new ArrayList<Double>();
			ArrayList<Double> devVert1     = new ArrayList<Double>();
			ArrayList<Double> devVert2 	   = new ArrayList<Double>();

			ArrayList<Double> tf1 	   = new ArrayList<Double>();
			ArrayList<Double> tr1 	   = new ArrayList<Double>();
			ArrayList<Double> tc1 	   = new ArrayList<Double>();
			ArrayList<Double> tf2 	   = new ArrayList<Double>();



			for (int i = 0; i <graphNumber; i++)
			{


				Graph g0 = graphs.get(i);


				Graph g1 = g0.copy();
				Graph g2 = g0.copy();

				//new method with consine force
				g1.COSINE_STRENGTH = 0.0;
				g1.ROTATE_STRENGTH = 1.0;     //acute or rotate: remember to set in crossAngle.java rotateYN


				//old classic method
				g2.COSINE_STRENGTH = 0.0;
				g2.ROTATE_STRENGTH = 0.0;

				CrossAngle.computeAngleForceElements(g0);
				CrossAngle.computeAngleForceElements(g1);
				CrossAngle.computeAngleForceElements(g2);


				boolean good=true;

				for (int j=0; j<repeatTimes;j++)
				{

					double a = g1.computeAllForces();
					if (a < 0.0)  //NaNs are found in force values
					{
						good=false;
						break;
					}

					double b = g2.computeAllForces();
					if (b < 0.0)  //NaNs are found in force values
					{
						good=false;
						break;
					}


					CrossAngle.computeAngleForceElements(g1);
					CrossAngle.computeAngleForceElements(g2);

					System.out.println("m=  " +m +"   i=  "+i+"------ j=   "+j);

				}


      			if (good==false)  //to create a new graph to replace the old one
				{
					strError = "model= " + strModel + " graph count =  "+(i+1)+"\n";
					continue;
				}
				else
				{
					//g0Str += "***Count= " + (i+1) + "**********\n" + g0.toString()+"\n";
					g1Str += "***Count= " + (i+1) + "**********\n" + g1.toString()+"\n";
					g2Str += "***Count= " + (i+1) + "**********\n" + g2.toString()+"\n";


					strResults +=  g0.size() +","+g0.getDensity() +",";
					strResults +=  g0.getNumOfCrosses() + ","+ g0.getDegreeMean() +","+ g0.getDegreeDeviation() +","+ g0.getMinCrossAngle() +","+g0.getEdgeMean()+","+g0.getEdgeDeviation()+",";
					strResults +=  g0.getMinVertexAngle()+","+g0.getFinVertexAngle()+","+g0.getDeviVertexAngle() +",";
					strResults +=  g1.getNumOfCrosses() + ","+ g2.getNumOfCrosses() + ","+ g1.getDegreeMean() + ","+ g2.getDegreeMean() +","+ g1.getDegreeDeviation() +","+ g2.getDegreeDeviation() +",";
					strResults +=  g1.getMinCrossAngle() +","+ g2.getMinCrossAngle() +","+ g1.getEdgeMean()+","+g2.getEdgeMean()+","+g1.getEdgeDeviation()+","+g2.getEdgeDeviation()+","+g1.getMinVertexAngle()+","+g2.getMinVertexAngle()+",";
					strResults +=  g1.getFinVertexAngle()+","+g2.getFinVertexAngle()+"," +g1.getDeviVertexAngle()+","+g2.getDeviVertexAngle()+",";
					strResults +=  g1.timeForce + "," + g1.timeRotate + "," + g1.timeCross +","+ g2.timeForce +"\n";

					  cross1.add(new Double(g1.getNumOfCrosses()));
					  cross2.add(new Double(g2.getNumOfCrosses()));
					 angleM1.add(new Double(g1.getDegreeMean()));
					 angleM2.add(new Double(g2.getDegreeMean()));
					 angleD1.add(new Double(g1.getDegreeDeviation()));
					 angleD2.add(new Double(g2.getDegreeDeviation()));

					 minAng1.add(new Double(g1.getMinCrossAngle()));
					 minAng2.add(new Double(g2.getMinCrossAngle()));

					 edgeM1.add(new Double(g1.getEdgeMean()));
					 edgeM2.add(new Double(g2.getEdgeMean()));
					 edgeD1.add(new Double(g1.getEdgeDeviation()));
					 edgeD2.add(new Double(g2.getEdgeDeviation()));
					 minVert1.add(new Double(g1.getMinVertexAngle()));
					 minVert2.add(new Double(g2.getMinVertexAngle()));
					 finVert1.add(new Double(g1.getFinVertexAngle()));
					 finVert2.add(new Double(g2.getFinVertexAngle()));
					 devVert1.add(new Double(g1.getDeviVertexAngle()));
					 devVert2.add(new Double(g2.getDeviVertexAngle()));
					 tf1.add(new Double(g1.timeForce));
					 tr1.add(new Double(g1.timeRotate));
					 tc1.add(new Double(g1.timeCross));
					 tf2.add(new Double(g2.timeForce));


				}

			}

			strMedian += 0 +","+0 +",";
			strMedian += 0 + ","+ 0 +","+ 0 +","+0+","+0+",";
			strMedian += 0+","+ 0 +","+0+","+0 +",";
			strMedian +=  median(cross1) + ","+ median(cross2) +","+ median(angleM1) + ","+ median(angleM2) +","+ median(angleD1) +","+median(angleD2)+",";
			strMedian +=  median(minAng1)+","+median(minAng2)+","+median(edgeM1)+","+median(edgeM2)+","+median(edgeD1)+","+median(edgeD2)+","+median(minVert1)+","+median(minVert2)+",";
			strMedian +=  median(finVert1)+","+median(finVert2)+","+median(devVert1)+","+median(devVert2)+",";
			strMedian +=  median(tf1)+","+median(tr1)+","+median(tc1)+","+median(tf2)+"\n";

			strResults += "\n\n" + strMedian;


	         //append to a file
			 try {
					BufferedWriter out;
					out = new BufferedWriter(new FileWriter(strModel+"Results.txt", true));
					out.write(strResults);
					out.close();
					out = new BufferedWriter(new FileWriter(strModel+"New.txt", true));
					out.write(g1Str);
					out.close();
					out = new BufferedWriter(new FileWriter(strModel+"Old.txt", true));
					out.write(g2Str);
					out.close();
					//out = new BufferedWriter(new FileWriter(strModel+"Initial.txt", true));
					//out.write(g0Str);
					//out.close();

					if (strError.trim().length() != 0)
					{
						out = new BufferedWriter(new FileWriter(strModel + "Error.txt", true));
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
