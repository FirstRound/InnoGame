package com.brackeen.scared.controllers;

import com.brackeen.scared.Tile;

import java.awt.geom.Point2D;
import java.util.*;

public class MovingController {

	private Point2D currentPosition = null;
	private Point2D destPosition    = null;
	private LinkedList<Point2D> way = new LinkedList<Point2D>();
	private boolean[][] map;
	private int fatal = 2;

	private final int STEP = 30;

	public MovingController(Tile[][] tiles) {
		makeBoolMap(tiles);
	}

	public void setCurrentPosition(Point2D position) {
		System.out.println(position.getX() + " : " + position.getY());
		currentPosition = position;
	}

	//abs(x), abs(y)
	public void calculateWay(Point2D dest) {
		destPosition = dest;
		way = getOptimalWay(destPosition);
	}

	public boolean hasNextMovement() {
		return !way.isEmpty();
	}

	public Point2D getNextMovement() {

		if (!way.isEmpty()) {
			Point2D res = way.getFirst();
			way.removeFirst();
			return res;
		} else {
			return null;
		}
	}

	public boolean canMoveTo(Point2D dest) {
		if ((int)dest.getX() < getMapWidth() && (int)dest.getY() < getMapHeight()) {
			return !(this.map[(int) dest.getX()][(int) dest.getY()]);
		}
		else {
			return false;
		}
	}

	public int getMapWidth() {
		return map.length;
	}

	public int getMapHeight() {
		return map[0].length;
	}

	private LinkedList<Point2D> getOptimalWay(Point2D to) {
		System.out.println("Here we go!");
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		//move to tile center
		points.add(new Point2D.Double(pointFloatRecounter(Math.abs(currentPosition.getX()+0.5)),
				pointFloatRecounter(Math.abs(currentPosition.getY()+0.5))));

		Queue<Point2D> queue = new LinkedList<Point2D>();
		queue.add(points.get(0));
		Point2D current =  null, prev = null;
		List<Point2D> tmp = new LinkedList<Point2D>();
		int prior[][] = new int[getMapWidth()*100][getMapHeight()*100];
		while(!queue.isEmpty()) {
			current = queue.peek();
			queue.remove();
			if ((current.getX()) == pointFloatRecounter(to.getX()) && (current.getY()) == pointFloatRecounter(to.getY())) {
				break;
			}
			tmp.add(new Point2D.Double((current.getX()+STEP), (current.getY()+STEP)));
			tmp.add(new Point2D.Double((current.getX()+STEP), (current.getY())));
			tmp.add(new Point2D.Double((current.getX()), (current.getY()+STEP)));
			tmp.add(new Point2D.Double((current.getX()-STEP), (current.getY()-STEP)));
			tmp.add(new Point2D.Double((current.getX()-STEP), (current.getY())));
			tmp.add(new Point2D.Double((current.getX()), (current.getY()-STEP)));
			tmp.add(new Point2D.Double((current.getX()+STEP), (current.getY()-STEP)));
			tmp.add(new Point2D.Double((current.getX()-STEP), (current.getY()+STEP)));

			for (Point2D point : tmp) {
				int a = (int)(point.getX()), b = (int)(point.getY());
				Point2D.Double p = new Point2D.Double(toNormalFloatRecounter((int)point.getX()), toNormalFloatRecounter((int)point.getY()));
				if (canMoveTo(p) && prior[a][b] == 0) {
					if (prev == null) {
						prior[(int)(point.getX())][(int)(point.getY())] = -1;
					}
					else {
						int la = toLinearArray((int)(current.getX()), (int)(current.getY()));
						prior[(int)(point.getX())][(int)(point.getY())] = la;
					}
					queue.add(point);
				}
			}

			prev = current;
			tmp.clear();
		}


		//8:11, 8:6, 3:9 problems
 		System.out.println(" #x: " + current.getX() + " y: " + current.getY());
		int a = 0;
		while (prior[(int)(current.getX())][(int)(current.getY())] != -1 && prior[(int)(current.getX())][(int)(current.getY())] != 0) {
			points.addFirst(new Point2D.Double(toNormalFloatRecounter((int)current.getX()), toNormalFloatRecounter((int)current.getY())));
			if ((int)(current.getX()) >= 1600 || (int)(current.getY()) >= 1600)
					a = 56;
			current = fromLinearArray(prior[(int)(current.getX())]
					[(int)(current.getY())]) ;
			a++;
			//current = new Point2D.Double(pointFloatRecounter(current.getX()),pointFloatRecounter(current.getY()));
		}

		Point2D po = new Point2D.Double(toNormalFloatRecounter((int)points.getLast().getX()), toNormalFloatRecounter((int)points.getLast().getY()));
		points.addFirst(po); //TODO: DIVIDE INTO 4 STEPS!!!

		points.removeLast();


		for (Point2D p : points) {
			System.out.print(" #x: " + p.getX() + " y: " + p.getY());
		}


		return points;

	}

	private int toLinearArray(int x, int y) {
		return x * (map.length*100) + y;
	}

	private int pointFloatRecounter(double coord) {
		return (int)(coord*100);
	}

	private double toNormalFloatRecounter(int coord) {
		return coord/100.0;
	}

	private Point2D fromLinearArray(int i) {
		//i = (int)toNormalFloatRecounter(i);
		return new Point2D.Double((i)/(map.length*100), (i)%(map.length*100));
	}

	private Point2D rMove() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		Random rand = new Random();
		Point2D point = new Point2D.Double();
		int dec = rand.nextInt(8);
		points.add(new Point2D.Double(currentPosition.getX()+1, currentPosition.getY()+1));
		points.add(new Point2D.Double(currentPosition.getX()-1, currentPosition.getY()-1));
		points.add(new Point2D.Double(currentPosition.getX()+1, currentPosition.getY()-1));
		points.add(new Point2D.Double(currentPosition.getX()-1, currentPosition.getY()+1));
		points.add(new Point2D.Double(currentPosition.getX()+1, currentPosition.getY()));
		points.add(new Point2D.Double(currentPosition.getX(), currentPosition.getY()+1));
		points.add(new Point2D.Double(currentPosition.getX()-1, currentPosition.getY()));
		points.add(new Point2D.Double(currentPosition.getX(), currentPosition.getY()-1));
		point = points.get(dec);
		while(!canMoveTo(point)) {
			point = points.get(dec);
		}
		return point;
	}


	private void makeBoolMap(Tile[][] tiles) {
		int width = tiles.length, height = tiles[0].length;
		map = new boolean[width][height];
		for (int i = 0 ; i < height; i++) {
			for (int j = 0 ; j < width; j++) {
				if (tiles[i][j].type == Tile.TYPE_WINDOW 
						|| tiles[i][j].type == Tile.TYPE_WALL) {
					map[i][j] = true;
				}
				else {
					map[i][j] = false;
				}
			}
		}

	}

}