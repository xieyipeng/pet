/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.example.a13834598889.lovepets.Fragments_Serch.clusterutil.clustering;


import com.baidu.mapapi.model.LatLng;

import java.util.Collection;

/**
 * A collection of ClusterItems that are nearby each other.
 */
public interface Cluster<T> {
    public LatLng getPosition();

    Collection<T> getItems();

    int getSize();
}