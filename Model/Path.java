//package graphs;

import java.awt.geom.Point2D;
import java.awt.geom.*;

import java.util.ArrayList;

import java.io.*;

import java.io.IOException;
import java.util.Scanner;

import java.lang.*;

public class Path {


	private ArrayList<Integer> nodes;
	double maxWidth, maxHeight;

	public Path()
	{
		nodes = new ArrayList<Integer>();
		maxWidth = 0.0;
		maxHeight = 0.0;
	}


	public void addNode(int i)
	{
		nodes.add(new Integer(i));
	}

	public void addNode(Integer In)
	{
		nodes.add(In);
	}


	public void addPath(ArrayList<Integer> p)
	{
		for (int i=0; i < p.size(); i++)
		{
			this.addNode(p.get(i));

		}

	}

	//note that width and height have nothing to do with path ieself. these are related to graph's width and height. we add these two varabile for the convenience of scale of old and new graphs.
	//pls see computeScale method of mapnew.java

	public void setWidth(double w)    //maxWidth is obtained by : compute width for each of the pair of old and new drawings, then chose the larger.
	{
		maxWidth = w;
	}

	public double getWidth()
	{
		return maxWidth;
	}

	public void setHeight(double h)
	{
		maxHeight = h;
	}


	public double getHeight()
	{
		return maxHeight;
	}


	public int getStart()
	{
		if (nodes.size()>0)
			return nodes.get(0).intValue();
		else
			return -1;
	}

	public int getEnd()
	{
		if (nodes.size()>1)
			return nodes.get(nodes.size()-1).intValue();
		else
			return -1;
	}

	public int getLength()
	{
		return nodes.size()-1;
	}

	public int getNode(int i)
	{
		if (i>=0 && i<nodes.size())  //i must be from 0,1,2,3 if the length is 3. nodes.size=4

		    return nodes.get(i).intValue();

		else

			return -99;

	}

	public String getPath()
	{
		String p = "";
		for (int i=0; i<nodes.size(); i++)
		{
			if (i==0)
			 	p += nodes.get(i).intValue();
			else
				p += "-" + nodes.get(i).intValue();
		}

		return p.trim();
	}

	public void printString()
	{
		for (int i=0; i<nodes.size(); i++)
		{
			System.out.print(nodes.get(i).intValue()+"  ");
		}
		System.out.print("\n");
	}

}
