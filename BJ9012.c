#include <stdio.h>
#include <string.h>

int isVPS(char* str) {
    int count = 0;
    
    for (int i = 0; str[i] != '\0'; i++) {
        if (str[i] == '(') {
            count++;
        } else if (str[i] == ')') {
            count--;
            if (count < 0) {
                return 0;  // 닫는 괄호가 먼저 나옴
            }
        }
    }
    
    return (count == 0);  // 모든 괄호가 짝을 이루면 1, 아니면 0
}

int main() {
    int T;
    char str[51];  // 최대 50자 + null
    
    scanf("%d", &T);
    
    for (int i = 0; i < T; i++) {
        scanf("%s", str);
        
        if (isVPS(str)) {
            printf("YES\n");
        } else {
            printf("NO\n");
        }
    }
    
    return 0;
}

