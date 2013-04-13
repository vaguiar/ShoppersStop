package com.example.shoppersstop;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

class Renderer extends View {
	public static enum Type {
		SHELF, PATH, ITEM
	};

	Rect[] shelfs;
	Type type;
	List<int[]> items;
	List<int[]> path;

	public Renderer(Context context) {
		super(context);
	}

	public void setShelfs(Rect[] shelfs) {
		this.shelfs = shelfs;
		this.type = Type.SHELF;
	}

	public void setItems(List<int[]> items) {
		this.items = items;
		this.type = Type.ITEM;
	}
	
	public void setPath(List<int[]> path) {
		this.path = path;
		this.type = Type.ITEM;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		switch (type) {
		case SHELF:
			for (int i = 0; i < shelfs.length; i++) {
				drawShelf(canvas, shelfs[i]);
			}
			break;
		case PATH:
			for (int i = 0; i < path.size() - 1; i++) {
				drawPath(canvas, path.get(i), path.get(i + 1));
			}
			break;
		case ITEM:
			for (int i = 0; i < items.size(); i++) {
				drawItem(canvas, items.get(i));
			}
			break;
		}
	}

	protected void drawItem(Canvas canvas, int[] x) {
		Paint paintItem = new Paint();
		paintItem.setColor(Color.RED);
		paintItem.setStyle(Paint.Style.FILL);
		paintItem.setStrokeWidth(3);
		canvas.drawRect(x[0], x[1], x[0] + 20, x[1] + 20, paintItem);
	}

	protected void drawPath(Canvas canvas, int[] start, int[] stop) {
		Paint paintPath = new Paint();
		paintPath.setColor(Color.GREEN);
		paintPath.setStyle(Paint.Style.STROKE);
		paintPath.setStrokeWidth(3);
		canvas.drawLine(start[0], start[1], stop[0], stop[1], paintPath);
	}

	protected void drawShelf(Canvas canvas, Rect r) {
		Paint myPaint = new Paint();
		myPaint.setColor(Color.BLUE);
		myPaint.setStyle(Paint.Style.FILL);
		myPaint.setStrokeWidth(3);
		canvas.drawRect(r, myPaint);
	}
}