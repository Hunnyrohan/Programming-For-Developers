import java.util.Arrays;

public class KthSmallestInvestment {

    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        long left = (long) returns1[0] * returns2[0];
        long right = (long) returns1[returns1.length - 1] * returns2[returns2.length - 1];

        // Binary search on the product range
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (countPairs(returns1, returns2, mid) < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return (int) left;
    }

    private static int countPairs(int[] returns1, int[] returns2, long target) {
        int count = 0;
        for (int num : returns1) {
            if (num == 0) {
                if (target >= 0) {
                    count += returns2.length;
                }
            } else if (num > 0) {
                // For positive numbers, find elements in returns2 <= target / num
                count += binarySearchCount(returns2, target / num);
            } else {
                // For negative numbers, find elements in returns2 >= target / num
                count += returns2.length - binarySearchCount(returns2, target / num - 1);
            }
        }
        return count;
    }

    private static int binarySearchCount(int[] arr, long target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println(kthSmallestProduct(returns1, returns2, k)); // Output: 8

        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        k = 6;
        System.out.println(kthSmallestProduct(returns3, returns4, k)); // Output: 0
    }
}