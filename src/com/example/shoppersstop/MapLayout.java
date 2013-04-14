package com.example.shoppersstop;

import java.util.List;

import android.graphics.Rect;

public class MapLayout {
	
	private Renderer renderer;
	
	public MapLayout(Renderer renderer){
		this.renderer = renderer;
	}

	public void createMapLayout(List<StoreShelf> allStoreShelves) {
		
		renderer.setShelfs(getCoordinateList(allStoreShelves));
			
	}

	private Rect[] getCoordinateList(List<StoreShelf> storeShelves){
		
		Rect [] shelves = new Rect[storeShelves.size()];
		int i = 0;
		for(StoreShelf each:storeShelves){
			shelves[i].left = each.getP1_X();
			shelves[i].top = each.getP1_Y();
			shelves[i].right = each.getP2_X();
			shelves[i].bottom = each.getP2_Y();
			i++;	
		}
		return shelves;
	}
	
	
}
