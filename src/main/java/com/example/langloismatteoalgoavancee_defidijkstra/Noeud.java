package com.example.langloismatteoalgoavancee_defidijkstra;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Noeud {

  private int id;
  private double posX = 0;
  private double posY = 0;

  Noeud(double x, double y) {
    posX = x;
    posY = y;
    id = 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  Noeud(int id, double x, double y) {
    this.id = id;
    posX = x;
    posY = y;
  }

  Noeud(Noeud a) {
    posX = a.getX();
    posY = a.getY();
  }

  public double getX() {
    return posX;
  }

  public double getY() {
    return posY;
  }

  public void setX(double x) {
    posX = x;
  }

  public void setY(double y) {
    posY = y;
  }

  public boolean equals(Noeud B) {
    return posX == B.getX() && posY == B.getY();
  }

  public double volOiseau(Noeud B) {
    return sqrt(pow(this.getX() - B.getX(), 2) + pow(this.getY() - B.getY(), 2));
  }

  public double distanceManhattan(Noeud B) {
    return Math.abs(this.getX() - B.getX()) + Math.abs(this.getY() - B.getY());
  }

  public double distanceEuclidienne(Noeud B) {
    return sqrt(pow(this.getX() - B.getX(), 2) + pow(this.getY() - B.getY(), 2));
  }

  public double distanceChebyshev(Noeud B) {
    return Math.max(Math.abs(this.getX() - B.getX()), Math.abs(this.getY() - B.getY()));
  }

}
