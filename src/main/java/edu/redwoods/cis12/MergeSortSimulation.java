package edu.redwoods.cis12;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Random;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MergeSortSimulation extends AlgorithmSimulation {
    private final Random obj;
    public MergeSortSimulation(AlgSimulatorController asc) {
        super("Merge Sort", "mergeSortControls.fxml", asc);
        obj = new Random();
    }

    private String randomHexColor() {
        int rand_num = obj.nextInt(0xfffff + 1);
        return String.format("#%06x", rand_num);
    }

    private int compare(Node node1, Node node2) {
        int v1 = Integer.parseInt(((Button)node1).getText());
        int v2 = Integer.parseInt(((Button)node2).getText());
        return Integer.compare(v1, v2);
    }

    private void merge(int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        List<Node> gn = gridPane.getChildren();
        String[] arr = new String[gn.size()];

        List<Node> L = gn.subList(l, l + n1);
        List<Node> R = gn.subList(m + 1, m + n2 + 1);

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if(compare(L.get(i), R.get(j)) < 1) {
                arr[k] = ((Button)L.get(i)).getText();
                i++;
            } else {
                arr[k] = ((Button)R.get(j)).getText();
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = ((Button)L.get(i)).getText();
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = ((Button)R.get(j)).getText();
            j++;
            k++;
        }

        int end = (l + n1 + n2);
        String color = this.randomHexColor();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Button b;
                for (int s=l; s < end; s++) {
                    b = (Button)gn.get(s);
                    b.setText(arr[s]);
                    b.setStyle(String.format("-fx-background-color: %s", color));
                }
            }
        });
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeSort(int left, int right) {
        try {
            if (left < right) {
                String color = this.randomHexColor();
                for(int i = left; i <= right; i++) {
                    gridPane.getChildren().get(i).setStyle(String.format("-fx-background-color: %s", color));
                }
                TimeUnit.SECONDS.sleep(1);

                int middle = left + (right - left) / 2;
                mergeSort(left, middle);
                mergeSort(middle + 1, right);

                merge(left,middle,right);
            }
        } catch (InterruptedException ie) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Interrupted");
            a.setContentText("Please enter an integer to search for in the \"Search For\" text-field.");
            a.show();
        }
    }

    @Override
    public void simulate() {
        try {
            new Thread(() -> {
                mergeSort(0,gridPane.getChildren().size() - 1);
            }).start();
        } catch(NumberFormatException|NullPointerException n) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid Search");
            a.setContentText("Please enter an integer to search for in the \"Search For\" text-field.");
            a.show();
        }
    }
}