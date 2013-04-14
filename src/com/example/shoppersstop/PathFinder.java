package com.example.shoppersstop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.view.animation.BounceInterpolator;

public class PathFinder {

	private Renderer renderer;
	private List itemQueue;
	private ItemsMap boundary;
	private dir direction;
	private static enum dir{
		CLOCKWISE,
		ANTICLOCKWISE
	};
	private int[][] clockBoundary = new int[4][2];
	private int[][] antiClockBoundary = new int[4][2];

	public PathFinder(Renderer renderer) {
		this.renderer = renderer;

	}

	private int[][] getClockBoundary() {
		return clockBoundary;
	}

	private void setClockBoundary(ItemsMap boundary) {

		clockBoundary[0][0] = boundary.getX() / 2;
		clockBoundary[0][1] = boundary.getY();

		clockBoundary[1][0] = 0;
		clockBoundary[1][1] = boundary.getY() / 2;

		clockBoundary[2][0] = boundary.getX() / 2;
		clockBoundary[2][1] = 0;

		clockBoundary[3][0] = boundary.getX();
		clockBoundary[3][1] = boundary.getY() / 2;
	}

	private int[][] getAntiClockBoundary() {
		return antiClockBoundary;
	}

	private void setAntiClockBoundary(ItemsMap boundary) {
		antiClockBoundary[0][0] = boundary.getX() / 2;
		antiClockBoundary[0][1] = boundary.getY();

		antiClockBoundary[1][0] = boundary.getX();
		antiClockBoundary[1][1] = boundary.getY() / 2;

		antiClockBoundary[2][0] = boundary.getX() / 2;
		antiClockBoundary[2][1] = 0;

		antiClockBoundary[3][0] = 0;
		antiClockBoundary[3][1] = boundary.getY() / 2;
	}

	public void calculatePath(List<ItemsMap> itemCoordinates, ItemsMap startPt,
			ItemsMap endPt, ItemsMap boundary) {
		this.boundary = boundary;		
		this.itemQueue = sortItemsInQuadrants(startPt, endPt, boundary, itemCoordinates);
		renderer.setPath(itemQueue);
	}

	private List <ItemsMap> sortItemsInQuadrants(ItemsMap startPt, ItemsMap endPt, ItemsMap boundary,
			List<ItemsMap> itemCoordinates) {
		List[] quadrantPoints = new ArrayList[4];
		
		if (startPt.getX() <= boundary.getX() / 2) {
			direction=dir.CLOCKWISE;
			quadrantPoints = clockWiseSort(itemCoordinates, boundary);
		} else {
			direction=dir.ANTICLOCKWISE;
			quadrantPoints = antiClockWiseSort(itemCoordinates, boundary);
		}
		return enQueueItemPoints(startPt, endPt, itemCoordinates, quadrantPoints);
	}

	private List enQueueItemPoints(ItemsMap startPt, ItemsMap endPt,
			List<ItemsMap> itemCoordinates, List[] quadrantPoints) {
		
		 List <ItemsMap> itemQueue = new LinkedList<ItemsMap>() ;
		ItemsMap temp_start;
		
		itemQueue.add(startPt);
				
		for(int i = 0; i < quadrantPoints.length; i++){
			temp_start = new ItemsMap();
			if(direction ==  dir.CLOCKWISE){
				temp_start.setX(clockBoundary[i][0]);
				temp_start.setY(clockBoundary[i][1]);
			}
			else{
				temp_start.setX(antiClockBoundary[i][0]);
				temp_start.setY(antiClockBoundary[i][1]);
			}
			
			while(quadrantPoints[i]!= null && quadrantPoints[i].size() > 0){
				temp_start = getMinimum(temp_start,quadrantPoints[i]);
				itemQueue.add(temp_start);
				quadrantPoints[i].remove(temp_start);

			}
			
		}
		itemQueue.add(endPt);
		
		return itemQueue;
	}

	private ItemsMap getMinimum(ItemsMap temp_start, List<ItemsMap> quadrantPoints) {
		
		float min = Float.MAX_VALUE;
		float temp;
		ItemsMap closest = null;
		
		for(ItemsMap each: quadrantPoints){
			if((temp = calcDistance(temp_start, each)) < min){
				min = temp;
				closest = each;
			}
		}
			
		return closest;
	}

	private List[] antiClockWiseSort(List<ItemsMap> itemCoordinates, ItemsMap boundary) {
		List[] quadrantPoints = new ArrayList[4];

		for (ItemsMap each : itemCoordinates) {
			if (isLesserThan(each,
					new ItemsMap(boundary.getX() / 2, boundary.getY() / 2))) {
				if (quadrantPoints[2] != null) {
					quadrantPoints[2].add(each);
				} else {
					quadrantPoints[2] = new ArrayList<ItemsMap>();
					quadrantPoints[2].add(each);
				}
			} else if ((isLesserThan(each, new ItemsMap(boundary.getX(),
					boundary.getY() / 2)))) {
				if (quadrantPoints[1] != null) {
					quadrantPoints[1].add(each);
				} else {
					quadrantPoints[1] = new ArrayList<ItemsMap>();
					quadrantPoints[1].add(each);
				}
			} else if ((isLesserThan(each, new ItemsMap(boundary.getX() / 2,
					boundary.getY())))) {
				if (quadrantPoints[3] != null) {
					quadrantPoints[3].add(each);
				} else {
					quadrantPoints[3] = new ArrayList<ItemsMap>();
					quadrantPoints[3].add(each);
				}
			} else {
				if (quadrantPoints[0] != null) {
					quadrantPoints[0].add(each);
				} else {
					quadrantPoints[0] = new ArrayList<ItemsMap>();
					quadrantPoints[0].add(each);
				}
			}

		}

		return quadrantPoints;
	}

	private List[] clockWiseSort(List<ItemsMap> itemCoordinates, ItemsMap boundary) {
		List[] quadrantPoints = new ArrayList[4];

		for (ItemsMap each : itemCoordinates) {
			if (isLesserThan(each,
					new ItemsMap(boundary.getX() / 2, boundary.getY() / 2))) {
				if (quadrantPoints[1] != null) {
					quadrantPoints[1].add(each);
				} else {
					quadrantPoints[1] = new ArrayList<ItemsMap>();
					quadrantPoints[1].add(each);
				}
			} else if ((isLesserThan(each, new ItemsMap(boundary.getX(),
					boundary.getY() / 2)))) {
				if (quadrantPoints[2] != null) {
					quadrantPoints[2].add(each);
				} else {
					quadrantPoints[2] = new ArrayList<ItemsMap>();
					quadrantPoints[2].add(each);
				}
			} else if ((isLesserThan(each, new ItemsMap(boundary.getX() / 2,
					boundary.getY())))) {
				if (quadrantPoints[0] != null) {
					quadrantPoints[0].add(each);
				} else {
					quadrantPoints[0] = new ArrayList<ItemsMap>();
					quadrantPoints[0].add(each);
				}
			} else {
				if (quadrantPoints[3] != null) {
					quadrantPoints[3].add(each);
				} else {
					quadrantPoints[3] = new ArrayList<ItemsMap>();
					quadrantPoints[3].add(each);
				}
			}

		}

		return quadrantPoints;
	}

	private boolean isLesserThan(ItemsMap pt1, ItemsMap pt2) {
		return ((pt1.getX() <= pt2.getX()) && (pt1.getY() <= pt2.getY()));
	}
	
	private float calcDistance(ItemsMap p1, ItemsMap p2){
		
		return (float) Math.sqrt(Math.pow(p1.getX()- p2.getX(), 2) + Math.pow(p1.getY()- p2.getY(), 2));
	}
	
}
