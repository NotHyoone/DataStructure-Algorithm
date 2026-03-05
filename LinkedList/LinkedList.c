// LinkedList 구현
#include <stdio.h>
#include <stdlib.h>

typedef int ElementType;    // 요소 타입 정의

// 구조체 생성 
typedef struct Node {
    ElementType Data;   // 데이터
    struct Node* NextNode;  // 다음 노드를 가리키는 포인터
} Node;

Node* SLL_CreateNode(ElementType NewData);
void SLL_DestroyNode(Node* Node);
void SLL_AppendNode(Node** Head, Node* NewNode);
Node* SLL_GetNodeAt(Node* Head, int Location);
void SLL_RemoveNode(Node** Head, Node* Remove);
void SLL_InsertAfter(Node* Current, Node* NewNode);
void SLL_InsertNewHead(Node** Head, Node* NewNode);
int SLL_GetNodeCount(Node* Head);


// 테스트 코드
int main(void) {
    int i = 0;
    int Count = 0;
    Node* List = NULL;
    Node* Current = NULL;
    Node* NewNode = NULL;

    for (i=0; i<5; i++) {
        NewNode = SLL_CreateNode(i);
        SLL_AppendNode(&List, NewNode);
    }

    NewNode = SLL_CreateNode(-1);
    SLL_InsertNewHead(&List, NewNode);

    NewNode = SLL_CreateNode(-2);
    SLL_InsertNewHead(&List, NewNode);

    // 리스트 출력
    Count = SLL_GetNodeCount(List);
    for (i=0; i<Count; i++) {
        Current = SLL_GetNodeAt(List, i);
        printf("%d ", Current->Data);
    }

    // 리스트의 세 번째 노드 뒤에 새 노드 삽입
    printf("\nInserting 3000 After [2] \n\n");
    Current = SLL_GetNodeAt(List, 2);
    NewNode = SLL_CreateNode(3000);

    if (Current != NULL) {
        SLL_InsertAfter(Current, NewNode);
    }

    // 리스트 출력
    Count = SLL_GetNodeCount(List);
    for (i=0; i<Count; i++) {
        Current = SLL_GetNodeAt(List, i);
        printf("%d ", Current->Data);
    }

    // 모든 노드 제거
    printf("\nDestroying List  \n");

    for (i=0; i<Count; i++) {
        Current = SLL_GetNodeAt(List, 0);
        SLL_RemoveNode(&List, Current);
        SLL_DestroyNode(Current);
    } 

    return 0;
}

// 노드 생성
Node* SLL_CreateNode(ElementType NewData) {
    Node* NewNode = (Node*)malloc(sizeof(Node)); 
    
    NewNode->Data = NewData;    // 데이터를 저장한다
    NewNode->NextNode = NULL;   // 다음 노드에 대한 포인터는 NULL로 초기화한다. 

    return NewNode; // 노드의 주소를 반환한다.
}

// 노드 소멸
void SLL_DestroyNode(Node* Node) {
    free(Node);
}

// 노드 추가
void SLL_AppendNode(Node** Head, Node* NewNode) {
    // 헤드 노드가 NULL이면 새로운 노드가 Head가 된다
    if ( (*Head) == NULL ) {
        *Head = NewNode;
    } else { 
        // 테일을 찾아 NewNode를 연결한다.
        Node* Tail = (*Head);
        while( Tail->NextNode != NULL) {
            Tail = Tail->NextNode;
        }
        Tail->NextNode = NewNode;
    }
}

// 노드 탐색
Node* SLL_GetNodeAt(Node* Head, int Location) {
    Node* Current = Head;   // 현재 위치 Head로 초기화
    if (Location < 0) return NULL;

    while(Current != NULL && (--Location) >= 0) {
        Current = Current->NextNode;
    }
    return Current;
}

// 노드 삭제
void SLL_RemoveNode(Node** Head, Node* Remove) {
    if ( (*Head) == Remove) {
        *Head = Remove->NextNode;
    } else {
        Node* Current = *Head;

        while(Current != NULL && Current->NextNode != Remove) {
            Current = Current->NextNode;
        }
        if (Current != NULL) Current->NextNode = Remove->NextNode;
    }
}

// 노드 삽입
void SLL_InsertAfter(Node* Current, Node* NewNode) {
    if (Current == NULL || NewNode == NULL) return;

    NewNode->NextNode = Current->NextNode;
    Current->NextNode = NewNode;
}

void SLL_InsertNewHead(Node** Head, Node* NewHead) {
    if ((*Head) == NULL) {
        (*Head) = NewHead;
    } else {
        NewHead->NextNode = (*Head);
        (*Head) = NewHead;
    }
}

// 노드 개수 반환
int SLL_GetNodeCount(Node* Head) {
    int Count = 0;
    Node* Current = Head;

    while(Current != NULL) {
        Current = Current->NextNode;
        Count++;
    }
    return Count;
}