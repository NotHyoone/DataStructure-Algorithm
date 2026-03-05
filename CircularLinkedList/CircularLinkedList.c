#include <stdio.h>
#include <stdlib.h>

typedef struct Node {
    int Data;
    struct Node* NextNode;
    struct Node* PrevNode;
} Node;

Node* CL_CreateNode(int NewData) {
    Node* NewNode = (Node*)malloc(sizeof(Node));
    NewNode->Data = NewData;
    NewNode->PrevNode = NULL;
    NewNode->NextNode = NULL;

    return NewNode;
}

void CL_DestroyNode(Node* Node) {
    free(Node);
}

void CL_AppendNode(Node** Head, Node* NewNode) {
    if ((*Head) == NULL) {
        *Head = NewNode;
        (*Head)->NextNode = *Head;
        (*Head)->PrevNode = *Head;
    } else {
        // Tail과 Head 사이에 NewNode를 삽입한다.
        Node* Tail = (*Head)->PrevNode;

        (*Head)->PrevNode = NewNode;
        Tail->NextNode = NewNode;

        NewNode->NextNode = (*Head);
        NewNode->PrevNode = Tail;
    }
}

Node* CL_GetNodeAt(Node* Head, int Location) {
    Node* Current = Head;
    if (Location < 0) return NULL;
    while(Current != NULL && (--Location) >= 0) {
        Current = Current->NextNode;
    }
    return Current;
}

void CL_RemoveNode(Node** Head, Node* Remove) {
    if ( (*Head) == Remove ) {
        (*Head)->PrevNode->NextNode = Remove->NextNode;
        (*Head)->NextNode->PrevNode = Remove->PrevNode;

        *Head = Remove->NextNode;

        Remove->PrevNode = NULL;
        Remove->NextNode = NULL;
    } else { 
        Remove->PrevNode->NextNode = Remove->NextNode;  // 삭제할 노드의 이전 노드가, 삭제할 노드의 다음 노드를 연결
        Remove->NextNode->PrevNode = Remove->PrevNode;  // 삭제할 노드의 다음 노드가, 삭제할 노드의 이전 노드를 연결

        // 삭제할 노드의 연결 삭제
        Remove->NextNode = NULL;
        Remove->PrevNode = NULL;
    }
}

void CL_InsertAfter(Node* Current, Node* NewNode) {
    if (Current == NULL || NewNode == NULL) return;

    NewNode->NextNode = Current->NextNode;
    NewNode->PrevNode = Current;
    Current->NextNode->PrevNode = NewNode;
    Current->NextNode = NewNode;
}

int CL_GetNodeCount(Node* Head) {
    unsigned int Count = 0;
    Node* Current = Head;

    while(Current != NULL) {
        Current = Current->NextNode;
        Count++;
    }
    return Count;
}