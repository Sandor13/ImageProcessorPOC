package example.com.imageprocessorpoc;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class CustomPicker extends LinearLayout {

    private static final int DEFAULT_NUMBER_OF_BLACK = 5;
    private static final int DEFAULT_NUMBER_OF_RED = 0;
    private static final int DEFAULT_BLACK_COLOR = 0xFF000000;
    private static final int DEFAULT_RED_COLOR = 0xFFCC0000;
    private static final boolean DEFAULT_ENABLED = true;

    private int numberOfDigitsBeforeDot = DEFAULT_NUMBER_OF_BLACK;
    private int numberOfDigitsAfterDot = DEFAULT_NUMBER_OF_RED;
    private int firstColor = DEFAULT_BLACK_COLOR;
    private int secondColor = DEFAULT_RED_COLOR;
    private boolean enabled = DEFAULT_ENABLED;

    private int pickerStyleId = -1;
    @NonNull
    private final Question question;

    public CustomPicker(Context context, @NonNull Question question) {
        super(context);
        this.question = question;
        init(context, question);
    }

    public CustomPicker(Context context, @Nullable AttributeSet attrs, @NonNull Question question) {
        super(context, attrs);
        this.question = question;
        init(context, question);
    }

    public CustomPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @NonNull Question question) {
        super(context, attrs, defStyleAttr);
        this.question = question;
        init(context, question);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, @NonNull Question question) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.question = question;
        init(context, question);
    }

    private void init(@NonNull Context context, @NonNull Question question) {
        setOrientation(HORIZONTAL);
        final Double highValue = question.getHighValue()[0];
        Integer maxDec = question.getMaxDec();
        numberOfDigitsBeforeDot = highValue == null ? 0 : String.valueOf(highValue.intValue()).length();
        numberOfDigitsAfterDot = maxDec == null ? 0 : maxDec;
        firstColor = ContextCompat.getColor(context, R.color.color_black);
        secondColor = ContextCompat.getColor(context, R.color.color_primary_light);
        //pickerStyleId = typedArray.getResourceId(R.styleable.MeterView_mv_pickerStyle, pickerStyleId);
        enabled = true;
        populate(context, question);
    }

    private void populate(@NonNull Context context, @NonNull Question question) {
        String[] units = question.getUnit();
        if (units != null && units.length > 0) {
            NumberPicker picker = createStringPicker(context, units);
            picker.setEnabled(isEnabled());
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            addView(picker, lp);
        }
        for (int i = 0; i < numberOfDigitsBeforeDot + numberOfDigitsAfterDot; i++) {
            NumberPicker picker = createNumberPicker(context);
            //picker.setBackgroundColor(i < numberOfDigitsBeforeDot ? firstColor : secondColor);
            picker.setEnabled(isEnabled());
            picker.setMinValue(0);
            picker.setMaxValue(9);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            addView(picker, lp);
        }
    }

    private NumberPicker createStringPicker(Context context, String[] units) {
        LayoutInflater inflater = LayoutInflater.from(context);
        NumberPicker picker = (NumberPicker) inflater.inflate(R.layout.picker_layout, null, false);
        picker.setMinValue(0);
        picker.setMaxValue(units.length - 1);
        picker.setDisplayedValues(units);
        return picker;
    }

    private NumberPicker createNumberPicker(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //return new NumberPicker(context);
        return (NumberPicker) inflater.inflate(R.layout.picker_layout, null, false);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !enabled || super.onInterceptTouchEvent(ev);
    }

    /**
     * Returns current value of the widget. Works only if "mnp_max" is not bigger then 9.
     * For other cases you have to extend this view for now.
     */
    public int getValue() {
        int result = 0;
        final int childCount = getChildCount();
        int koeff = childCount - 1;
        for (int i = 1; i < childCount; i++) {
            NumberPicker picker = (NumberPicker) getChildAt(i);
            result += picker.getValue() * Math.pow(10, --koeff);
        }
        return result;
    }

    public int getUnit() {
        int unit;
        NumberPicker picker = (NumberPicker) getChildAt(0);
        unit = picker.getValue();
        return unit;
    }

    /**
     * Sets current value to the widget. Works only if "mnp_max" is not bigger then 9.
     * For other cases you have to extend this view for now.
     */
    public void setValue(int value) {
        int koeff = getChildCount();
        for (int i = 1; i < getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) getChildAt(i);
            int number = (int) (value / Math.pow(10, --koeff));
            if (i == 1 && number > 9) {
                throw new IllegalArgumentException("Number of digits cannot be greater then pickers number");
            }
            value -= number * Math.pow(10, koeff);
            picker.setValue(number);
        }
    }
}
