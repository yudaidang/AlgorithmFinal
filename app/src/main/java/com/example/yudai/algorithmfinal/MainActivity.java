package com.example.yudai.algorithmfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.yudai.algorithmfinal.Entity.LocationLocal;
import com.example.yudai.algorithmfinal.TSPTW.DynamicProgrammingTSPTW;
import com.example.yudai.algorithmfinal.TSPTW.Haversine;
import com.example.yudai.algorithmfinal.TSPTW.TSPTWBuild;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button run;
    private double TEST = 0.9;
    private LocationLocal locationLocal = new LocationLocal("Vinpearl Safari Phú Quốc 1", 10.337086, 103.891322, 540, 990, 300, 1);
    private LocationLocal locationLocal1 = new LocationLocal("Di tích Nhà tù Phú Quốc 2", 10.043501, 104.018480, 480, 1020, 90, 2);
    private int stayRoot = 300;
    private int stay1Root = 90;
    private int result = Integer.MAX_VALUE;
    private LocationLocal startNode = new LocationLocal("The Shells Resort ", 10.243979, 103.948455, 540, 1440, 0, 0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<LocationLocal> mLocates = new ArrayList<>();


        //**

        mLocates.add(startNode);
        mLocates.add(new LocationLocal("Chợ đêm Phú Quốc 3", 10.218282, 103.960214, 990, 1440, 180, 3));
        mLocates.add(new LocationLocal("Làng chài Hàm Ninh 4", 10.179362, 104.049026, 240, 1320, 120, 4));
        mLocates.add(new LocationLocal("Dinh Cậu 5", 10.217232, 103.956423, 540, 1200, 60, 5));
        mLocates.add(new LocationLocal("Bãi Sao 6", 10.055666, 104.035671, 540, 1440, 120, 6));
        mLocates.add(new LocationLocal("Xưởng nước mắm Phụng Hưng 7", 10.044376, 104.016972, 540, 1140, 60, 7));
        mLocates.add(new LocationLocal("Bãi Khem 8", 10.033389, 104.030324, 540, 1140, 120, 8));
        mLocates.add(new LocationLocal("Chợ Đông Dương 9", 10.221933, 103.957209, 540, 1140, 90, 9));
        mLocates.add(new LocationLocal("Trung tâm bảo tồn chó xoáy 10", 10.178127, 104.008562, 540, 1080, 60, 10));
        mLocates.add(new LocationLocal("Ngọc Trai Ngọc Hiền 11", 10.207466, 103.962319, 540, 1260, 90, 11));
        mLocates.add(new LocationLocal("Vườn tiêu Ngọc Hà 12", 10.210645, 103.984373, 540, 1140, 60, 12));
        mLocates.add(new LocationLocal("Làng Chài Rạch Vẹm 13", 10.365665, 103.932144, 540, 1140, 90, 13));

        run = findViewById(R.id.run);


        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = 0;
                while(mLocates.size() != 0) {
                    if(k==1){
                        mLocates.add(startNode);
                    }
                    k = 1;
                    TSPTWBuild tsptwBuild = new TSPTWBuild(mLocates);
                    ArrayList<LocationLocal> result = tsptwBuild.selectCluster(startNode, 540, 1320);
                    for (LocationLocal locationLoca1 : result) {
                        for (int i = 0; i < mLocates.size(); i++) {
                            if (locationLoca1.getmId() == mLocates.get(i).getmId()) {
                                mLocates.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
        });


    }

    private Haversine haversine = new Haversine();

    public Integer[][] createTableTravelTime(ArrayList<LocationLocal> locations) {
        int mNumber = locations.size();
        Integer[][] mArray = new Integer[mNumber][mNumber];
        for (int m = 0; m < mNumber; m++) {
            for (int n = 0; n < mNumber; n++) {
                Integer travel = (int) Math.round(haversine.travelTime(locations.get(m).getmLatitude(), locations.get(m).getmLongitude(),
                        locations.get(n).getmLatitude(), locations.get(n).getmLongitude()));
                mArray[m][n] = travel;
            }
        }
        return mArray;
    }
}
