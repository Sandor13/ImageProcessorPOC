package example.com.imageprocessorpoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//https://github.com/GuilhE/android-circular-progress-view?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6152

public class MainScreen extends AppCompatActivity {

    private Unbinder butterKnifeUnbinder;

    @BindView(R.id.number_picker_1)
    NumberPicker numberPicker1;
    @BindView(R.id.number_picker_2)
    NumberPicker numberPicker2;
    @BindView(R.id.number_picker_3)
    NumberPicker numberPicker3;
    @BindView(R.id.custom)
    LinearLayout linearLayout;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.buttonValue)
    TextView buttonValue;
    @BindView(R.id.buttonUnit)
    TextView buttonUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        butterKnifeUnbinder = ButterKnife.bind(this);

        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(2);
        numberPicker1.setDisplayedValues(new String[]{"Belgium", "France", "United Kingdom"});
        numberPicker2.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker3.setMaxValue(9);
        numberPicker3.setMinValue(0);

        Double[] lowValue = {0D, 0D};
        Double aDouble = 9999999999D;
        Double[] highValue = {aDouble, aDouble};
        //Double[] highValue = {107.53999999999999D, 42D};
        final Question question = new Question(null, null, null, null, null, null, null, new String[]{"degrees F", "degrees C"}, lowValue, highValue, 2);
        CustomRangePicker customPicker = new CustomRangePicker(this, question);
        linearLayout.addView(customPicker);

        buttonValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("" + customPicker.getValue());
            }
        });
        buttonUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("" + customPicker.getUnit());
            }
        });
    }

    @Override
    protected void onDestroy() {
        butterKnifeUnbinder.unbind();
        super.onDestroy();
    }
}
