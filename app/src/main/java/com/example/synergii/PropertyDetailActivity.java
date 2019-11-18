package com.example.synergii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

public class PropertyDetailActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    //private ArrayList<ImageModel> imageModelArrayList;

    //private int[] myImageList = new int[]{R.drawable.harley2, R.drawable.benz2, R.drawable.vecto, R.drawable.webshots, R.drawable.bikess, R.drawable.img1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
    }
}
