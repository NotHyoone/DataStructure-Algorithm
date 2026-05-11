#include <stdio.h>
#include <string.h>

#define Ci 1
#define Cd 1
#define Cu 2

#define MIN(a,b,c) ( (a) < (b) ? ( (a) < (c) ? (a) : (c) ) : ( (b) < (c) ? (b) : (c) ) )

int editDistanceDP(char* X, char* Y) {
    int n = strlen(X);
    int m = strlen(Y);
    int D[n+1][m+1];

    // 초기값 설정(기저 조건)
    for(int i=0; i <= n; i++) D[i][0] = i * Cd;
    for(int j=0; j <= n; j++) D[0][j] = j * Ci;

    // 테이블 채우기(Bottom-up)
    for(int i=0; i <= n; i++) {
        for (int j=1; j <= m; j++) {
            int diff = (X[i-1] == Y[j-1]) ? 0 : Cu;

            // D[i][j] = min3(D[i-1][j] + Cd, D[i][j-1] + Ci, D[i-1][j-1] + diff);
            D[i][j] = MIN(D[i-1][j] + Cd, D[i][j-1]+Ci, D[i-1][j-1]+diff);
        }
    }
    return D[n][m];
}

int main() {
    char X[] = "bbabb";
    char Y[] = "abaa";

    printf("동적 프로그래밍 결과: %d\n", editDistanceDP(X,Y));
    return 0;
}