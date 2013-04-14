package com.example.shoppersstop;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.ScrollView;

class Renderer extends ScrollView {

	Rect[] shelfs;
	List<ItemsMap> items;

	List<int[]> path;

	public Renderer(Context context) {
		super(context);
	}

	public void setShelfs(Rect[] shelfs) {
		this.shelfs = shelfs;
	}

	public void setItems(List<ItemsMap> items) {
		this.items = items;
	}

	public void setPath(List<int[]> path) {
		this.path = path;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < shelfs.length; i++) {
			drawShelf(canvas, shelfs[i]);
		}
//		for (int i = 0; i < path.size() - 1; i++) {
//			drawPath(canvas, path.get(i), path.get(i + 1));
//		}
		for (int i = 0; i < items.size(); i++) {
			drawItem(canvas, items.get(i));
		}
	}

	protected void drawItem(Canvas canvas, ItemsMap x) {
		Paint paintItem = new Paint();
		paintItem.setColor(Color.RED);
		paintItem.setStyle(Paint.Style.FILL);
		paintItem.setStrokeWidth(3);
		canvas.drawRect(x.getX(), x.getY(), x.getX() + 20, x.getY() + 20,
				paintItem);
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
