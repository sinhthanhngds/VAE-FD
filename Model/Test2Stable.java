//package graphs;

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import java.lang.*;

import java.io.*;

import java.net.*;

public class Test2Stable {

	public static void main(String[] args)
	{

		int newNumber, oldNumber;



		for (int m=1; m<4; m++)
		{

			String strModel="";

			if (m==1)
				strModel="er";
			else if (m==2)
				strModel="watts";
			else if (m==3)
				strModel="eppstein";


			String strResults = "size density cross angleM angleD minAngle edgeM edgeD minVerte ";

			strResults += "repeat1 repeat2 cross1 cross2 angleM1 angleM2 angleD1 angleD2 minAngl1 minAngl2 ";
			strResults += "edgeM1 edgeM2 edgeD1 edgeD2 minVert1 minVert2 time11 time12 time1 time2 time22" +"\n";

			strResults += "===size:10-50, 3n>|V|>n; 1: new 2: old; " + strModel +" model. iteration stops separately when both reach maxForce<0.5 first" +"\n";

			String g0Str="===Initial graphs; " + strModel +" model;\n";
			String g1Str="===Resulting graphs based on new method, with cosine force; " + strModel +" model \n";
			String g2Str="===Resulting graphs based on classic method, without cosine force; " + strModel +" model \n";

			int graphNumber = 500;

			int repeatTimes = 80000;

			double maxForce = 0.5;


			for (int i = 0; i <graphNumber; i++)
			{
				newNumber=0;
				oldNumber=0;


				int N = (int)(10+Math.random() * (50-10+1));


				Graph g0;
				boolean connected = false;
				do {
					g0 = new Graph();

					if (m==1)
						g0.erGraph(N);
					else if (m==2)
						g0.wattsGraph(N);
					else if (m==3)
						g0.eppsteinGraph(N);

					connected = g0.connected();

				} while (!connected);

				Graph g1 = g0.copy();
				Graph g2 = g0.copy();

				//new method with consine force
				g1.COSINE_STRENGTH = 1.0;
				g1.CROSS_STRENGTH = 0.0;


				//old classic method
				g2.COSINE_STRENGTH = 0.0;
				g2.CROSS_STRENGTH = 0.0;

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

					CrossAngle.computeAngleForceElements(g1);

					//System.out.println("m=  " +m +"   i=  "+i+"------ j=   "+j+"  new  maxF= "+a);

					if (a>=0.0 && a<=maxForce)
					{
						newNumber = j+1;
						break;
					}
				}


				if (good==false)  //to create a new graph to replace the old one
				{
					i--;
					continue;
				}


				for (int j=0; j<repeatTimes;j++)
				{
					double b= g2.computeAllForces();
					if (b < 0.0)  //NaNs are found in force values
					{
						good=false;
						break;
					}

					CrossAngle.computeAngleForceElements(g2);

					//System.out.println("m=  " +m +"   i=  "+i+"------ j=   "+j+"  old  maxF= "+b);

					if (b>=0.0 && b<=maxForce)
					{
						oldNumber = j+1;
						break;
					}
				}


				if (good==false)  //to create a new graph to replace the old one
				{
					i--;
					continue;
				}


				if (newNumber == 0)
					newNumber = repeatTimes;
				if (oldNumber ==0)
					oldNumber = repeatTimes;


				g0Str += "***Count= " + (i+1) + "**********\n" + g0.toString()+"\n";
				g1Str += "***Count= " + (i+1) + " Iterations= " + newNumber +"**********\n" + g1.toString()+"\n";
				g2Str += "***Count= " + (i+1) + " Iterations= " + oldNumber +"**********\n" + g2.toString()+"\n";

				strResults +=  g0.size() +","+g0.getDensity() +",";
				strResults +=                   g0.getNumOfCrosses() + ","+ g0.getDegreeMean() +","+ g0.getDegreeDeviation() +","+g0.getMinCrossAngle()+","+g0.getEdgeMean()+","+g0.getEdgeDeviation()+","+g0.getMinVertexAngle()+",";
				strResults +=  newNumber + "," + oldNumber +"," + g1.getNumOfCrosses() + ","+ g2.getNumOfCrosses() + ","+ g1.getDegreeMean() + ","+ g2.getDegreeMean() +","+ g1.getDegreeDeviation() +","+ g2.getDegreeDeviation() +","+ g1.getMinCrossAngle() +","+ g2.getMinCrossAngle()+",";
				strResults +=  g1.getEdgeMean()+","+g2.getEdgeMean()+","+g1.getEdgeDeviation()+","+g2.getEdgeDeviation()+","+g1.getMinVertexAngle()+","+g2.getMinVertexAngle()+",";
				strResults +=  g1.timeForce +","+ g1.timeCross + "," + (g1.timeForce+g1.timeCross) +","+ g2.timeForce +","+ g2.timeCross +"\n";

			}



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
					out = new BufferedWriter(new FileWriter(strModel+"Initial.txt", true));
					out.write(g0Str);
					out.close();

			 } catch (IOException e) {
				 System.out.println(e);
			 }
		 }




	}





}
