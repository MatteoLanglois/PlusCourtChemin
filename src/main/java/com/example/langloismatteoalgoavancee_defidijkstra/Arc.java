package com.example.langloismatteoalgoavancee_defidijkstra;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Arc {
  Noeud A;
  Noeud B;

  double poids;

  double distance;

  public Arc(Noeud a, Noeud b, double poids) {
    this.A = a;
    this.B = b;
    this.poids = poids;
  }

  public Arc(Noeud a, Noeud b, double poids, double dist) {
    A = a;
    B = b;
    this.poids = poids;
    this.distance = dist;
  }

  public Noeud getA() {
    return A;
  }

  public void setA(Noeud a) {
    A = a;
  }

  public Noeud getB() {
    return B;
  }

  public void setB(Noeud b) {
    B = b;
  }

  public double getPoids() {
    return poids;
  }

  public void setPoids(double poids) {
    this.poids = poids;
  }
}
