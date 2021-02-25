import java.io.*;
import java.util.*;


public class SegTreeSetTo {
    public static void main(String[] args) {

        /*
        *
        * this is just a tester in the main for the segment tree
         */
        Random random = new Random(5);
        int T = 2000;
        for (int tt = 0; tt < T; tt++) {
            System.out.println();
            int n = 1 + random.nextInt(10);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = random.nextInt(100);
            }
            int[] b = new int[n];
            for (int i = 0; i < n; i++) {
                b[i] = a[i];
            }
            WorkingSegTreeSum st = new WorkingSegTreeSum(0, n - 1, a);
            int nQueries = 10;
            for (int qq = 0; qq < nQueries; qq++) {
                int k = random.nextInt(5);
                if (k == 0) { // rangeSet
                    int l = random.nextInt(n);
                    int r = random.nextInt(n);
                    int max = Math.max(l, r);
                    int min = Math.min(l, r);
                    int val = random.nextInt(100);
                    st.setTo(min, max, val);
                    for (int j = min; j <= max; j++) {
                        b[j] = val;
                    }

                } else if (k == 1) { //pointUpdate
                    int index = random.nextInt(n);
                    int val = random.nextInt(100);
                    st.pointUpdate(index, val);
                    b[index] = val;
                } else if (k == 2) { //rangeMax
                    int l = random.nextInt(n);
                    int r = random.nextInt(n);
                    int max = Math.max(l, r);
                    int min = Math.min(l, r);

                    int stans = st.rangeMax(min, max);
                    int ans = Integer.MIN_VALUE;
                    for (int j = min; j <= max; j++)
                        ans = Math.max(ans, b[j]);
                    if (stans != ans)
                        System.out.println("FAILED RANGEMAX");

                } else if (k == 3) { //rangeMin
                    int l = random.nextInt(n);
                    int r = random.nextInt(n);
                    int max = Math.max(l, r);
                    int min = Math.min(l, r);

                    int stans = st.rangeMin(min, max);
                    int ans = Integer.MAX_VALUE;
                    for (int j = min; j <= max; j++)
                        ans = Math.min(ans, b[j]);
                    if (stans != ans)
                        System.out.println("FAILED RANGEMIN");
                } else if (k == 4) { //rangeSum
                    int l = random.nextInt(n);
                    int r = random.nextInt(n);
                    int max = Math.max(l, r);
                    int min = Math.min(l, r);

                    int stans = st.rangeSum(min, max);
                    int ans = 0;
                    for (int j = min; j <= max; j++)
                        ans += b[j];
                    if (stans != ans)
                        System.out.println("FAILED RANGESUM");
                }

            }
        }
    }
    /*
    *
    * does rangesum, rangemin, rangemax, rangeset, pointupdate
     */
    static class WorkingSegTreeSum {
        int leftmost, rightmost;
        WorkingSegTreeSum left, right;
        int sum;
        int max;
        int min;
        int setTo = -1;

        public WorkingSegTreeSum(int leftmost, int rightmost, int[] a) {
            this.leftmost = leftmost;
            this.rightmost = rightmost;
            if (leftmost == rightmost) {
                // it is a leaf
                sum = a[leftmost];
                max = a[leftmost];
                min = a[leftmost];
            } else {
                // it is not a leaf
                int mid = (leftmost + rightmost) / 2;
                left = new WorkingSegTreeSum(leftmost, mid, a);
                right = new WorkingSegTreeSum(mid + 1, rightmost, a);
                recalc();
            }
        }

        public int sum() {
            if (setTo == -1)
                return sum;
            return setTo == 0 ? 0 : (rightmost - leftmost + 1) * setTo;
        }

        public int max() {
            if (setTo == -1)
                return max;
            return setTo == 0 ? 0 : setTo;
        }

        public int min() {
            if (setTo == -1)
                return min;
            return setTo == 0 ? 0 : setTo;
        }

        public void recalc() {
            if (leftmost == rightmost) {
                return;
            }
            sum = left.sum() + right.sum();
            max = Math.max(left.max(), right.max());
            min = Math.min(left.min(), right.min());

        }

        public void prop() {
            if (setTo == -1) return;
            if (leftmost == rightmost) {
                sum = setTo;
                max = setTo;
                min = setTo;
                setTo = -1;
                return;
            }
            left.setTo = setTo;
            right.setTo = setTo;
            setTo = -1;
            recalc();
        }

        public void setTo(int l, int r, int setTo) {
            prop();
            if (l <= leftmost && r >= rightmost) {
                this.setTo = setTo;
                return;
            }
            if (r < leftmost || l > rightmost) {
                return;
            }
            left.setTo(l, r, setTo);
            right.setTo(l, r, setTo);
            recalc();
        }

        public void pointUpdate(int index, int newVal) {
            prop();
            if (leftmost == rightmost) {
                sum = newVal;
                max = newVal;
                min = newVal;
                return;
            }
            if (index <= left.rightmost) {
                left.pointUpdate(index, newVal);
            } else {
                right.pointUpdate(index, newVal);
            }
            recalc();
        }

        public int rangeSum(int l, int r) {
            prop();
            // entirely disjoint
            if (l > rightmost || r < leftmost)
                return 0;
            //covers us
            if (l <= leftmost && r >= rightmost) {
                return sum();
            }
            //others
            return left.rangeSum(l, r) + right.rangeSum(l, r);
        }

        public int rangeMax(int l, int r) {
            prop();
            // entirely disjoint
            if (l > rightmost || r < leftmost)
                return Integer.MIN_VALUE;
            //covers us
            if (l <= leftmost && r >= rightmost) {
                return max();
            }
            //others
            return Math.max(left.rangeMax(l, r), right.rangeMax(l, r));
        }

        public int rangeMin(int l, int r) {
            prop();
            // entirely disjoint
            if (l > rightmost || r < leftmost)
                return Integer.MAX_VALUE;
            //covers us
            if (l <= leftmost && r >= rightmost) {
                return min();
            }
            //others
            return Math.min(left.rangeMin(l, r), right.rangeMin(l, r));
        }
    }
}
