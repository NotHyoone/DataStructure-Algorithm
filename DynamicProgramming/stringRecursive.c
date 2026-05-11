#include <stdio.h>
#include <string.h>

#define Ci 1
#define Cd 1
#define Cu 2

#define MIN(a,b,c) ( (a) < (b) ? ( (a) < (c) ? (a) : (c) ) : ( (b) < (c) ? (b) : (c) ) )

int editDistanceRecursive(char* X, char* Y, int i, int j) {
    // 기저 조건: 한쪽 문자열이 비어있는 경우
    if (i == 0) return j * Ci;
    if (j == 0) return i * Cd;

    int diff;
    if (X[i - 1] == Y[j - 1]) {
        diff = 0; // 문자가 같으면 비용 없음
    } else {
        diff = Cu; // 문자가 다르면 교체 비용
    }

    return MIN(
        editDistanceRecursive(X, Y, i-1, j) + Cd, 
        editDistanceRecursive(X, Y, i, j-1) + Ci,
        editDistanceRecursive(X, Y, i-1, j-1) + diff
    );
}

int main() {
    char X[] = "bbabb";
    char Y[] = "abaa";

    printf("분할 정복 결과: %d\n", editDistanceRecursive(X, Y, strlen(X), strlen(Y)));
    return 0;
}