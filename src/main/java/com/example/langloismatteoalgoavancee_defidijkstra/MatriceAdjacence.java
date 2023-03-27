package com.example.langloismatteoalgoavancee_defidijkstra;

import java.util.ArrayList;

public class MatriceAdjacence {

  double[][] matrice;

  public MatriceAdjacence(int taille) {
    matrice = new double[taille][taille];
  }

  public void setArc(int i, int j, double poids) {
    matrice[i][j] = poids;
  }
}
