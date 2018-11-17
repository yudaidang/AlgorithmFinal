package com.example.yudai.algorithmfinal.Clustering;

import com.example.yudai.algorithmfinal.Entity.LocationLocal;

import java.util.HashMap;
import java.util.HashSet;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public interface ClusteringContract {

    HashMap<Integer, HashSet<LocationLocal>> flowMain();

    Instances getMemberCluster();

    HashMap<Integer, HashSet<LocationLocal>> setDataSetForCluster(SimpleKMeans kMeans);

    SimpleKMeans algorithmClustering(Instances dataset);
}
