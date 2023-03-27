package com.example.langloismatteoalgoavancee_defidijkstra;

import java.util.ArrayList;

public interface GraphePondere {
  void ajouterSommet(Noeud sommet);

  void ajouterArete(Noeud sommet1, Noeud sommet2, double poids);

  void enleveSommet(Noeud sommet);

  void enleveArc(Arc arc);

  Arc getArc(Noeud sommet1, Noeud sommet2);

  Noeud getSommet(int id);

  double getPoids(Noeud sommet1, Noeud sommet2);

  ArrayList<Noeud> getSommets();
  ArrayList<Arc> getArcs();

  ArrayList<Noeud> getVoisins(Noeud sommet);

  double AEtoileListeAdjacence(Noeud sommet1, Noeud sommet2);

}
