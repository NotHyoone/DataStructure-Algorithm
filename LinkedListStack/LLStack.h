#ifndef LINKEDLIST_STACK_H
#define LINKEDLIST_STACK_H

#include <stdio.h>
#include <string.h> 
#include <stdlib.h>

typedef struct tagNode {
    char* Data;
    struct tagNode* NextNode;
} Node;

typedef struct tagLinkedListStack {
    Node* List; // 데이터를 담는 링크드 리스트를 가리킴
    Node* Top;  // 링크드 리스트의 테일을 가리킴
} LinkedListStack;

void LLS_CreateStack(LinkedListStack** Stack);
void LLS_DestroyStack(LinkedListStack* Stack);
Node* LLS_CreateNode(char* NewData);
void LLS_DestroyNode(Node* _Node);
void LLS_Push(LinkedListStack* Stack, Node* NewNode);
Node* LLS_Pop(LinkedListStack* Stack);
Node* LLS_Top(LinkedListStack* Stack);
int LLS_GetSize(LinkedListStack* Stack);
int LLS_IsEmpty(LinkedListStack* Stack);

#endif // LINKEDLIST_STACK_H