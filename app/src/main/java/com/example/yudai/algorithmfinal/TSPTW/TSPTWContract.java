package com.example.yudai.algorithmfinal.TSPTW;

import com.example.yudai.algorithmfinal.Entity.LocationLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface TSPTWContract {

//    //Chạy logic
//    ArrayList<LocationLocal> buildLogic(LocationLocal startLocation, int startHour, int endHour);
//
//    //1. Kiểm tra tổng thời gian lưu trú có lớn hơn thời gian đi trong ngày không?
//    void filterLocationStayAll();

    //Chọn cluster theo điều kiện
    ArrayList<LocationLocal> selectCluster(LocationLocal location, int startHour, int endHour);

    //sắp xếp location theo thời gian đóng mở cửa;
    void sortLocationWOpenHour(ArrayList<LocationLocal> locations);


    //Lọc location theo thời gian đóng mở cửa: điều kiện là thời gian đóng mở cửa phải xen kẽ nhau và nằm trong khoảng thời gian đi trong ngày.
    //Nếu không tìm được thì lấy location ở cụm còn lại sao cho thỏa thời gian tới location đó phải ít hơn thời gian chờ của địa điểm gần nhất.
    void filterClusterWTime(int startHour, int endHour, ArrayList<LocationLocal> mLocations, ArrayList<LocationLocal> mLocationsDiff, double centroidLat, double centroidLon);

    //Lấy cluster có chứa điểm bắt đầu
    int getClusterContaintLocationStart(HashMap<Integer, HashSet<LocationLocal>> listLocationCLuster, LocationLocal mLocate);

    //lọc cluster trước khi chạy thuật toán
    void filterCluster();

    //chạy thuật toán tìm đường đi ngắn nhất
    void TSPTWBuild();

    void getOpenCloseHour();

    void getStay();


}
