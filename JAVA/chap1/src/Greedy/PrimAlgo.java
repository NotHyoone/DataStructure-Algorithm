package Greedy;

import java.util.ArrayList;
import java.util.List;

public class PrimAlgo {

    static final int INF = Integer.MAX_VALUE;

    // 간선을 표현하는 내부 클래스
    static class Edge {
        int u, v, weight;
        Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
        @Override
        public String toString() {
            return "(" + u + " - " + v + ", weight: " + weight + ")";
        }
    }

    /**
     * Prim 알고리즘: 최소 신장 트리(MST)를 구한다.
     * @param n   정점 수 (1-indexed)
     * @param W   가중치 인접 행렬 (W[i][j] = 연결 없으면 INF)
     * @return    MST를 구성하는 간선 집합
     */
    static List<Edge> prim(int n, int[][] W) {
        int[] nearest  = new int[n + 1];   // nearest[i]: i와 가장 가까운 MST 내 정점
        int[] distance = new int[n + 1];   // distance[i]: i까지의 현재 최소 거리
        List<Edge> F = new ArrayList<>();  // 결과 간선 집합

        // 초기화: 정점 1을 시작점으로 설정
        for (int i = 2; i <= n; i++) {
            nearest[i]  = 1;
            distance[i] = W[1][i];
        }

        // n-1개의 간선을 선택
        for (int iter = 0; iter < n - 1; iter++) {
            int min = INF;
            int candidate = -1;

            // 아직 MST에 포함되지 않은 정점 중 거리가 최소인 정점 선택
            for (int i = 2; i <= n; i++) {
                if (distance[i] > 0 && distance[i] < min) {
                    min = distance[i];
                    candidate = i;
                }
            }

            if (candidate == -1) break; // 연결 그래프가 아닌 경우

            // 선택된 간선을 MST에 추가
            F.add(new Edge(nearest[candidate], candidate, distance[candidate]));
            distance[candidate] = -1; // MST에 포함됨을 표시

            // 나머지 정점의 거리 갱신
            for (int i = 2; i <= n; i++) {
                if (W[candidate][i] != INF && W[candidate][i] < distance[i]) {
                    nearest[i]  = candidate;
                    distance[i] = W[candidate][i];
                }
            }
        }
        return F;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] W = new int[n + 1][n + 1];

        // 모든 간선을 INF로 초기화
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                W[i][j] = (i == j) ? 0 : INF;

        // 무방향 그래프 간선 설정
        int[][] edges = {
            {1, 2, 1}, {1, 3, 3},
            {2, 3, 3}, {2, 4, 6},
            {3, 4, 4}, {3, 5, 2},
            {4, 5, 5}
        };
        for (int[] e : edges) {
            W[e[0]][e[1]] = e[2];
            W[e[1]][e[0]] = e[2];
        }

        List<Edge> mst = prim(n, W);

        System.out.println("=== Prim MST 결과 ===");
        int total = 0;
        for (Edge e : mst) {
            System.out.println(e);
            total += e.weight;
        }
        System.out.println("총 가중치: " + total);
    }
}
