#include <stdio.h>
#include <stdlib.h>

// 배열기반 스택 구현
typedef struct Node {
    int Data;
} Node;

typedef struct ArrayStack { 
    int Capacity;
    int Top;
    Node* Nodes;
} ArrayStack;

void AS_CreateStack(ArrayStack** Stack, int Capacity) {
    // 스택을 자유 저장소에 생성
    (*Stack) = (ArrayStack*)malloc((sizeof(ArrayStack)));
    // 입력된 Capacity만큼 노드를 자유 저장소에 생성
    (*Stack)->Nodes = (Node*)malloc(sizeof(Node)*Capacity);
    // Capacity 및 Top 초기화
    (*Stack)->Capacity = Capacity;
    (*Stack)->Top = -1;
}

void AS_DestroyStack(ArrayStack* Stack) {
    // 노드를 자유 저장소에서 해제
    free(Stack->Nodes);
    // 스택을 자유 저장소에서 해제
    free(Stack);
}

void AS_Push(ArrayStack* Stack, int Data) {
    Stack->Top++;
    Stack->Nodes[Stack->Top].Data = Data;
}

void AS_Pop(ArrayStack* Stack) {
    int Position = Stack->Top--;
    return Stack->Nodes[Stack->Top].Data;
}

int AS_Top(ArrayStack* Stack) {
    return Stack->Nodes[Stack->Top].Data;
}

int AS_GetSize(ArrayStack* Stack) {
    return Stack->Top+1;
}

int AS_IsEmpty(ArrayStack* Stack) {
    return (Stack->Top == -1);
}

int AS_IsFull(ArrayStack* Stack) {
    return (Stack->Top == Stack->Capacity);
}