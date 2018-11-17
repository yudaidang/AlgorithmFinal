package com.example.yudai.algorithmfinal.TSPTW;

import android.util.Log;

import com.example.yudai.algorithmfinal.Clustering.ClusteringBuild;
import com.example.yudai.algorithmfinal.Entity.LocationLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TSPTWBuild implements TSPTWContract {
    private ArrayList<LocationLocal> mLocates;

    public TSPTWBuild(ArrayList<LocationLocal> mLocates) {
        this.mLocates = mLocates;
    }

    private Haversine haversine = new Haversine();
//    private int mStayAll = 0;
//    private int endHour, startHour;
//    private LocationLocal startLocation;
//    @Override
//    public ArrayList<LocationLocal> buildLogic(LocationLocal startLocation, int startHour, int endHour) {
//        this.endHour = endHour;
//        this.startHour = startHour;
//        this.startLocation = startLocation;
//        return null;
//    }
//
//    @Override
//    public void filterLocationStayAll() {
//        while(mStayAll > ) {
//            for (int i = 0; i < mLocates.size(); i++) {
//                mStayAll += mLocates.get(i).getmStay();
//            }
//            if (mStayAll > endHour - startHour) {
//                continue;
//            }
//        }
//    }

    @Override
    public ArrayList<LocationLocal> selectCluster(LocationLocal location, int startHour, int endHour) {
        int minTravelTime = -1;
        int numStart = 0;
        List<Integer> listTSPTW = null;
        int mStay = 0;
        ArrayList<LocationLocal> mLocationCluster = new ArrayList<>();
        ArrayList<LocationLocal> mLocationClusterDiff = new ArrayList<>();
        ArrayList<LocationLocal> mLocationList = new ArrayList<>();
        //Loop: tổng thời gian lưu trú > thời gian đi trong ngày
        while (mStay + minTravelTime < endHour - startHour) {
            minTravelTime = 0;
            mStay = 0;

            //2. GOM CỤM
            ClusteringBuild clusteringBuild = new ClusteringBuild(mLocates);

            //danh sách các địa điểm cho từng cụm
            HashMap<Integer, HashSet<LocationLocal>> mLocationForCluster = clusteringBuild.flowMain();

            //Lấy cụm chứa điểm bắt đầu
            int cluster = getClusterContaintLocationStart(mLocationForCluster, location);

            //danh sách các địa điểm có chứa địa điểm bắt đầu
            mLocationList = new ArrayList<>(mLocationForCluster.get(cluster));

            // Lấy tổng thời gian lưu trú và index điểm bắt đầu trong list từ cụm
            for (int i = 0; i < mLocationList.size(); i++) {
                mStay += mLocationList.get(i).getmStay();
                if (mLocationList.get(i).getmId() == location.getmId()) {
                    numStart = i;
                }
            }
            Log.d("VONGLAPMSTAY", mStay + " ");

            //tổng thời gian lưu trú lớn hơn thời gian đi trong ngày tiếp tục lặp
            if (mStay > endHour - startHour) {
                continue;
            }
            //Tìm đường đi tối ưu nhất cho các địa điểm trong cụm
            DynamicProgrammingTSPTW dynamicProgrammingTSPTW = new DynamicProgrammingTSPTW(createTableTravelTime(mLocationList), numStart, mLocationList);
            listTSPTW = dynamicProgrammingTSPTW.getTour();
            minTravelTime = dynamicProgrammingTSPTW.getTourTravelTime();
            Log.d("VONGLAPMSTAY2", mStay + " " + minTravelTime);

            //Kiểm tra tổng thời gian lưu trú + thời gian di chuyển > thời gian đi trong một ngày
            if (mStay + minTravelTime > endHour - startHour) {
                continue;
            } else {
                //Lấy danh sách các địa điểm của cụm còn lại.
                if (cluster == 0) {
                    mLocationClusterDiff = new ArrayList<>(mLocationForCluster.get(1));
                } else {
                    mLocationClusterDiff = new ArrayList<>(mLocationForCluster.get(0));
                }
                Log.d("VONGLAP1", minTravelTime + " " + listTSPTW);
                break;
            }
        }
        Log.d("XONGVONGLAP1", minTravelTime + " " + listTSPTW + " " + mStay + " " + (endHour - startHour));
        //Sau khi lặp xong vòng lặp 1 thì tiếp tục vòng lặp 2.
        //tìm những địa điểm thỏa tổng thời gian lưu trú trong khoảng 0.9 - 1 * thời gian có thể đi trong ngày.
        while (mStay + minTravelTime < (int) ((endHour - startHour) * 0.9)) {

            int mintravelTime2 = Integer.MAX_VALUE;
            List<Integer> listTSPTW2 = new ArrayList<>();
            int minTravelTimeIndex = -1;
            Log.d("TIENXULYVONGLAP2", mLocationList.size() + "");

            //Tìm địa điểm có thời gian đi du lịch tối ưu nhất.
            for (int i = 0; i < mLocationClusterDiff.size(); i++) {
                ArrayList<LocationLocal> mLocationClusterTemp = new ArrayList<>(mLocationList);
                mLocationClusterTemp.add(mLocationClusterDiff.get(i));
                DynamicProgrammingTSPTW dynamicProgrammingTSPTW = new DynamicProgrammingTSPTW(createTableTravelTime(mLocationClusterTemp), numStart, mLocationClusterTemp);
                if (dynamicProgrammingTSPTW.getTourTravelTime() < mintravelTime2) {
                    Log.d("XULYXONGVONGLAP2", mLocationClusterTemp.size() + "");

                    mintravelTime2 = dynamicProgrammingTSPTW.getTourTravelTime();
                    minTravelTimeIndex = i;
                    listTSPTW2 = dynamicProgrammingTSPTW.getTour();
                }
            }

            Log.d("TIMXONGVONGLAP2", mintravelTime2 + " " + listTSPTW2 + " " + (int) ((endHour - startHour) * 0.9));

            int stayTemp;
            //kiểm tra có thỏa tìm được địa điểm nào ko?
            if (minTravelTimeIndex != -1) {
                LocationLocal verify = mLocationClusterDiff.get(minTravelTimeIndex);
                stayTemp = mStay + verify.getmStay();
                //tổng thời gian lưu trú + thời gian di chuyển có nhỏ hơn thời gian đi trong ngày không?
                if (stayTemp + mintravelTime2 <= endHour - startHour) {
                    mStay = stayTemp;
                    minTravelTime = mintravelTime2;
                    mLocationList.add(verify);

                    //xóa địa điểm thỏa khỏi mLocationListDiff
                    for (int i = 0; i < mLocationClusterDiff.size(); i++) {
                        if (verify.getmId() == mLocationClusterDiff.get(i).getmId()) {
                            mLocationClusterDiff.remove(i);

                            break;
                        }
                    }

                    //cập nhật getTour:
                    listTSPTW = listTSPTW2;
                    Log.d("VONGLAP2", minTravelTime + " " + listTSPTW + " " + mStay);
                    continue;
                }
            } else {
                //kết thúc vòng lặp 2
                Log.d("ERRORVONGLAP2", minTravelTime + " " + listTSPTW);
                //Lấy được danh sách các địa điểm thỏa: mLocationList,
                // minTravelTime: Thời gian di chuyển tối ưu nhất
                // listTSPTW: Các địa điểm thỏa.
                break;
            }
        }
        for (int i = 0; i < mLocationList.size(); i++) {
            Log.d("HOANTHANHVONGLAP", i + " " + mLocationList.get(i).getmName() + " ");

        }
        Integer[][] tsv = createTableTravelTime(mLocationList);
        /*Log.d("TSVVONGLAP", " " + tsv[2][3] + " " + mLocationList.get(3).getmOpen() + " " + mLocationList.get(3).getmClose() + " " + mLocationList.get(3).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[3][0] + " "+ mLocationList.get(0).getmOpen() + " " + mLocationList.get(0).getmClose()+ " " + mLocationList.get(0).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[0][7] + " "+ mLocationList.get(7).getmOpen() + " " + mLocationList.get(7).getmClose()+ " " + mLocationList.get(7).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[7][1] + " "+ mLocationList.get(1).getmOpen() + " " + mLocationList.get(1).getmClose()+ " " + mLocationList.get(1).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[1][5] + " "+ mLocationList.get(5).getmOpen() + " " + mLocationList.get(5).getmClose()+ " " + mLocationList.get(5).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[5][4] + " "+ mLocationList.get(4).getmOpen() + " " + mLocationList.get(4).getmClose()+ " " + mLocationList.get(4).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[4][6] + " "+ mLocationList.get(6).getmOpen() + " " + mLocationList.get(6).getmClose()+ " " + mLocationList.get(6).getmStay());
        Log.d("TSVVONGLAP", " " + tsv[6][2] + " "+ mLocationList.get(2).getmOpen() + " " + mLocationList.get(2).getmClose()+ " " + mLocationList.get(2).getmStay());
*/
        Log.d("YUYUUYYYUYUYYUYU1", listTSPTW + " ");
        Log.d("YUYUUYYYUYUYYUYU2", minTravelTime + "");
        return mLocationList;
    }

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

    @Override
    public void sortLocationWOpenHour(ArrayList<LocationLocal> locations) {
        Collections.sort(locations);
    }

    //PHẢI LỌC THỜI GIAN MỞ CỬA LỚN HƠN HOẶC BẰNG THỜI GIAN SỚM NHẤT MỞ CỬA CỦA CÁC ĐỊA ĐIỂM TRONG CỤM
    @Override
    public void filterClusterWTime(int startHour, int endHour, ArrayList<LocationLocal> mLocations, ArrayList<LocationLocal> mLocationsDiff, double centroidLat, double centroidLon) {
        //Danh sách các địa điểm không thỏa thời gian
        ArrayList<LocationLocal> mLocationsDelete = new ArrayList<>();
        ArrayList<LocationLocal> mLocationsAdd = new ArrayList<>();
        int mStay = 0;
        int realEndTime = startHour;
        for (int i = 0; i < mLocations.size(); i++) {
            LocationLocal location = mLocations.get(i);
            int endTime;
            //không thỏa thời gian đóng mở cửa
            if (location.getmOpen() > realEndTime + getDistance()) {
                LocationLocal local = getLocationDiff(mLocationsDiff, centroidLat, centroidLon, startHour, endHour, mLocations.size());

                //Không tìm được địa điểm khả điều kiện
                if (local == null) {
                    //Lấy địa điểm hiện tại
                    mStay += location.getmStay();
                    endTime = location.getmClose();
                } else {
                    //thêm địa điểm local vào location add
                    mStay += local.getmStay();
                    mLocationsAdd.add(local);
                    endTime = local.getmClose();

                }
            } else {
                mStay += location.getmStay();
                endTime = location.getmClose();
            }

            if (endTime > endHour) {
                realEndTime = endHour;
            } else {
                realEndTime = endTime;
            }
            if (mStay > endHour - startHour) {
                mLocationsDiff.add(location);
                mLocations.remove(location);
            }
        }


        //Nếu vẫn còn thời gian đi trong một ngày
        while (mStay < endHour - startHour) {
            LocationLocal location = null;
            //Lấy location từ cluster còn lại không xét điều kiện thời gian open
            getLocationDiff(mLocationsDiff, centroidLat, centroidLon, startHour, endHour, mLocations.size());
            if (location == null) {
                break;
            } else {
                mStay += location.getmStay();
                mLocationsAdd.add(location);
            }
        }

        //Thêm locationadd vào locationcluster
        mLocations.addAll(mLocationsAdd);
    }

    private double getDistanceEuclide(double latitude1, double longitude1, double latitude2, double longitude2) {
        return Math.sqrt((latitude1 - latitude2) * (latitude1 - latitude2)
                + (longitude1 - longitude2) * (longitude1 - longitude2));
    }


    //Hàm lấy một địa điểm ở cụm khác thỏa điều kiện
    LocationLocal getLocationDiff(ArrayList<LocationLocal> mLocationsLocal, double centroidLat, double centroiLon, int startHour, int endHour, int sizeOfCluster) {
        final double longitudeClone = new Double(centroiLon);
        final double latitudeClone = new Double(centroidLat);

        Collections.sort(mLocationsLocal, new Comparator<LocationLocal>() {
            @Override
            public int compare(LocationLocal o1, LocationLocal o2) {
                double distanceo1 = getDistanceEuclide(o1.getmLatitude(), o1.getmLongitude(), latitudeClone, longitudeClone);
                double distanceo2 = getDistanceEuclide(o2.getmLatitude(), o2.getmLongitude(), latitudeClone, longitudeClone);

                if (distanceo1 == distanceo2) {
                    return 0;
                } else if (distanceo1 > distanceo2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < mLocationsLocal.size(); i++) {
            LocationLocal locate = mLocationsLocal.get(i);
            if (locate.getmOpen() <= endHour) {
                // trung bình cộng các tọa độ của cụm
                centroidLat = (centroidLat * sizeOfCluster + locate.getmLatitude()) / (sizeOfCluster + 1);
                centroiLon = (centroiLon * sizeOfCluster + locate.getmLongitude()) / (sizeOfCluster + 1);
                sizeOfCluster++;
                mLocationsLocal.remove(locate);
                return locate;
            }
        }

        return null;
    }

    //Hàm lấy khoảng cách
    int getDistance() {
        return 0;
    }

    @Override
    public int getClusterContaintLocationStart(HashMap<Integer, HashSet<LocationLocal>> listLocationCLuster, LocationLocal mLocate) {
        //loop từng cụm
        for (Integer key : listLocationCLuster.keySet()) {
            //loop lấy duyệt các địa điểm mỗi cụm
            for (int i = 0; i < listLocationCLuster.get(key).size(); i++) {
                if (listLocationCLuster.get(key).contains(mLocate)) {
                    return key;
                }
            }
        }
        return 0;
    }

    @Override
    public void filterCluster() {

    }

    @Override
    public void TSPTWBuild() {

    }

    @Override
    public void getOpenCloseHour() {

    }

    @Override
    public void getStay() {

    }
}
