package mylib.math;

public final class LevenshteinDistance {

    private LevenshteinDistance(){
        // nothing happens
    }

    public static int calculate(String first, String second){
        int[][] matrix = new int[first.length()+1][second.length()+1];
        return calculate0(first, second, matrix);
    }

    private static int calculate0(String first, String second, int[][] matrix){

        for (int i = 0; i < matrix.length; i++){
            matrix[i][0] = i;
        }
        for (int i = 0; i < matrix[0].length; i++){
            matrix[0][i] = i;
        }

        for (int i = 0; i < matrix.length-1; i++){
            for (int j = 0; j < matrix[0].length-1; j++){

                int field1;
                if (first.charAt(i) == second.charAt(j))
                    field1 = matrix[i][j];
                else
                    field1 = matrix[i][j] + 1;

                int field2 = matrix[i+1][j] + 1;
                int field3 = matrix[i][j+1] + 1;
                int min = Math.min(field1, Math.min(field2, field3));
                matrix[i+1][j+1] = min;

            }
        }
        return matrix[matrix.length-1][matrix[0].length-1];
    }

}
