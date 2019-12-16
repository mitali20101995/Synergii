package com.example.synergii;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

public class PropertyDetails extends AppCompatActivity {


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.ic_villa,R.drawable.ic_villa,R.drawable.ic_villa};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();

    }

    private ArrayList<ImageModel> populateList () {

        ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init () {


        mPager = (ViewPager) findViewById(R.id.viewPager);
        SlidingImage_Adapter slidingImage_adapter = new SlidingImage_Adapter(this);
        mPager.setAdapter(slidingImage_adapter);
            /*
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new SlidingImage_Adapter(PropertyDetails.this, imageModelArrayList));

            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

            //Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES = imageModelArrayList.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                public void onPageSelected(int position) {
                    currentPage = position;

                }


                public void onPageScrolled(int pos, float arg1, int arg2) {

                }


                public void onPageScrollStateChanged(int pos) {

                }
            });
*/
    }


}
