package Greedy;

import java.util.*;

public class PrimMinHeap {

    static final int INF = Integer.MAX_VALUE;

    // 간선을 표현하는 클래스
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // MinHeap에서 사용되는 정점 정보 클래스
    static class Vertex implements Comparable<Vertex> {
        int vertex;
        int distance;

        Vertex(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(this.distance, other.distance);
        }

        @Override
        public String toString() {
            return "V:" + vertex + ",D:" + distance;
        }
    }

    /**
     * MinHeap을 사용한 Prim 알고리즘
     * 시간복잡도: O(E log V)
     * @param n                그래프의 정점 수
     * @param adjacencyList    인접 리스트
     * @return                 MST의 총 가중치와 간선 정보
     */
    static class MST {
        int totalWeight;
        List<String> edges;

        MST(int totalWeight, List<String> edges) {
            this.totalWeight = totalWeight;
            this.edges = edges;
        }
    }

    static MST primWithMinHeap(int n, List<List<Edge>> adjacencyList) {
        boolean[] inMST = new boolean[n + 1];     // MST에 포함된 정점 표시
        int[] distance = new int[n + 1];          // 각 정점까지의 최소 거리
        int[] parent = new int[n + 1];            // MST에서의 부모 정점
        List<String> resultEdges = new ArrayList<>();
        int totalWeight = 0;

        // 초기화
        Arrays.fill(distance, INF);
        Arrays.fill(parent, -1);

        // MinHeap 생성 (Priority Queue)
        PriorityQueue<Vertex> minHeap = new PriorityQueue<>();

        // 시작 정점을 1로 설정
        distance[1] = 0;
        minHeap.offer(new Vertex(1, 0));

        // 정점을 하나씩 MST에 추가
        while (!minHeap.isEmpty()) {
            Vertex current = minHeap.poll();
            int u = current.vertex;

            // 이미 MST에 포함된 정점이면 스킵
            if (inMST[u]) continue;

            // 현재 정점을 MST에 추가
            inMST[u] = true;

            // 부모 정점과의 간선을 결과에 추가 (시작 정점 제외)
            if (parent[u] != -1) {
                resultEdges.add("(" + parent[u] + " - " + u + ", weight: " + distance[u] + ")");
                totalWeight += distance[u];
            }

            // 현재 정점의 인접 정점들 처리
            for (Edge edge : adjacencyList.get(u)) {
                int v = edge.to;
                int weight = edge.weight;

                // MST에 미포함된 정점이고, 새로운 가중치가 더 작으면 갱신
                if (!inMST[v] && weight < distance[v]) {
                    distance[v] = weight;
                    parent[v] = u;
                    minHeap.offer(new Vertex(v, weight));
                }
            }
        }

        return new MST(totalWeight, resultEdges);
    }

    public static void main(String[] args) {
        int n = 5;

        // 인접 리스트 생성
        List<List<Edge>> adjacencyList = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        // 무방향 그래프 간선 추가
        int[][] edges = {
            {1, 2, 1}, {1, 3, 3},
            {2, 3, 3}, {2, 4, 6},
            {3, 4, 4}, {3, 5, 2},
            {4, 5, 5}
        };

        for (int[] e : edges) {
            adjacencyList.get(e[0]).add(new Edge(e[1], e[2]));
            adjacencyList.get(e[1]).add(new Edge(e[0], e[2]));
        }

        // Prim 알고리즘 실행
        MST result = primWithMinHeap(n, adjacencyList);

        System.out.println("=== MinHeap을 사용한 Prim MST 결과 ===");
        for (String edge : result.edges) {
            System.out.println(edge);
        }
        System.out.println("총 가중치: " + result.totalWeight);
        System.out.println("\n시간복잡도: O(E log V)");
        System.out.println("공간복잡도: O(V + E)");
    }
}
