package com.devomb.cornerbanner.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import com.devomb.cornerbanner.CornerBanner;

public class MainActivity extends Activity {

    private ImageView car;
    private ImageView poster;
    private ImageView hoodie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ll);

        car = (ImageView) findViewById(R.id.car_image);
        poster = (ImageView) findViewById(R.id.poster_image);
        hoodie = (ImageView) findViewById(R.id.hoodie_image);

        createBanners();
    }

    private void createBanners(){
        CornerBanner sold = new CornerBanner(this);
        sold.setText("Sold");
        sold.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sold.setWidth(200);
        sold.setHeight(50);
        sold.setBackgroundResource(R.color.blue);
        sold.attachTo(car);

        CornerBanner inTheater = new CornerBanner(this);
        inTheater.setText("In Theater");
        inTheater.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        inTheater.setBackgroundResource(R.color.red);
        inTheater.attachTo(poster, CornerBanner.Position.TOP_RIGHT_CORNER);

        CornerBanner sale = new CornerBanner(this);
        sale.setText("- 60%");
        sale.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        sale.setPadding(16,8,16,8);
        sale.setBackgroundResource(R.color.purple);
        sale.attachTo(hoodie);
    }
}
