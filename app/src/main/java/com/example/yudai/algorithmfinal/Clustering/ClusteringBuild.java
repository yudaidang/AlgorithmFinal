package com.example.yudai.algorithmfinal.Clustering;

import android.util.Log;
import android.util.Pair;

import com.example.yudai.algorithmfinal.Entity.LocationLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ClusteringBuild implements ClusteringContract {

    private static final int NAME_WEKA = 0;
    private static final int LATITUDE = 1;
    private static final int LONGITUDE = 2;

    //Danh sách địa điểm theo cụm
    private HashMap<Integer, HashSet<LocationLocal>> mLocationsCluster = new HashMap<>();

    private ArrayList<LocationLocal> mLocates;
    public HashMap<Integer, Pair<Double, Double>> mCentroids = new HashMap<>();

    public ClusteringBuild(ArrayList<LocationLocal> mLocates) {
        this.mLocates = mLocates;
    }

    @Override
    public HashMap<Integer, HashSet<LocationLocal>> flowMain() {
        Instances dataset = getMemberCluster();
        SimpleKMeans kMeans = algorithmClustering(dataset);
        return setDataSetForCluster(kMeans);

    }

    public Pair<Double, Double> getCentroid(int key){
        return mCentroids.get(key);
    }

    @Override
    public Instances getMemberCluster() {
        ArrayList<Attribute> listAtr = new ArrayList();
        listAtr.add(new Attribute("Hoang", (FastVector) null));
        listAtr.add(new Attribute("Latitude"));
        listAtr.add(new Attribute("Longitude"));
        Instances dataset = new Instances("locationDataset", listAtr, mLocates.size());
        for (int i = 0; i < mLocates.size(); i++) {
            Instance inst = new DenseInstance(3);
            inst.setDataset(dataset);
            inst.setValue(NAME_WEKA, mLocates.get(i).getmName());
            inst.setValue(LATITUDE, mLocates.get(i).getmLatitude());
            inst.setValue(LONGITUDE, mLocates.get(i).getmLongitude());
            dataset.add(inst);
        }
        return dataset;
    }

    @Override
    public HashMap<Integer, HashSet<LocationLocal>> setDataSetForCluster(SimpleKMeans kMeans) {
        int[] assignments2 = new int[0];
        try {
            assignments2 = kMeans.getAssignments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int k = 0;
        for (int clusterNum : assignments2) {
            HashSet<LocationLocal> mListClone = new HashSet<>();

            //có cluster này chưa?
            if (mLocationsCluster.containsKey(clusterNum)) {
                mListClone = mLocationsCluster.get(clusterNum);
            }
            mListClone.add(mLocates.get(k));
            mLocationsCluster.put(clusterNum, mListClone);
            k++;
        }
        return mLocationsCluster;
    }

    @Override
    public SimpleKMeans algorithmClustering(Instances dataset) {
        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(new int[]{0});
        try {
            removeFilter.setInputFormat(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instances newData = null;
        try {
            newData = Filter.useFilter(dataset, removeFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataset.setClassIndex(0);

        SimpleKMeans kMeans = new SimpleKMeans();

        try {
            kMeans.setNumClusters(2);
            kMeans.setSeed(10);
            kMeans.setPreserveInstancesOrder(true);
            kMeans.setDistanceFunction(new EuclideanDistance());
            kMeans.buildClusterer(newData);
            int[] assignments = kMeans.getAssignments();
            int i = 0;
            for (int clusterNum : assignments) {
                Log.d("YUYUYUYUYUYUYU", "Instance " + i + " Cluster " + clusterNum);
                i++;
            }

            Instances incentroid = kMeans.getClusterCentroids();
            mCentroids.clear();
            for (int m = 0; m < incentroid.numInstances(); m++) {
                Instance in = incentroid.get(m);
                mCentroids.put(m, new Pair<>(in.value(0), in.value(1)));
            }

            ClusterEvaluation eval = new ClusterEvaluation();
            //thiết lập thuật toán
            eval.setClusterer(kMeans);

            //insert dataset
            eval.evaluateClusterer(dataset);
            Log.d("Kết quả sau khi gom cụm", eval.clusterResultsToString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return kMeans;
    }
}
