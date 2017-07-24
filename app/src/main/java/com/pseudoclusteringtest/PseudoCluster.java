package com.pseudoclusteringtest;


import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class PseudoCluster {
    private List<PseudoClusterElement> clusterList;

    double distanceThreshold;


    PseudoCluster(double distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
        clusterList = new ArrayList<>();
    }


    int getTotalElementCount() {
        int size = clusterList.size();
        if (size == 0) {
            return 0;
        }
        else {
            int sum = 0;
            for (PseudoClusterElement cluster : clusterList) {
                sum += cluster.size();
            }
            return sum;
        }
    }


    void add(Location location) {
        int clusterCount = clusterList.size();

        // first location - create first cluster and add the location to it
        if(clusterCount == 0) {
            addCluster(location);
        }

        else {

            // try to place the location in an existing cluster
            for(int i = 0; i < clusterCount; i++) {
                // if distance to the mean of i-th cluster < threshold -> place the location
                // in that cluster
                if(location.distanceTo(clusterList.get(i).getMeanLocation()) < distanceThreshold) {
                    clusterList.get(i).add(location);
                    return;
                }

            }

            // if not placed -> requires a new cluster
            addCluster(location);

        }

    }


    private void addCluster(Location location) {
        PseudoClusterElement element = new PseudoClusterElement(location);
        clusterList.add(element);
    }


    PseudoClusterElement getLargestCluster() {
        int size = clusterList.size();
        if (size == 0) {
            return null;
        }
        else if (size == 1) {
            return clusterList.get(0);
        }
        else {
            int maxIndex = 0;
            int maxSize = clusterList.get(0).size();

            for (int i = 1; i < size; i++) {
                int tempSize = clusterList.get(i).size();
                if (tempSize > maxSize) {
                    maxIndex = i;
                    maxSize = tempSize;
                }
            }
            return clusterList.get(maxIndex);
        }
    }
}
