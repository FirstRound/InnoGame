package com.brackeen.scared.controllers;

import com.brackeen.scared.Tile;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MovingController {

	private Point2D currentPosition = null;
	private Point2D destPosition    = null;
	private LinkedList<Point2D> way = new LinkedList<Point2D>();
	private boolean[][] map;

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
		if(!way.isEmpty()) {
			return way.pop();
		}
		else {
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
		points.add(new Point2D.Double(Math.abs(currentPosition.getX())+0.5,
				Math.abs(currentPosition.getY())+0.5));

		Queue<Point2D> queue = new LinkedList<Point2D>();
		queue.add(points.get(0));
		Point2D curren =  null, prev = null;
		List<Point2D> tmp = new LinkedList<Point2D>();
		int prior[][] = new int[getMapHeight()][getMapWidth()];
		while(!queue.isEmpty()) {
			curren = queue.peek();
			queue.remove();
			if (prev == null) {
				prior[(int)curren.getX()][(int)curren.getY()] = -1;
			}
			else {
				prior[(int)curren.getX()][(int)curren.getY()] = toLinearArray((int)prev.getX(), (int)prev.getY());
			}
			if (curren.equals(to)) {
				break;
			}
			tmp.add(new Point2D.Double(curren.getX()+1, curren.getY()+1));
			tmp.add(new Point2D.Double(curren.getX()-1, curren.getY()-1));
			tmp.add(new Point2D.Double(curren.getX()+1, curren.getY()-1));
			tmp.add(new Point2D.Double(curren.getX()-1, curren.getY()+1));
			tmp.add(new Point2D.Double(curren.getX()+1, curren.getY()));
			tmp.add(new Point2D.Double(curren.getX(), curren.getY()+1));
			tmp.add(new Point2D.Double(curren.getX()-1, curren.getY()));
			tmp.add(new Point2D.Double(curren.getX(), curren.getY()-1));

			for (Point2D point : tmp) {
				if (prior[(int)point.getX()][(int)point.getY()] == 0 && canMoveTo(point)) {
					queue.add(point);
				}
			}

			prev = curren;
			tmp.clear();
		}


		System.out.println(" #x: " + curren.getX() + " y: " + curren.getY());
		while (prior[(int)curren.getX()][(int)curren.getY()] != -1) {

			points.addFirst(curren);
			curren = fromLinearArray(prior[(int)curren.getX()][(int)curren.getY()]) ;
		}
		points.addFirst(points.getLast());
		points.removeLast();


		for (Point2D p : points) {
			System.out.print(" #x: " + p.getX() + " y: " + p.getY());
		}


		return points;

	}

	private int toLinearArray(int x, int y) {
		return x * map.length + y;
	}

	private Point2D fromLinearArray(int i) {
		return new Point2D.Double((i)/map.length, (i)%map.length);
	}

	private boolean isAtDestPoint() {
		return (currentPosition == destPosition); 
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