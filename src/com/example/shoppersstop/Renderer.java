package com.example.shoppersstop;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ScrollView;

class Renderer extends ScrollView {

	List<StoreShelf> shelfs;
	List<ItemsMap> items;

	List<ItemsMap> path;

	public Renderer(Context context) {
		super(context);
	}

	/*
	 * public void setShelfs(Rect[] shelfs) { this.shelfs = shelfs; }
	 */

	public void setShelfs(List<StoreShelf> shelfs) {
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
		canvas.drawColor(Color.GRAY);
		for (int i = 0; i < shelfs.size(); i++) {
			drawShelf(canvas, shelfs.get(i));
		}
		for (int i = 0; i < items.size(); i++) {
			drawItem(canvas, items.get(i));
		}
		for (int i = 0; i < path.size() - 1; i++) {
			drawPath(canvas, path.get(i), path.get(i + 1));
		}
	}

	protected void drawItem(Canvas canvas, ItemsMap x) {
		Paint paintItem = new Paint();
		paintItem.setColor(Color.RED);
		paintItem.setStyle(Paint.Style.FILL);
		Paint paintText = new Paint();
		paintText.setColor(Color.rgb(176, 226, 255));
		paintText.setStyle(Paint.Style.FILL_AND_STROKE);
		paintText.setTextSize(15);
		paintText.setStrokeWidth(1);
		paintText.setTypeface(Typeface.SANS_SERIF);
		canvas.drawCircle(x.getX(), x.getY(), 9, paintItem);
		canvas.drawText(x.getName(), x.getX() + 2, x.getY() + 6, paintText);
	}

	protected void drawPath(Canvas canvas, ItemsMap start, ItemsMap stop) {
		Paint paintPath = new Paint();
		paintPath.setColor(Color.GREEN);
		paintPath.setStyle(Paint.Style.STROKE);
		paintPath.setStrokeWidth(4);
		canvas.drawLine(start.getX(), start.getY(), stop.getX(), stop.getY(),
				paintPath);
	}

	protected void drawShelf(Canvas canvas, StoreShelf eachShelf) {
		Paint myPaint = new Paint();
		myPaint.setColor(Color.BLUE);
		myPaint.setStyle(Paint.Style.FILL);
		myPaint.setStrokeWidth(3);

		canvas.drawRect(eachShelf.getP1_X(), eachShelf.getP1_Y(),
				eachShelf.getP2_X(), eachShelf.getP2_Y(), myPaint);
		if (eachShelf.getCatagory().equals("ENTRANCE")) {
			Paint paintText = new Paint();
			paintText.setColor(Color.rgb(255, 69, 0));
			paintText.setStyle(Paint.Style.FILL_AND_STROKE);
			paintText.setTextSize(22);
			paintText.setStrokeWidth(2);
			paintText.setTypeface(Typeface.SANS_SERIF);
			canvas.drawText("ENTRY", eachShelf.getP1_X() - 30, eachShelf.getP1_Y() + 6, paintText);
		}
		else if(eachShelf.getCatagory().equals("EXIT")) {
			Paint paintText = new Paint();
			paintText.setColor(Color.rgb(255, 69, 0));
			paintText.setStyle(Paint.Style.FILL_AND_STROKE);
			paintText.setTextSize(22);
			paintText.setStrokeWidth(2);
			paintText.setTypeface(Typeface.SANS_SERIF);
			canvas.drawText(eachShelf.getCatagory(), eachShelf.getP1_X() + 10, eachShelf.getP1_Y() + 6, paintText);
		}
	}
}
