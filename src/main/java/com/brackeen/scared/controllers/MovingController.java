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
		System.out.println("Expected finish point: x:" + to.getX() + " y:" + to.getY());
		System.out.println("Here we go!");
		to.setLocation(pointFloatRecounter((to.getX())),
				pointFloatRecounter(to.getY()));
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		//move to tile center
		points.add(new Point2D.Double(pointFloatRecounter(Math.abs(currentPosition.getX()+0.5)),
				pointFloatRecounter(Math.abs(currentPosition.getY()+0.5))));

		Queue<Point2D> queue = new LinkedList<Point2D>();
		queue.add(points.get(0));
		Point2D current =  null, prev = null;
		List<Point2D> tmp = new LinkedList<Point2D>();
		int prior[][] = new int[getMapWidth()*101][getMapHeight()*101];
		while(!queue.isEmpty()) {
			current = queue.peek();
			queue.remove();
			if (isFinishPoint(to, current)) {
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
				Point2D.Double p = new Point2D.Double(toNormalFloatRecounter(a), toNormalFloatRecounter(b));
				if (canMoveTo(p) && prior[a][b] == 0) {
					if (prev == null) {
						prior[a][b] = -1;
					}
					else {
						int la = toLinearArray((int)(current.getX()), (int)(current.getY	()));
						prior[a][b] = la;
					}
					queue.add(point);
				}
			}
			if (current.getX()/100 > 1600) {
				System.out.print("PIZDA!");
			}
			prev = current;
			tmp.clear();
		}



 		System.out.println("Last point: x:" + current.getX()/100 + " y:" + current.getY()/100);
		try {
			while (prior[(int) (current.getX())][(int) (current.getY())] != -1 && prior[(int) (current.getX())][(int) (current.getY())] != 0) {
				points.addFirst(new Point2D.Double(toNormalFloatRecounter((int) current.getX()), toNormalFloatRecounter((int) current.getY())));
				current = fromLinearArray(prior[(int) (current.getX())]
						[(int) (current.getY())]);
				//current = new Point2D.Double(pointFloatRecounter(current.getX()),pointFloatRecounter(current.getY()));
			}
		}
		catch (Exception ex) {
			System.out.print("Shit!");
		}

		Point2D po = new Point2D.Double(toNormalFloatRecounter((int)points.getLast().getX()), toNormalFloatRecounter((int)points.getLast().getY()));
		points.addFirst(po);

		points.removeLast();


		return points;

	}

	private boolean isFinishPoint(Point2D dest, Point2D current) {
		if (Math.abs(dest.getX()-current.getX()) < 90 && Math.abs(dest.getY()-current.getY()) < 90) {
			return true;
		}
		else
			return false;
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
		Point2D point;
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