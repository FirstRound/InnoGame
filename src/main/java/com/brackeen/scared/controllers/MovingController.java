package com.brackeen.scared.controllers;

import com.brackeen.scared.Tile;

import java.awt.geom.Point2D;
import java.util.LinkedList;

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
		way = getOptimalWay(currentPosition, destPosition); 
	}

	private LinkedList<Point2D> getOptimalWay(Point2D from, Point2D to) {
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		//move to tile center
		points.add(new Point2D.Double(Math.abs(currentPosition.getX())+0.5,
				Math.abs(currentPosition.getY())+0.5));



		return points;

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
		return !(this.map[(int)dest.getX()][(int)dest.getY()]);
	}

	public int getMapWidth() {
		return map.length;
	}

	public int getMapHeight() {
		return map[0].length;
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