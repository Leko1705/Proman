package mylib;

import mylib.structs.AVLTree;

import java.util.*;

@SuppressWarnings("unused")
public final class Sort {

    private static final Random random = new Random();

    private static final PivotChooser[] choosers = {
            (l, r) -> l + ((r - l) / 2),
            (l, r) -> l,
            (l, r) -> r - 1,
            random::nextInt
    };

    public static final PivotChooser PIVOT_CENTER = choosers[0];
    public static final PivotChooser PIVOT_LEFT = choosers[1];
    public static final PivotChooser PIVOT_RIGHT = choosers[2];
    public static final PivotChooser PIVOT_RANDOM = choosers[3];

    private Sort(){
    }

    public static <T extends Comparable<T>> void bubble(T[] a){
        bubble(a, true);
    }

    public static <T extends Comparable<T>> void bubble(T[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static <T extends Comparator<T>> void bubble(T[] a){
        bubble(a, true);
    }

    public static <T extends Comparator<T>> void bubble(T[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static void bubble(int[] a){
        bubble(a, true);
    }

    public static void bubble(int[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static void bubble(double[] a) {
        bubble(a, true);
    }
    public static void bubble(double[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static void bubble(long[] a){
        bubble(a, true);
    }
    public static void bubble(long[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static void bubble(float[] a){
        bubble(a, true);
    }
    public static void bubble(float[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }

    public static void bubble(byte[] a){
        bubble(a, true);
    }
    public static void bubble(byte[] a, boolean ascending){
        for (int i = 0; i < a.length; i++){
            boolean sorted = true;
            for (int j = 0; j < a.length-1-i; j++){
                if (inOrder(a[j+1], a[j], ascending)){
                    swap(a, j, j+1);
                    sorted = false;
                }
            }
            if (sorted) return;
        }
    }


    public static <T extends Comparable<T>> void shaker(T[] a){
        shaker(a, true);
    }
    public static <T extends Comparable<T>> void shaker(T[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k-1], a[k], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static <T extends Comparator<T>> void shaker(T[] a){
        shaker(a, true);
    }
    public static <T extends Comparator<T>> void shaker(T[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static void shaker(int[] a){
        shaker(a, true);
    }
    public static void shaker(int[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static void shaker(double[] a){
        shaker(a, true);
    }
    public static void shaker(double[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static void shaker(long[] a){
        shaker(a, true);
    }
    public static void shaker(long[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static void shaker(float[] a){
        shaker(a, true);
    }
    public static void shaker(float[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static void shaker(byte[] a){
        shaker(a, true);
    }
    public static void shaker(byte[] a, boolean ascending){
        int i = 0, j = a.length-1;
        while (i < j){
            for (int k = i; k < j-1; k++){
                if (inOrder(a[k+1], a[k], ascending)){
                    swap(a, k, k+1);
                }
            }
            i++;
            for (int k = j; k > i-1; k--){
                if (inOrder(a[k], a[k-1], ascending)){
                    swap(a, k, k-1);
                }
            }
            j--;
        }
    }

    public static <T extends Comparable<T>> void selection(T[] a){
        selection(a, true);
    }
    public static <T extends Comparable<T>> void selection(T[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
             s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static <T extends Comparator<T>> void selection(T[] a){
        selection(a, true);
    }
    public static <T extends Comparator<T>> void selection(T[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static void selection(int[] a){
        selection(a, true);
    }
    public static void selection(int[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static void selection(double[] a){
        selection(a, true);
    }
    public static void selection(double[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static void selection(long[] a){
        selection(a, true);
    }
    public static void selection(long[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static void selection(float[] a){
        selection(a, true);
    }
    public static void selection(float[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }

    public static void selection(byte[] a){
        selection(a, true);
    }
    public static void selection(byte[] a, boolean ascending){
        int s;
        for (int i = 0; i < a.length - 1; i++){
            s = i;
            for (int j = i + 1; j < a.length; j++){
                if (inOrder(a[j], a[s], ascending)){
                    s = j;
                }
            }
            swap(a, i, s);
        }
    }



    public static <T extends Comparable<T>> void insertion(T[] a){
        insertion(a, true);
    }
    public static <T extends Comparable<T>> void insertion(T[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }

    public static <T extends Comparator<T>> void insertion(T[] a){
        insertion(a, true);
    }
    public static <T extends Comparator<T>> void insertion(T[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }

    public static void insertion(int[] a){
        insertion(a, true);
    }
    public static void insertion(int[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }

    public static void insertion(double[] a){
        insertion(a, true);
    }
    public static void insertion(double[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }

    public static void insertion(long[] a){
        insertion(a, true);
    }
    public static void insertion(long[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }

    public static void insertion(float[] a){
        insertion(a, true);
    }
    public static void insertion(float[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }

    }

    public static void insertion(byte[] a){
        insertion(a, true);
    }
    public static void insertion(byte[] a, boolean ascending){
        for (int i = 1; i < a.length; i++){
            for (int j = i; j > 0; j--){
                if (inOrder(a[j], a[j-1], ascending)){
                    swap(a, j, j-1);
                }
                else break;
            }
        }
    }


    public static <T extends Comparable<T>> void gnome(T[] a){
        gnome(a, true);
    }
    public static <T extends Comparable<T>> void gnome(T[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i].compareTo(a[i-1]) == 0){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static <T extends Comparator<T>> void gnome(T[] a){
        gnome(a, true);
    }
    public static <T extends Comparator<T>> void gnome(T[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i].compare(a[i], a[i-1]) == 0){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static void gnome(int[] a){
        gnome(a, true);
    }
    public static void gnome(int[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i] == a[i-1]){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static void gnome(double[] a){
        gnome(a, true);
    }
    public static void gnome(double[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i] == a[i-1]){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static void gnome(long[] a){
        gnome(a, true);
    }
    public static void gnome(long[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i] == a[i-1]){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static void gnome(float[] a){
        gnome(a, true);
    }
    public static void gnome(float[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i] == a[i-1]){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }

    public static void gnome(byte[] a){
        gnome(a, true);
    }
    public static void gnome(byte[] a, boolean ascending){
        int i = 0;
        while (i < a.length){
            if (i == 0 || inOrder(a[i-1], a[i], ascending) || a[i] == a[i-1]){
                i++;
            }
            else {
                swap(a, i, i-1);
                i--;
            }
        }
    }



    public static <T extends Comparable<T>> void quick(T[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }

    public static <T extends Comparable<T>> void quick(T[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }

    public static <T extends Comparable<T>> void quick(T[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }

    public static <T extends Comparable<T>> void quick(T[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }

    private static <T extends Comparable<T>> void quick0(T[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        T p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i].compareTo(a[j]) == 0) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }


    public static <T extends Comparator<T>> void quick(T[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }

    public static <T extends Comparator<T>> void quick(T[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static <T extends Comparator<T>> void quick(T[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }

    public static <T extends Comparator<T>> void quick(T[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }

    private static <T extends Comparator<T>> void quick0(T[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        T p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i].compare(a[i], a[j]) == 0) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }


    public static void quick(int[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }
    public static void quick(int[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static void quick(int[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }
    public static void quick(int[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }
    private static void quick0(int[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        int p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i] == a[j]) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }


    public static void quick(double[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }
    public static void quick(double[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static void quick(double[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }
    public static void quick(double[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }
    private static void quick0(double[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        double p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i] == a[j]) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }

    public static void quick(long[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }
    public static void quick(long[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static void quick(long[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }
    public static void quick(long[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }
    private static void quick0(long[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        long p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i] == a[j]) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }


    public static void quick(float[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }
    public static void quick(float[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static void quick(float[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }
    public static void quick(float[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }
    private static void quick0(float[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        float p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i] == a[j]) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }


    public static void quick(byte[] a, boolean ascending){
        quick0(a, 0, a.length, PIVOT_CENTER, ascending);
    }
    public static void quick(byte[] a, PivotChooser chooser, boolean ascending){
        quick0(a, 0, a.length, chooser, ascending);
    }
    public static void quick(byte[] a){
        quick0(a, 0, a.length, PIVOT_CENTER, true);
    }
    public static void quick(byte[] a, PivotChooser chooser){
        quick0(a, 0, a.length, chooser, true);
    }
    private static void quick0(byte[] a, int l, int r, PivotChooser pivotChooser, boolean ascending){

        if (l >= r) return;

        byte p = a[pivotChooser.choose(l, r)];
        int i = l;
        int j = r - 1;

        while (i < j){

            while (i < j && inOrder(a[i], p, ascending)) i++;
            while (i < j && inOrder(p, a[j], ascending)) j--;

            if (i < j) {
                swap(a, i, j);
                if (a[i] == a[j]) i++;
            }
        }

        quick0(a, l, i, pivotChooser, ascending);
        quick0(a, i+1, r, pivotChooser, ascending);

    }

    public static <T extends Comparable<T>> void merge(T[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }
    public static <T extends Comparable<T>> void merge(T[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }

    private static <T extends Comparable<T>> T[] divide(T[] a, boolean ascending){
        if (a.length == 1) return a;
        T[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        T[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static <T extends Comparable<T>> T[] merge0(T[] a, T[] b, boolean ascending){
        Comparable<T>[] res = new Comparable[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return (T[]) res;
    }

    public static <T extends Comparator<T>> void merge(T[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }
    public static <T extends Comparator<T>> void merge(T[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }
    private static <T extends Comparator<T>> T[] divide(T[] a, boolean ascending){
        if (a.length == 1) return a;
        T[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        T[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static <T extends Comparator<T>> T[] merge0(T[] a, T[] b, boolean ascending){
        Comparator<T>[] res = new Comparator[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ? a[i++] : b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return (T[]) res;
    }


    public static void merge(int[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }
    public static void merge(int[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }

    private static int[] divide(int[] a, boolean ascending){
        if (a.length == 1) return a;
        int[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        int[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static int[] merge0(int[] a, int[] b, boolean ascending){
        int[] res = new int[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return res;
    }


    public static void merge(double[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }

    public static void merge(double[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }
    private static double[] divide(double[] a, boolean ascending){
        if (a.length == 1) return a;
        double[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        double[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static double[] merge0(double[] a, double[] b, boolean ascending){
        double[] res = new double[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return res;
    }


    public static void merge(long[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }
    public static void merge(long[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }
    private static long[] divide(long[] a, boolean ascending){
        if (a.length == 1) return a;
        long[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        long[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static long[] merge0(long[] a, long[] b, boolean ascending){
        long[] res = new long[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return res;
    }


    public static void merge(float[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }
    public static void merge(float[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }
    private static float[] divide(float[] a, boolean ascending){
        if (a.length == 1) return a;
        float[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        float[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static float[] merge0(float[] a, float[] b, boolean ascending){
        float[] res = new float[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return res;
    }


    public static void merge(byte[] a){
        System.arraycopy(divide(a, true), 0, a, 0, a.length);
    }

    public static void merge(byte[] a, boolean ascending){
        System.arraycopy(divide(a, ascending), 0, a, 0, a.length);
    }
    private static byte[] divide(byte[] a, boolean ascending){
        if (a.length == 1) return a;
        byte[] l = divide(Arrays.copyOfRange(a, 0, a.length/2), ascending);
        byte[] r = divide(Arrays.copyOfRange(a, a.length/2, a.length), ascending);
        return merge0(l, r, ascending);
    }

    private static byte[] merge0(byte[] a, byte[] b, boolean ascending){
        byte[] res = new byte[a.length + b.length];

        int i = 0, j = 0, p = 0;

        while (i < a.length && j < b.length)
            res[p++] = inOrder(a[i], a[j], ascending) ?  a[i++] :  b[j++];

        while (i < a.length) res[p++] = a[i++];
        while (j < b.length) res[p++] = b[j++];

        return res;
    }

    public static <T extends Comparable<T>> void heap(T[] a){
        heap(a, true);
    }
    public static <T extends Comparable<T>> void heap(T[] a, boolean ascending){
        PriorityQueue<T> heap = new PriorityQueue<>(List.of(a));
        if (ascending){
            for (int i = 0; i < a.length; i++) {
                a[i] = heap.poll();
            }
        }
        else {
            for (int i = a.length - 1; i >= 0; i--) {
                a[i] = heap.poll();
            }
        }
    }

    public static <T extends Comparator<T>> void heap(T[] a) {
        heap(a, true);
    }
    public static <T extends Comparator<T>> void heap(T[] a, boolean ascending){
        PriorityQueue<T> heap = new PriorityQueue<>(List.of(a));
        if (ascending){
            for (int i = 0; i < a.length; i++) {
                a[i] = heap.poll();
            }
        }
        else {
            for (int i = a.length - 1; i >= 0; i--) {
                a[i] = heap.poll();
            }
        }
    }


    public static void heap(int[] a) {
        heap(a, true);
    }
    public static void heap(int[] a, boolean ascending) {
        Integer[] b = new Integer[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        heap(b, ascending);
        for (int i = 0; i < a.length; i++) a[i] = b[i];
    }

    public static void heap(double[] a) {
        heap(a, true);
    }

    public static void heap(double[] a, boolean ascending) {
        Double[] b = new Double[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        heap(b, ascending);
        for (int i = 0; i < a.length; i++) a[i] = b[i];
    }

    public static void heap(long[] a) {
        heap(a, true);
    }
    public static void heap(long[] a, boolean ascending) {
        Long[] b = new Long[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        heap(b, ascending);
        for (int i = 0; i < a.length; i++) a[i] = b[i];
    }

    public static void heap(float[] a) {
        heap(a, true);
    }

    public static void heap(float[] a, boolean ascending) {
        Float[] b = new Float[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        heap(b, ascending);
        for (int i = 0; i < a.length; i++) a[i] = b[i];
    }

    public static void heap(byte[] a) {
        heap(a, true);
    }

    public static void heap(byte[] a, boolean ascending) {
        Byte[] b = new Byte[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        heap(b, ascending);
        for (int i = 0; i < a.length; i++) a[i] = b[i];
    }

    public static <T extends Comparable<T>> void bogo(T[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static <T extends Comparator<T>> void bogo(T[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static void bogo(int[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static void bogo(double[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static void bogo(long[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static void bogo(float[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static void bogo(byte[] a, boolean ascending){
        do {
            int i = random.nextInt(a.length);
            int j = random.nextInt(a.length);
            swap(a, i, j);
        } while (!sorted(a, ascending));
    }

    public static <T extends Comparable<T>> void treeSort(T[] a){
        treeSort(a, true);
    }

    public static <T extends Comparable<T>> void treeSort(T[] a, boolean ascending){
        AVLTree<T> tree = new AVLTree<>(List.of(a));

        // sort via inorder traversal
        Iterator<T> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static void treeSort(int[] a){
        treeSort(a, true);
    }

    public static void treeSort(int[] a, boolean ascending){
        List<Integer> b = new LinkedList<>();
        for (int i : a) b.add(i);

        AVLTree<Integer> tree = new AVLTree<>(b);
        Iterator<Integer> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static void treeSort(double[] a){
        treeSort(a, true);
    }

    public static void treeSort(double[] a, boolean ascending){
        List<Double> b = new LinkedList<>();
        for (double i : a) b.add(i);

        AVLTree<Double> tree = new AVLTree<>(b);
        Iterator<Double> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static void treeSort(long[] a){
        treeSort(a, true);
    }

    public static void treeSort(long[] a, boolean ascending){
        List<Long> b = new LinkedList<>();
        for (long i : a) b.add(i);

        AVLTree<Long> tree = new AVLTree<>(b);
        Iterator<Long> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static void treeSort(float[] a){
        treeSort(a, true);
    }

    public static void treeSort(float[] a, boolean ascending){
        List<Float> b = new LinkedList<>();
        for (float i : a) b.add(i);

        AVLTree<Float> tree = new AVLTree<>(b);
        Iterator<Float> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static void treeSort(byte[] a){
        treeSort(a, true);
    }

    public static void treeSort(byte[] a, boolean ascending){
        List<Byte> b = new LinkedList<>();
        for (byte i : a) b.add(i);

        AVLTree<Byte> tree = new AVLTree<>(b);
        Iterator<Byte> itr = tree.iterator();
        if (ascending){
            for (int i = 0; i < a.length; i++){
                a[i] = itr.next();
            }
        }
        else {
            for (int i = a.length-1; i >= 0; i--){
                a[i] = itr.next();
            }
        }
    }

    public static <T extends Comparable<T>> boolean sorted(T[] a){
        return sorted(a, true);
    }

    public static <T extends Comparable<T>> boolean sorted(T[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static <T extends Comparator<T>> boolean sorted(T[] a){
        return sorted(a, true);
    }

    public static <T extends Comparator<T>> boolean sorted(T[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static boolean sorted(int[] a){
        return sorted(a, true);
    }

    public static boolean sorted(int[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static boolean sorted(double[] a){
        return sorted(a, true);
    }

    public static boolean sorted(double[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static boolean sorted(long[] a){
        return sorted(a, true);
    }

    public static boolean sorted(long[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static boolean sorted(float[] a){
        return sorted(a, true);
    }

    public static boolean sorted(float[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static boolean sorted(byte[] a){
        return sorted(a, true);
    }

    public static boolean sorted(byte[] a, boolean ascending){
        for (int i = 0; i < a.length-1; i++){
            if (inOrder(a[i+1], a[i], ascending))
                return false;
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean sorted(Collection<T> c){
        return sorted(c, true);
    }

    public static <T extends Comparable<T>> boolean sorted(Collection<T> c, boolean ascending){
        Iterator<T> itr = c.iterator();
        T previous = itr.next();
        while (itr.hasNext()){
            T next = itr.next();
            if (inOrder(next, previous, ascending))
                return false;
            previous = next;
        }
        return true;
    }

    public static <T> boolean sorted(Collection<T> c, Comparator<T> comparator){
        Iterator<T> itr = c.iterator();
        T previous = itr.next();
        while (itr.hasNext()){
            T next = itr.next();
            if (comparator.compare(next, previous) < 0)
                return false;
            previous = next;
        }
        return true;
    }

    private static <T extends Comparable<T>> boolean inOrder(T a, T b, boolean ascending){
        return (a.compareTo(b) < 0 && ascending) || (a.compareTo(b) > 0 && !ascending);
    }

    private static <T extends Comparator<T>> boolean inOrder(T a, T b, boolean ascending){
        return (a.compare(a, b) < 0 && ascending) || (a.compare(a, b) > 0 && !ascending);
    }

    private static boolean inOrder(long a, long b, boolean ascending){
        return (a < b && ascending) || (a > b && !ascending);
    }

    private static <T> void swap(Comparable<T>[] a, int i, int j){
        Comparable<T> dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static <T> void swap(Comparator<T>[] a, int i, int j){
        Comparator<T> dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static void swap(int[] a, int i, int j){
        int dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static void swap(double[] a, int i, int j){
        double dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static void swap(long[] a, int i, int j){
        long dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static void swap(float[] a, int i, int j){
        float dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }

    private static void swap(byte[] a, int i, int j){
        byte dummy = a[i];
        a[i] = a[j];
        a[j] = dummy;
    }


    public interface PivotChooser{
        int choose(int l, int r);
    }

}
