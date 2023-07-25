package com.rilixtech.widget.countrycodepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NewRoundRectCornerImageView extends ImageView {


    private float radius = 12.0f;
    private Path path;
    private RectF rect;

    public NewRoundRectCornerImageView(Context context) {
        super(context);
        init();
    }

    public NewRoundRectCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewRoundRectCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
      float value1= getContext().getResources().getDimension(com.intuit.sdp.R.dimen._3sdp);

        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, value1, value1, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
