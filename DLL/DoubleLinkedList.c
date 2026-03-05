#include <stdio.h>
#include <stdlib.h>

typedef int ElementType;

typedef struct tagNode {
    ElementType Data;
    struct tagNode* PrevNode;
    struct tagNode* NextNode;
} Node;

Node* DLL_CreateNode(ElementType NewData);
void DLL_DestroyNode(Node* Node);
void DLL_AppendNode(Node** Head, Node* NewNode);
Node* DLL_GetNodeAt(Node* Head, int Location);
void DLL_RemoveNode(Node** Head, Node* Remove);
void DLL_InsertAfter(Node* Current, Node* NewNode);
int DLL_GetNodeCount(Node* Head);
void PrintNode(Node* _Node);
void PrintReverse(Node* Head);

int main(void) {
    int i = 0;
    int Count = 0;
    Node* List = NULL;
    Node* NewNode = NULL;
    Node* Current = NULL;

    // 노드 5개 추가
    for (i=0; i<5; i++) {
        NewNode = DLL_CreateNode(i);
        DLL_AppendNode(&List, NewNode);
    }

    // 리스트 출력
    Count = DLL_GetNodeCount(List);
    for (i=0; i<Count; i++) {
        Current = DLL_GetNodeAt(List, i);
        printf("List[%d] : %d\n", i, Current->Data);
    }

    // 리스트의 세 번째 노드 뒤에 새 노드 삽입
    printf("\nInserting 3000 After [2] \n\n");
    Current = DLL_GetNodeAt(List, 2);
    NewNode = DLL_CreateNode(3000);
    if (Current != NULL) {
        DLL_InsertAfter(Current, NewNode);
    }

    // 리스트 출력
    Count = DLL_GetNodeCount(List);
    for (i=0; i<Count; i++) {
        Current = DLL_GetNodeAt(List, i);
        printf("List[%d] : %d\n", i, Current->Data);
    }

    PrintReverse(List);

    // 모든 노드 제거
    printf("\nDestroying List \n");

    Count = DLL_GetNodeCount(List);
    for (i=0; i<Count; i++) {
        Current = DLL_GetNodeAt(List, 0);

        if (Current != NULL) {
            DLL_RemoveNode(&List, Current);
            DLL_DestroyNode(Current);
        }
    }

    return 0;
}

// 노드 생성 함수
Node* DLL_CreateNode(ElementType NewData) {
    Node* NewNode = (Node*)malloc(sizeof(Node));

    NewNode->Data = NewData;
    NewNode->PrevNode = NULL;
    NewNode->NextNode = NULL;

    return NewNode;
}

// 노드 소멸
void DLL_DestroyNode(Node* Node) {
    free(Node);
}

// 노드 추가 함수
void DLL_AppendNode(Node** Head, Node* NewNode) {

    if ((*Head) == NULL) {
        *Head = NewNode;
    } else {
        // 테일을 찾아 NewNode를 연결한다.
        Node* Tail = (*Head);
        while(Tail->NextNode != NULL) {
            Tail = Tail->NextNode;
        }
        Tail->NextNode = NewNode;
        NewNode->PrevNode = Tail;   // 기존의 테일을 새로운 테일의 PrevNode가 가리킴
    }
}

// 노드 탐색 함수
Node* DLL_GetNodeAt(Node* Head, int Location) {
    Node* Current = Head;
    if (Location < 0) return NULL;
    while(Current != NULL && (--Location) >= 0) {
        Current = Current->NextNode;
    }
    return Current;
}

// 노드 삭제 함수
void DLL_RemoveNode(Node** Head, Node* Remove) {
    // 삭제할 노드가 헤드이면 
    if ( (*Head) == Remove ) {
        *Head = Remove->NextNode;
        if ( (*Head) != NULL )
            (*Head)->PrevNode = NULL;
        
        Remove->PrevNode = NULL;
        Remove->NextNode = NULL;
    } else {
        Node* Temp = Remove;

        if (Remove->PrevNode != NULL)
            Remove->PrevNode->NextNode = Temp->NextNode;
        if (Remove->NextNode != NULL)
            Remove->NextNode->PrevNode = Temp->PrevNode;

        Remove->PrevNode = NULL;
        Remove->NextNode = NULL;
    }
}

// 노드 삽입 함수
void DLL_InsertAfter(Node* Current, Node* NewNode) {
    if (Current == NULL || NewNode == NULL) return;
    
    NewNode->NextNode = Current->NextNode;
    NewNode->PrevNode = Current;

    if (Current->NextNode != NULL) {
        Current->NextNode->PrevNode = NewNode;
        Current->NextNode = NewNode;
    }
}

// 노드 개수 세기 함수
int DLL_GetNodeCount(Node* Head) {
    unsigned int Count = 0;
    Node* Current = Head;

    while(Current != NULL) {
        Current = Current->NextNode;
        Count++;
    }
    return Count;
}

void PrintNode(Node* _Node) {
    if (_Node->PrevNode == NULL) 
        printf("Prev: NULL\n");
    else 
        printf("Prev: %d\n", _Node->PrevNode->Data);
    
    printf("Current: %d\n", _Node->Data);

    if (_Node->NextNode == NULL) 
        printf("Next: NULL\n");
    else 
        printf("Next: %d\n", _Node->NextNode->Data);
}

void PrintReverse(Node* Head) {
    Node* Current = Head;

    if (Current == NULL) return;

    while(Current->NextNode != NULL) {
        Current = Current->NextNode;
    }

    while(Current != NULL) {
        PrintNode(Current);
        Current = Current->PrevNode;
    }
}