package cosine.boat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;

import androidx.annotation.NonNull;


public class LogView extends ScrollView {

    private final TextView mTextView;

    public LogView(@NonNull Context context) {
        super(context);
        this.setBackground(getViewBackground());
        this.mTextView = new LineTextView(context);

        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mTextView);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setTextIsSelectable(true);
        mTextView.setTextSize(15);
        mTextView.setLineSpacing(0, 1f);
    }

    public LogView(Context context, AttributeSet attr) {
        super(context,attr);
        this.setBackground(getViewBackground());
        this.mTextView = new LineTextView(context);

        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mTextView);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setTextIsSelectable(true);
        mTextView.setTextSize(15);
        mTextView.setLineSpacing(0, 1f);
    }

    public void appendLog(String str) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mTextView != null) {
                    LogView.this.mTextView.append(str);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toBottom(LogView.this, mTextView);
                        }
                    }, 50);
                }
            }
        });
    }

    public void cleanLog() {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mTextView != null) {
                    LogView.this.mTextView.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toBottom(LogView.this, mTextView);
                        }
                    }, 50);
                }
            }
        });
    }

    private void toBottom(final ScrollView scrollView, final View view) {
        int offset = view.getHeight()
                - scrollView.getHeight();
        if (offset < 0) {
            offset = 0;
        }
        scrollView.scrollTo(0, offset);
    }

    private LayerDrawable getViewBackground() {
        int radiusSize = 0;
        int mainColor = Color.parseColor("#7f5B5B5B");

        float[] outerR = new float[]{radiusSize, radiusSize, radiusSize, radiusSize, radiusSize, radiusSize, radiusSize, radiusSize};
        RoundRectShape rectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(rectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(mainColor);

        Drawable[] layers = new Drawable[]{shapeDrawable};

        return new LayerDrawable(layers);
    }

    public class LineTextView extends androidx.appcompat.widget.AppCompatTextView {
        private final Paint line;

        public LineTextView(Context context) {
            super(context);
            setFocusable(true);
            line = new Paint();
            line.setColor(Color.RED);
            line.setStrokeWidth(2);
            setPadding(95, 0, 0, 0);
            setGravity(Gravity.TOP);
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            if (getText().toString().length() != 0) {
                float y;
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(getTextSize());
                for (int l = 0; l < getLineCount(); l++) {
                    y = ((l + 1) * getLineHeight()) - getLineHeight() / 4;
                    canvas.drawText(String.valueOf(l + 1), 0, y, p);
                    canvas.save();
                }
            }
            int k = getLineHeight();
            int i = getLineCount();
            canvas.drawLine(90, 0, 90, getHeight() + (i * k), line);
            int y = (getLayout().getLineForOffset(getSelectionStart()) + 1) * k;
            canvas.save();
            canvas.restore();
            super.onDraw(canvas);
        }
    }
}
