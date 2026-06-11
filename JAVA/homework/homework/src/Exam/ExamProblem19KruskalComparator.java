package Exam;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ExamProblem19KruskalComparator {
    static class Edge {
        int v1;
        int v2;
        int weight;

        Edge(int v1, int v2, int weight) {
            this.v1 = v1;
            this.v2 = v2;
            this.weight = weight;
        }
    }

    static class MyComp implements Comparator<Edge> {
        @Override
        public int compare(Edge e1, Edge e2) {
            return Integer.compare(e1.weight, e2.weight);
        }
    }

    public PriorityQueue<Edge> newMinHeap() {
        return new PriorityQueue<>(new MyComp());
    }
}
