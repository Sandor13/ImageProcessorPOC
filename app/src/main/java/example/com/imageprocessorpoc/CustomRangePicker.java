package example.com.imageprocessorpoc;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomRangePicker extends LinearLayout {

    /*private static final int DEFAULT_NUMBER_OF_BLACK = 5;
    private static final int DEFAULT_NUMBER_OF_RED = 0;
    private static final int DEFAULT_BLACK_COLOR = 0xFF000000;
    private static final int DEFAULT_RED_COLOR = 0xFFCC0000;
    private int numberOfDigitsBeforeDot = DEFAULT_NUMBER_OF_BLACK;
    private int numberOfDigitsAfterDot = DEFAULT_NUMBER_OF_RED;
    private int firstColor = DEFAULT_BLACK_COLOR;
    private int secondColor = DEFAULT_RED_COLOR;
*/
    private static final int PICKER_BEFORE_DOT = 1;
    private static final int PICKER_AFTER_DOT = 2;

    private double value;
    private int digitsAfterDot;

    public CustomRangePicker(Context context, @NonNull Question question) {
        super(context);
        init(context, question);
    }

    public CustomRangePicker(Context context, @Nullable AttributeSet attrs, @NonNull Question question) {
        super(context, attrs);
        init(context, question);
    }

    public CustomRangePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @NonNull Question question) {
        super(context, attrs, defStyleAttr);
        init(context, question);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomRangePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, @NonNull Question question) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, question);
    }

    private void init(@NonNull Context context, @NonNull Question question) {
        setOrientation(HORIZONTAL);
        final Double highValue = question.getHighValue()[0];
        Integer maxDec = question.getMaxDec();
        value = new BigDecimal(highValue).setScale(maxDec == null ? 0 : maxDec, RoundingMode.UP).doubleValue();
        digitsAfterDot = (int) Math.pow(10, question.getMaxDec());
        //firstColor = ContextCompat.getColor(context, R.color.color_black);
        //secondColor = ContextCompat.getColor(context, R.color.color_primary_light);
        populate(context, question);
    }

    private void populate(@NonNull Context context, @NonNull Question question) {
        setUnits(context, question);
        setPickerBeforeDot(context, question);
        //setDot(context);
        setPickerAfterDot(context, digitsAfterDot - 1);
    }

    private void setUnits(@NonNull Context context, @NonNull Question question) {
        String[] units = question.getUnit();
        NumberPicker picker = createStringPicker(context, units);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.picker_margin), 0);
        addView(picker, layoutParams);
    }

    private NumberPicker createStringPicker(Context context, String[] units) {
        LayoutInflater inflater = LayoutInflater.from(context);
        NumberPicker picker = (NumberPicker) inflater.inflate(R.layout.picker_layout, null, false);
        picker.setMinValue(0);
        picker.setMaxValue(units.length - 1);
        picker.setDisplayedValues(units);
        return picker;
    }

    private void setPickerBeforeDot(@NonNull Context context, @NonNull Question question) {
        NumberPicker pickerBeforeDot = createNumberPicker(context);
        //picker.setBackgroundColor(i < numberOfDigitsBeforeDot ? firstColor : secondColor);
        int min = (int) Math.round(question.getLowValue()[0]);
        int max = (int) Math.round(question.getHighValue()[0]);
        pickerBeforeDot.setMinValue(min);
        pickerBeforeDot.setMaxValue(max);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.picker_margin), 0);
        pickerBeforeDot.setOnValueChangedListener((numberPicker, oldVal, newVal) -> {
            if (newVal == max) {
                removeView(getChildAt(2));
                setPickerAfterDot(context, (int) ((value - Math.floor(value)) * digitsAfterDot));
            } else {
                if (oldVal == max) {
                    removeView(getChildAt(2));
                    setPickerAfterDot(context, digitsAfterDot - 1);
                }
            }
        });
        addView(pickerBeforeDot, layoutParams);
    }

    private void setPickerAfterDot(@NonNull Context context, int maxValue) {
        NumberPicker pickerAfterDot = createNumberPicker(context);
        pickerAfterDot.setEnabled(isEnabled());
        pickerAfterDot.setMinValue(0);
        pickerAfterDot.setMaxValue(maxValue);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        addView(pickerAfterDot, lp);
    }

    private NumberPicker createNumberPicker(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return (NumberPicker) inflater.inflate(R.layout.picker_layout, null, false);
    }

    public int getUnit() {
        int unit;
        NumberPicker picker = (NumberPicker) getChildAt(0);
        unit = picker.getValue();
        return unit;
    }

    /**
     * Returns current value of the widget. Works only if "mnp_max" is not bigger then 9.
     * For other cases you have to extend this view for now.
     */
    public double getValue() {
        NumberPicker picker = (NumberPicker) getChildAt(PICKER_BEFORE_DOT);
        double beforeDot = picker.getValue();
        picker = (NumberPicker) getChildAt(PICKER_AFTER_DOT);
        double afterDot = ((double) picker.getValue() / digitsAfterDot);
        return beforeDot + afterDot;
    }

    /**
     * More universal method to get value
     * Returns current value of the widget. Works only if "mnp_max" is not bigger then 9.
     * For other cases you have to extend this view for now.
     */
    /*public int getValue() {
        int result = 0;
        final int childCount = getChildCount();
        int koeff = childCount - 1;
        for (int i = 1; i < childCount; i++) {
            NumberPicker picker = (NumberPicker) getChildAt(i);
            result += picker.getValue() * Math.pow(10, --koeff);
        }
        return result;
    }*/

    /*  //Set a dot before decimal
    private void setDot(@NonNull Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView textView = (TextView) inflater.inflate(R.layout.picker_dot_layout, null, false);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);
        layoutParams.gravity = Gravity.CENTER;
        addView(textView, layoutParams);
    }*/

    //private boolean enabled = true;
    /*@Override
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
     }*/
}
