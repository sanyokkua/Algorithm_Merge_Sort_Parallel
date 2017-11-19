package com.kostenko.mergesort;

import com.kostenko.mergesort.sorters.InsertSort;
import com.kostenko.mergesort.sorters.MergeSort;
import com.kostenko.mergesort.sorters.ParallelMergeSort;
import com.kostenko.mergesort.sorters.SimpleSort;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;
import static org.junit.Assert.assertTrue;

public class TestMergeSort {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int[] test1 = {2, 6, 3, 8, 5, 1, 4, 9, 7, 10, 14, 0, 11, 15, 13, 12};
    private static final int[] test2 = copyOf(test1, test1.length);
    private static final int[] test3 = copyOf(test1, test1.length);
    private static final int[] test4 = copyOf(test1, test1.length);
    private Sort SIMPLE;
    private Sort INSERT;
    private Sort MERGE;
    private Sort PARALLEL_MERGE;
    private boolean runSlowSorters;
    private int numberOfElements;

    @Before
    public void setup() {
        String size = System.getProperty("size", "100000");
        String runSlow = System.getProperty("slow", "false");
        numberOfElements = Integer.parseInt(size);
        runSlowSorters = Boolean.parseBoolean(runSlow);

        SIMPLE = new SimpleSort();
        INSERT = new InsertSort();
        MERGE = new MergeSort();
        PARALLEL_MERGE = new ParallelMergeSort(128);
    }

    @Test
    public void testStaticDataSort() {
        test(SIMPLE, test1);
        test(INSERT, test2);
        test(MERGE, test3);
        test(PARALLEL_MERGE, test4);
    }

    @Test
    public void testDynamicBigDataSort() {
        int[] bigArraySimpleSort = RANDOM.ints(numberOfElements).toArray();
        int[] bigArrayInsertSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);
        int[] bigArrayMergeSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);
        int[] bigArrayMergeParallelSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);

        if (runSlowSorters) {
            test(SIMPLE, bigArraySimpleSort);
            test(INSERT, bigArrayInsertSort);
        }
        test(MERGE, bigArrayMergeSort);
        test(PARALLEL_MERGE, bigArrayMergeParallelSort);
    }

    private void test(Sort sort, int[] array) {
        System.out.println("-------------------------------------------------------------");
        System.out.println(format("Sorting arrays with %d elements", array.length));
        String className = sort.getClass().getSimpleName();
        Instant before = Instant.now();
        sort.sort(array);
        Instant after = Instant.now();
        long millis = Duration.between(before, after).toMillis();
        System.out.println(format("Sorted by: %s, Time: %d millis, %d seconds", className, millis, millis / 1000));
        isSorted(array);
        System.out.println("-------------------------------------------------------------");
    }

    private void isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            assertTrue(array[i] <= array[i + 1]);
        }
    }
}
