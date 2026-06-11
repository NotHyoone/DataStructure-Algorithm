package DC_DP;// 22212046 안효원

class DC_DP_Homework1 {
    int[] answer = new int[2];

    public int[] solution(int[][] arr) {
        compress(arr, 0, 0, arr.length);
        return answer;
    }

    private void compress(int[][] arr, int row, int col, int size) {
        if (isSame(arr, row, col, size)) {
            answer[arr[row][col]]++;    // 0 또는 1의 개수를 증가시키고 압축 종료
            return;
        }
        // 다르면 4등분하여 재귀적으로 압축
        int half = size / 2;
        compress(arr, row, col, half); // 1사분면
        compress(arr, row, col + half, half); // 2사분면
        compress(arr, row + half, col, half); // 3사분면
        compress(arr, row + half, col + half, half); // 4사분
    }

    // 주어진 영역이 모두 같은 값인지 확인하는 메서드
    private boolean isSame(int[][] arr, int row, int col, int size) {
        int value = arr[row][col];

        for (int i = row; i < row + size; i++) {
            for (int j = col; j < col + size; j++) {
                if (arr[i][j] != value) {
                    return false;
                }
            }
        }
        return true;
    }

}