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

	List<ItemsMap> path;

	public Renderer(Context context) {
		super(context);
	}

	public void setShelfs(Rect[] shelfs) {
		this.shelfs = shelfs;
	}

	public void setItems(List<ItemsMap> items) {
		this.items = items;
	}

	public void setPath(List<ItemsMap> path) {
		this.path = path;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < shelfs.length; i++) {
			drawShelf(canvas, shelfs[i]);
		}
		for (int i = 0; i < path.size() - 1; i++) {
			drawPath(canvas, path.get(i), path.get(i + 1));
		}
		for (int i = 0; i < items.size(); i++) {
			drawItem(canvas, items.get(i));
		}
	}

	protected void drawItem(Canvas canvas, ItemsMap x) {
		Paint paintItem = new Paint();
		paintItem.setColor(Color.RED);
		paintItem.setStyle(Paint.Style.FILL);
		canvas.drawCircle(x.getX(), x.getY(), 9, paintItem);
	}

	protected void drawPath(Canvas canvas, ItemsMap start, ItemsMap stop) {
		Paint paintPath = new Paint();
		paintPath.setColor(Color.GREEN);
		paintPath.setStyle(Paint.Style.STROKE);
		paintPath.setStrokeWidth(4);
		canvas.drawLine(start.getX(), start.getY(), stop.getX(), stop.getY(), paintPath);
	}

	protected void drawShelf(Canvas canvas, Rect r) {
		Paint myPaint = new Paint();
		myPaint.setColor(Color.BLUE);
		myPaint.setStyle(Paint.Style.FILL);
		myPaint.setStrokeWidth(3);
		canvas.drawRect(r, myPaint);
	}
}
