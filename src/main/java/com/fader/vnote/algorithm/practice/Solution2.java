package com.fader.vnote.algorithm.practice;

/**
 * @author FaderW
 * 找出无序数组中第K大元素
 * 2019/6/13
 */

public class Solution2 {

    public static int findTopNumInArray(int[] array, int k) {
        int left = 0;
        int right = array.length-1;

        return findTopK(array, k, left, right);
    }

    private static int findTopK(int[] array, int k, int left, int right) {
        if (left >= right) {
            throw new RuntimeException("error");
        }
        int index = partition(array, left, right);
        if ((k-1) == index) {
            return array[index];
        } else if ((k-1) > index) {
            return findTopK(array, k, index+1, right);
        } else {
            return findTopK(array, k, left, index-1);
        }
    }

    private static int partition(int[] array, int left, int right) {
        // 保存当前store下标，初始为left
        int storeIndex = left;
        int pivot = array[right];
        for (int j = left; j < right; j++) {
            // 如果小于pivot，说明应该存入当前storeIndex,交换位置
            if (array[j] < pivot) {
                int temp = array[j];
                array[j] = array[storeIndex];
                array[storeIndex] = temp;
                // storeIndex往后移动一位
                storeIndex++;
            }
        }

        // 最后将pivot于当前storeIndex元素交换
        int temp2 = array[storeIndex];
        array[storeIndex] = array[right];
        array[right] = temp2;

        return storeIndex;
    }

    /**
     * 反转整数
     */
    public class Solution7 {
        /**
         * 转换为字符串再反转
         * @param x
         * @return
         */
        public int reverse(int x) {
            if (x == 0) {
                return 0;
            }
            boolean flag = x > 0;
            x = Math.abs(x);
            String s = String.valueOf(x);
            StringBuilder sb = new StringBuilder();
            for (int i = s.length()-1; i >=0; i--) {
                sb.append(s.charAt(i));
            }
            try {
                x = Integer.parseInt(sb.toString());
            } catch (NumberFormatException e) {
                return 0;
            }

            if (!flag) {
                x = x*(-1);
            }
            return x;
        }


        public int reverse2(int x) {
            int res = 0;
            int pop;
            while (x != 0) {
                pop = x % 10;
                x /= 10;

                if (res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && pop > Integer.MAX_VALUE%10)) {
                    return 0;
                } else if (res < Integer.MIN_VALUE/10 || (res == Integer.MIN_VALUE/10 && pop < Integer.MIN_VALUE%10)) {
                    return 0;
                }
                res = res * 10 + pop;
            }

            return res;
        }
    }

    public void test7() {
        Solution7 solution7 = new Solution7();
        System.out.println(solution7.reverse2(-343535));
    }

    public static void main(String[] args) {
        Solution2 solution2 = new Solution2();
        solution2.test7();
    }
}
