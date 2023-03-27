package com.example.langloismatteoalgoavancee_defidijkstra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

  static final NumberAxis xAxis = new NumberAxis(2.15, 2.55, 0.000001);
  static final NumberAxis yAxis = new NumberAxis(48.79, 48.98, 0.000000001);
  static final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
  static final XYChart.Series<Number, Number> all = new XYChart.Series<>();
  static final XYChart.Series<Number, Number> explored = new XYChart.Series<>();
  static final XYChart.Series<Number, Number> path = new XYChart.Series<>();
  static Label distance = new Label();
  static Label noeudsExpl = new Label();
  static TextField idDepartText = new TextField();
  static TextField idArriveeText = new TextField();
  static Button calc = new Button("Calculer");
  static Label tempsExec = new Label();


  @Override
  public void start(Stage stage) throws Exception {
    all.setName("Noeuds");
    explored.setName("Noeuds explorés");
    path.setName("Chemin le plus court");

    System.out.println("Chargement des donnees...");

    GraphePondereImpl graphePondere = initialiseDonnees();

    System.out.println("Données chargees");

    int idDepart = 23;
    int idArrivee = 8555;

    calc.setOnAction(e -> {
      graphePondere.explored.clear();
      graphePondere.predecesseurs.clear();
      long startTime = System.nanoTime();
      graphePondere.AEtoileListeAdjacence(graphePondere.getSommet(Integer.parseInt(idDepartText.getText())), graphePondere.getSommet(Integer.parseInt(idArriveeText.getText())));
      long endTime = System.nanoTime();
      tempsExec.setText("Temps d'execution :\n" + (endTime - startTime) / 1000000 + "ms");
      updateGraph(graphePondere, Integer.parseInt(idDepartText.getText()), Integer.parseInt(idArriveeText.getText()));
    });

    VBox info = new VBox();
    Label distanceLabel = new Label("Distance : ");
    Label noeudsExplLabel = new Label("Noeuds explorés : ");
    info.getChildren().add(noeudsExplLabel);
    info.getChildren().add(noeudsExpl);
    info.getChildren().add(distanceLabel);
    info.getChildren().add(distance);
    info.getChildren().add(idDepartText);
    info.getChildren().add(idArriveeText);
    info.getChildren().add(calc);
    info.getChildren().add(tempsExec);
    scatterChart.styleProperty().set("-fx-width: 900px; -fx-height: 800px;");

    HBox root = new HBox(scatterChart);
    root.getChildren().add(info);
    scatterChart.setMinSize(950, 750);


    Scene scene = new Scene(root, 1050, 750);

    stage.setScene(scene);
    stage.show();
    /*
    System.out.println("Calcul du chemin le plus court...");
    double dist = graphePondere.AEtoile(graphePondere.getSommet(idDepart), graphePondere.getSommet(idArrivee));
    System.out.println("Chemin le plus court trouve");

    System.out.println("Affichage du graphe");
    updateGraph(graphePondere, idDepart, idArrivee);
    System.out.println("Graphe affiche");
     */
  }

  private void updateGraph(GraphePondereImpl graphePondere, int idDepart, int idArrivee) {
    ArrayList<Noeud> path = new ArrayList<>();
    double dist = 0;
    double dist2 = 0;
    Noeud noeudCourant = graphePondere.getSommet(idArrivee);
    while (noeudCourant != graphePondere.getSommet(idDepart)) {
      dist += Application.distanceLongLat(noeudCourant, graphePondere.predecesseurs.get(noeudCourant));
      dist2 += graphePondere.getArc(noeudCourant, graphePondere.predecesseurs.get(noeudCourant)).distance;
      path.add(noeudCourant);
      noeudCourant = graphePondere.predecesseurs.get(noeudCourant);
    }

    for (Noeud noeud : graphePondere.getSommets()) {
      if (path.contains(noeud)) {
        Application.path.getData().add(new XYChart.Data<>(noeud.getX(), noeud.getY()));
      } else if (graphePondere.explored.contains(noeud)) {
        Application.explored.getData().add(new XYChart.Data<>(noeud.getX(), noeud.getY()));
      } else {
        Application.all.getData().add(new XYChart.Data<>(noeud.getX(), noeud.getY()));
      }

    }
    scatterChart.getData().add(Application.all);
    scatterChart.getData().add(Application.explored);
    scatterChart.getData().add(Application.path);

    distance.setText(Math.round(dist2) + "m");
    noeudsExpl.setText(graphePondere.explored.size() + " noeuds");

    System.out.println("Nombres de noeuds : " + graphePondere.getSommets().size());
    System.out.println("Nombres de noeuds explorés / nombres de noeuds : "
        + Math.round((double) graphePondere.explored.size()
        / graphePondere.getSommets().size() * 100) + "%");
    System.out.println("Distance : " + Math.round(dist) + "m");
    System.out.println("Distance 2 : " + Math.round(dist2) + "m");
    System.out.println("Taille du chemin : " + path.size());
  }


  static GraphePondereImpl initialiseDonnees() throws Exception {
    ArrayList<Noeud> noeuds = readPoint();
    ArrayList<Arc> arcs = readArc(noeuds);

    return new GraphePondereImpl(noeuds, arcs);
  }

  private static ArrayList<Arc> readArc(ArrayList<Noeud> noeuds) throws Exception {
    FileReader fileArc = new FileReader(
        "C:\\Users\\mamac\\Documents\\Cours\\L2Info\\CoursMatteo\\Semestre2\\AlgoAvancee"
            + "\\TP\\LangloisMatteoAlgoAvancee_DefiDijsktra\\src\\main\\java\\com\\example"
            + "\\langloismatteoalgoavancee_defidijkstra\\data\\paris_arcs.csv");
    BufferedReader bufferedReaderArc = new BufferedReader(fileArc);

    ArrayList<Arc> arcs = new ArrayList<>();
    String line;
    while ((line = bufferedReaderArc.readLine()) != null) {
      String[] values = line.split("\t");
      int id1 = Integer.parseInt(values[0].substring(1));
      int id2 = Integer.parseInt(values[1]);
      double distance = Double.parseDouble(values[2]);
      int poids = Integer.parseInt(values[3].substring(0, values[3].length() - 1));

      Noeud noeud1 = null;
      Noeud noeud2 = null;
      for (Noeud noeud : noeuds) {
        if (noeud.getId() == id1) {
          noeud1 = noeud;
        }
        if (noeud.getId() == id2) {
          noeud2 = noeud;
        }
      }

      arcs.add(new Arc(noeud1, noeud2, poids, distance));
    }

    bufferedReaderArc.close();
    fileArc.close();
    return arcs;
  }

  private static ArrayList<Noeud> readPoint() throws Exception {
    FileReader filePoint = new FileReader(
        "C:\\Users\\mamac\\Documents\\Cours\\L2Info\\CoursMatteo\\Semestre2\\AlgoAvancee"
            + "\\TP\\LangloisMatteoAlgoAvancee_DefiDijsktra\\src\\main\\java\\com\\example"
            + "\\langloismatteoalgoavancee_defidijkstra\\data\\paris_noeuds.csv");
    BufferedReader bufferedReaderPoint = new BufferedReader(filePoint);

    ArrayList<Noeud> noeuds = new ArrayList<>();
    String line;
    while ((line = bufferedReaderPoint.readLine() )!= null) {
      String[] values = line.split("\t");
      int id = Integer.parseInt(values[0].substring(1));
      double x = Double.parseDouble(values[1]);
      double y = Double.parseDouble(values[2].substring(0, values[2].length() - 1));

      noeuds.add(new Noeud(id, x, y));
    }
    bufferedReaderPoint.close();
    filePoint.close();
    return noeuds;
  }

  private static double distanceLongLat(Noeud A, Noeud B) {
    double latA = A.getX();
    double longA = A.getY();
    double latB = B.getX();
    double longB = B.getY();

    double rayonTerre = 6371;
    double dLat = Math.toRadians(latB - latA);
    double dLong = Math.toRadians(longB - longA);
    latA = Math.toRadians(latA);
    latB = Math.toRadians(latB);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.sin(dLong / 2) * Math.sin(dLong / 2) * Math.cos(latA) * Math.cos(latB);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return rayonTerre * c * 1000;
  }
  public static void main(String[] args) {
    launch();
  }
}