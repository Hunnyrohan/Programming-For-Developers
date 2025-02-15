public class CriticalTemperature {

    public static int findMinMeasurements(int k, int n) {
        if (n == 0) {
            return 0;
        }
        int left = 1;
        int right = n;
        int result = n;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canDetermine(mid, k, n)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    private static boolean canDetermine(int m, int k, int n) {
        long sum = 1; // Start with C(m,0) = 1
        long currentTerm = 1;
        int maxIterations = Math.min(m, k);
        for (int i = 1; i <= maxIterations; ++i) {
            currentTerm *= (m - i + 1);
            currentTerm /= i;
            sum += currentTerm;
            if (sum > n) { // Early exit to avoid overflow
                return true;
            }
        }
        return sum >= (n + 1);
    }

    public static void main(String[] args) {
        System.out.println(findMinMeasurements(1, 2));  // Output: 2
        System.out.println(findMinMeasurements(2, 6));  // Output: 3
        System.out.println(findMinMeasurements(3, 14)); // Output: 4
    }
}