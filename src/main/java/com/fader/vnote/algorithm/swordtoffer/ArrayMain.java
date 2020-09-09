package com.fader.vnote.algorithm.swordtoffer;

public class ArrayMain {

    /**
     * 可以将值为 i 的元素调整到第 i 个位置上进行求解。
     * 本题要求找出重复的数字，因此在调整过程中，如果第 i 位置上已经有一个值为 i 的元素，就可以知道 i 值重复。
     * @param nums
     * @return
     */
    public static int findRepeatNumber(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // 当前下标对应的值是否等于下标，否的话则交换到值所对应的下标
            while (nums[i] != i) {
                // 如果当前下标j（nums[i]）已经有值为j的元素，则值j重复
                if (nums[i] == nums[nums[i]]) {
                    return nums[i];
                }
                swap(nums, i, nums[i]);
            }

        }

        return -1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 二维数组中的查找
     * @param matrix
     * @param target
     * @return
     */
    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        int columnLength = matrix[0].length;
        int rowLength = matrix.length;
        int row = 0; int col = columnLength-1;
        while (row < rowLength && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] > target) {
                col--;
            } else {
                row++;
            }
        }

        return false;
    }

    /**
     * 替换空格
     * @param s
     * @return
     */
    public static String replaceSpace(String s) {
//        StringBuilder stringBuilder = new StringBuilder(s);
//        int oldLength = stringBuilder.length();
//        for (int i = 0; i < oldLength; i++) {
//            if (stringBuilder.charAt(i) == ' ') {
//                stringBuilder.append("  ");
//            }
//        }
//        int newLength = stringBuilder.length();
//        int p2 = newLength-1;
//        int p1 = oldLength-1;
//        while (p2 > p1 && p1 > 0) {
//            char c = stringBuilder.charAt(p1--);
//            if (c == ' ') {
//                stringBuilder.setCharAt(p2--, '0');
//                stringBuilder.setCharAt(p2--, '2');
//                stringBuilder.setCharAt(p2--, '%');
//            } else {
//                stringBuilder.setCharAt(p2--, c);
//            }
//        }
//
//        return stringBuilder.toString();
        int length = s.length();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                length++;
            }
        }
        char[] chars = new char[length];
        int p1 = s.length();
        int p2 = length;
        while (p2 > p1 && p1 > 0) {
            char c = s.charAt(p1--);
            if (c == ' ') {
                chars[p2--] = '0';
                chars[p2--] = '2';
                chars[p2--] = '%';
            } else {
                chars[p2--] = c;
            }
        }

        return new String(chars, 0, chars.length);
    }

    static int RectCover(int target) {
        if (target <= 2) {
            return target;
        }
        int[] dp = new int[target+1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= target; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }

        return dp[target];
    }

    /**
     * 青蛙跳台阶II
     * @param target
     * @return
     */
    public int JumpFloorII(int target) {
        if (target < 2) {
            return 1;
        }
        return (int) Math.pow(2, target-1);
    }

    private boolean[][] marked;
    private int rows;
    private int cols;

    public boolean exist(char[][] board, String word) {
        this.rows = board.length;
        this.cols = board[0].length;
        marked = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (backTracking(board, i, j, word)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean backTracking(char[][] board, int row, int col, String word) {
        if (row == rows || row < 0 || col == cols || col < 0) {
            return false;
        }
        char c = board[row][col];
        if (marked[row][col] || word.charAt(0) != c) {
            return false;
        }
        if (backTracking(board, row-1, col, word.substring(1))) {return true;}
        if (backTracking(board, row+1, col, word.substring(1))) {return true;}
        if (backTracking(board, row, col-1, word.substring(1))) {return true;}
        if (backTracking(board, row, col+1, word.substring(1))) {return true;}

        return false;
    }

    public static void main(String[] args) {
        System.out.print(RectCover(2));
    }

}
