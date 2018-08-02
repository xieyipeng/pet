
package com.example.a13834598889.lovepets.Fragments_Serch.clusterutil.clustering.algo;


import com.example.a13834598889.lovepets.Fragments_Serch.clusterutil.clustering.Cluster;
import com.example.a13834598889.lovepets.Fragments_Serch.clusterutil.clustering.ClusterItem;

import java.util.Collection;
import java.util.Set;

/**
 * Logic for computing clusters
 */
public interface Algorithm<T extends ClusterItem> {
    void addItem(T item);

    void addItems(Collection<T> items);

    void clearItems();

    void removeItem(T item);

    Set<? extends Cluster<T>> getClusters(double zoom);

    Collection<T> getItems();
}