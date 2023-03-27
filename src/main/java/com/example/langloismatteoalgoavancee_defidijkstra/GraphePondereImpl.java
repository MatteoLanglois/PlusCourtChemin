package com.example.langloismatteoalgoavancee_defidijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GraphePondereImpl implements GraphePondere {

  private ArrayList<Noeud> noeuds;
  private ArrayList<Arc> arcs;
  private Map<Noeud, ArrayList<Arc>> listeAdjacence;
  private MatriceAdjacence matriceAdjacence;

  public Set<Noeud> explored = new HashSet<>();
  public Map<Noeud, Noeud> predecesseurs = new HashMap<>();

  public GraphePondereImpl(ArrayList<Noeud> noeuds, ArrayList<Arc> arcs) {
    this.noeuds = noeuds;
    this.arcs = arcs;
    listeAdjacence = new HashMap<>();
    matriceAdjacence = new MatriceAdjacence(noeuds.size());
    for (Noeud noeud : noeuds) {
      ArrayList<Arc> arcsAdjacents = new ArrayList<>();
      for (Arc arc : arcs) {
        if (arc.getA() == noeud || arc.getB() == noeud) {
          arcsAdjacents.add(arc);
        }
      }
      listeAdjacence.put(noeud, arcsAdjacents);
    }
    for (Arc arc : arcs) {
      matriceAdjacence.setArc(arc.getA().getId(), arc.getB().getId(), arc.getPoids());
    }
  }

  @Override
  public void ajouterSommet(Noeud sommet) {
    noeuds.add(sommet);
  }

  @Override
  public void ajouterArete(Noeud sommet1, Noeud sommet2, double poids) {
    Arc nouveau = new Arc(sommet1, sommet2, poids);
    arcs.add(nouveau);
    listeAdjacence.get(sommet1).add(nouveau);
    listeAdjacence.get(sommet2).add(nouveau);
  }

  @Override
  public void enleveSommet(Noeud sommet) {
    noeuds.remove(sommet);
    listeAdjacence.remove(sommet);
  }

  @Override
  public void enleveArc(Arc arc) {
    arcs.remove(arc);
    listeAdjacence.get(arc.getA()).remove(arc);
    listeAdjacence.get(arc.getB()).remove(arc);
  }

  @Override
  public Arc getArc(Noeud sommet1, Noeud sommet2) {
    List<Arc> list = listeAdjacence.get(sommet1);
    Arc sortie = null;
    for (Arc arc : list) {
      if (arc.getA() == sommet1 || arc.getB() == sommet1 && arc.getB() == sommet2 || arc.getA() == sommet2) {
        sortie = arc;
      }
    }
    return sortie;
  }

  @Override
  public Noeud getSommet(int id) {
    return noeuds.get(id);
  }


  @Override
  public double getPoids(Noeud sommet1, Noeud sommet2) {
    return getArc(sommet1, sommet2).getPoids();
  }

  @Override
  public ArrayList<Noeud> getSommets() {
    return noeuds;
  }

  @Override
  public ArrayList<Arc> getArcs() {
    return arcs;
  }

  @Override
  public ArrayList<Noeud> getVoisins(Noeud sommet) {
    ArrayList<Noeud> listeAdjacenceSommet = new ArrayList<>();
    for (Arc arc : listeAdjacence.get(sommet)) {
      if (sommet == arc.getA()) {
        listeAdjacenceSommet.add(arc.getB());
      }
    }
    return listeAdjacenceSommet;
  }

  @Override
  public double AEtoileListeAdjacence(Noeud sommet1, Noeud sommet2) {
    Map<Noeud, Double> distances = new HashMap<>();
    for (Noeud noeud : noeuds) {
      distances.put(noeud, Double.POSITIVE_INFINITY);
    }
    distances.put(sommet1, 0.);
    ArrayList<Noeud> candidats = new ArrayList<>();
    candidats.add(sommet1);
    explored.clear();

    Noeud noeud = sommet1;
    while (!candidats.isEmpty() && sommet2 != noeud) {
      noeud = TrouverProchainNoeud(candidats, distances, sommet2);
      if (noeud == null) {
        break;
      }
      ArrayList<Noeud> s1 = new ArrayList<>();
      for (Noeud noeudPossible : getVoisins(noeud)) {
        if (!explored.contains(noeudPossible)) {
          s1.add(noeudPossible);
        }
      }
      for (Noeud ProchainNoeudPossible : s1) {
        if (!candidats.contains(ProchainNoeudPossible)) {
            candidats.add(ProchainNoeudPossible);
        }
        if (distances.get(ProchainNoeudPossible) > distances.get(noeud) + getArc(noeud, ProchainNoeudPossible).distance) {
          distances.replace(ProchainNoeudPossible, distances.get(noeud) + getArc(noeud, ProchainNoeudPossible).distance);
          predecesseurs.put(ProchainNoeudPossible, noeud);
        }
      }
      candidats.remove(noeud);
      explored.add(noeud);
    }
    return distances.get(sommet2);
  }

  private Noeud TrouverProchainNoeud(ArrayList<Noeud> candidats, Map<Noeud, Double> distances, Noeud sommet2) {
    double mini = Double.POSITIVE_INFINITY;
    Noeud sommet = null;
    for (Noeud noeud : candidats) {
      if (distances.get(noeud) + noeud.distanceChebyshev(sommet2) < mini) {
        mini = distances.get(noeud) + noeud.distanceChebyshev(sommet2);
        sommet = noeud;
      }
    }
    return sommet;
  }
}
