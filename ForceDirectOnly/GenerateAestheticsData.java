
//************

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

public class GenerateAestheticsData {



 public static void main(String[] args)
 {


            //String fileName = "rome30";
            //String fileName = "classicalDrawings";
            //String fileName = "graphData";
            String fileName = "Mapped";

   ArrayList<Graph> graphs = read(fileName + ".txt");


   String strResults="";

            strResults += fileName +"\n" + "id size density cross angleM angleD minAng edgeM edgeD minVertx finVertx devVertx minPtEdg"+"\n";


   int graphNumber = graphs.size();



   for (int i = 0; i <graphNumber; i++)
   {


    Graph g0 = graphs.get(i);


    CrossAngle.computeAngleForceElements(g0);


    strResults +=  (i+1) + " " + g0.size() +" "+g0.getDensity() +" ";
    strResults +=  g0.getNumOfCrosses() + " "+ g0.getDegreeMean() +" "+ g0.getDegreeDeviation() +" "+ g0.getMinCrossAngle() +" "+g0.getEdgeMean()+" "+g0.getEdgeDeviation()+" ";
    strResults +=  g0.getMinVertexAngle()+" "+g0.getFinVertexAngle()+" "+g0.getDeviVertexAngle()+" "+g0.getMinPtEdgDist() +"\n";

   }



          //append to a file
    try {
     BufferedWriter out;
     out = new BufferedWriter(new FileWriter(fileName +"_Aesthetic.txt", true));
     out.write(strResults);
     out.close();
    } catch (IOException e) {
     System.out.println(e);
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
