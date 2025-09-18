public class SudokuSuma{
    // Tamaño del tablero (4x4), almacena cuántas filas y columnas tiene el sudoku
    static int N = 4;
    
    // Tablero inicial con algunos números ya puestos
    // Es una matriz de N x N que almacena los valores de cada celda del sudoku
    static int[][] board = {
        {1, 5, 0, 0},
        {0, 0, 4, 0},
        {0, 0, 0, 0},
        {0, 11, 0, 8}
    };
    
    // Sumas que debe tener cada fila y cada columna
    // rowSum almacena la suma objetivo de cada fila
    static int[] rowSum = {21, 43, 37, 35};  // Sumas de las filas
    // colSum almacena la suma objetivo de cada columna
    static int[] colSum = {37, 35, 33, 31};  // Sumas de las columnas
    
    // Array para marcar qué números ya se han usado (del 1 al 16)
    // used[i] es true si el número i ya está en el tablero
    static boolean[] used = new boolean[17];
    
    public static void main(String[] args) {
        // Imprime el tablero inicial
        System.out.println("Tablero inicial:");
        printBoard();
        System.out.println("\nSumas requeridas:");
        System.out.println("Filas: " + java.util.Arrays.toString(rowSum));
        System.out.println("Columnas: " + java.util.Arrays.toString(colSum));
        System.out.println("\nResolviendo...\n");
        
        // Marca los números que ya están puestos en el tablero
        markUsedNumbers();
        
        // Intenta resolver el sudoku
        if (solve(0, 0)) {
            System.out.println("Sudoku resuelto");
            printBoard();
            System.out.println("\nVerificacion de sumas:");
            verifySums();
        } else {
            System.out.println("No se encontró solución para este Sudoku.");
            printBoard();
        }
    }
    
    // Función que marca los números ya puestos en el tablero como usados
    static void markUsedNumbers() {
        // Recorre el tablero y marca en el arreglo 'used' los números que ya están
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != 0) {
                    used[board[i][j]] = true;
                }
            }
        }
    }
    
    // Función principal de backtracking para resolver el sudoku
    static boolean solve(int row, int col) {
        // Si llegamos al final del tablero, está resuelto
        if (row == N) {
            return true;
        }
        
        // Calcula la siguiente posición en el tablero
        int nextRow = (col == N - 1) ? row + 1 : row;
        int nextCol = (col == N - 1) ? 0 : col + 1;
        
        // Si la celda ya tiene un número, pasa a la siguiente
        if (board[row][col] != 0) {
            return solve(nextRow, nextCol);
        }
        
        // Prueba todos los números del 1 al 16 para esta celda
        for (int num = 1; num <= 16; num++) {
            // RESTRICCIONES PRINCIPALES:
            // 1. El número no debe estar repetido en el tablero (used[num] == false)
            // 2. El número debe cumplir con las sumas parciales de la fila y columna (canPlaceNumber)
            if (!used[num] && canPlaceNumber(row, col, num)) {
                board[row][col] = num; // Pon el número en la celda
                used[num] = true;      // Márcalo como usado
                
                boolean isValid = true;
                // Si estamos en la última columna, verifica la suma de la fila
                if (col == N - 1) {
                    isValid = checkRowSum(row);
                }
                // Si estamos en la última fila, verifica la suma de la columna
                if (row == N - 1) {
                    isValid = isValid && checkColSum(col);
                }
                // Si todo está bien, sigue con la siguiente celda
                if (isValid && solve(nextRow, nextCol)) {
                    return true;
                }
                // Si no funciona, quita el número y sigue probando
                board[row][col] = 0;
                used[num] = false;
            }
        }
        // Si no se puede poner ningún número, regresa falso (retrocede/backtrack)
        return false;
    }
    
    // Función que verifica si se puede poner un número en la celda sin pasarse de la suma
    static boolean canPlaceNumber(int row, int col, int num) {
        // RESTRICCIÓN: La suma parcial de la fila no debe exceder la suma objetivo
        // Verifica suma parcial de la fila
        int rowSum = 0;
        int emptyCellsInRow = 0;
        for (int j = 0; j < N; j++) {
            if (board[row][j] != 0) {
                rowSum += board[row][j];
            } else {
                emptyCellsInRow++;
            }
        }
        int newRowSum = rowSum + num;
        // Si es la última celda vacía en la fila, la suma debe ser exacta
        if (emptyCellsInRow == 1) {
            // RESTRICCIÓN: La suma de la fila debe ser igual a la suma objetivo
            if (newRowSum != SudokuSuma.rowSum[row]) {
                return false;
            }
        } else if (newRowSum > SudokuSuma.rowSum[row]) {
            // RESTRICCIÓN: No se puede exceder la suma objetivo de la fila
            return false;
        }
        
        // RESTRICCIÓN: La suma parcial de la columna no debe exceder la suma objetivo
        // Verifica suma parcial de la columna
        int colSum = 0;
        int emptyCellsInCol = 0;
        for (int i = 0; i < N; i++) {
            if (board[i][col] != 0) {
                colSum += board[i][col];
            } else {
                emptyCellsInCol++;
            }
        }
        int newColSum = colSum + num;
        // Si es la última celda vacía en la columna, la suma debe ser exacta
        if (emptyCellsInCol == 1) {
            // RESTRICCIÓN: La suma de la columna debe ser igual a la suma objetivo
            if (newColSum != SudokuSuma.colSum[col]) {
                return false;
            }
        } else if (newColSum > SudokuSuma.colSum[col]) {
            // RESTRICCIÓN: No se puede exceder la suma objetivo de la columna
            return false;
        }
        // Si pasa todas las verificaciones, se puede poner el número
        return true;
    }
    
    // Función que verifica si la suma de la fila es correcta
    static boolean checkRowSum(int row) {
        int sum = 0;
        for (int j = 0; j < N; j++) {
            sum += board[row][j];
        }
        return sum == rowSum[row];
    }
    
    // Función que verifica si la suma de la columna es correcta
    static boolean checkColSum(int col) {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            sum += board[i][col];
        }
        return sum == colSum[col];
    }
    
    // Función que imprime el tablero como una cuadrícula con sumas
    static void printBoard() {
        System.out.println("+----+----+----+----+");
        for (int i = 0; i < N; i++) {
            System.out.print("|");
            for (int j = 0; j < N; j++) {
                System.out.printf(" %2d |", board[i][j]);
            }
            System.out.printf(" = %2d\n", rowSum[i]);
            System.out.println("+----+----+----+----+");
        }
        // Imprime las sumas de las columnas debajo de la cuadrícula
        System.out.print(" ");
        for (int j = 0; j < N; j++) {
            System.out.printf(" %2d  ", colSum[j]);
        }
        System.out.println("\n");
    }
    
    // Función que verifica si el sudoku está bien resuelto
    static void verifySums() {
        boolean allCorrect = true;

        // Verifica sumas de filas
        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += board[i][j];
            }
            if (sum != rowSum[i]) {
                allCorrect = false;
                break;
            }
        }

        // Verifica sumas de columnas
        for (int j = 0; j < N; j++) {
            int sum = 0;
            for (int i = 0; i < N; i++) {
                sum += board[i][j];
            }
            if (sum != colSum[j]) {
                allCorrect = false;
                break;
            }
        }

        // Verifica que todos los números del 1 al 16 estén presentes
        boolean[] present = new boolean[17];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] >= 1 && board[i][j] <= 16) {
                    present[board[i][j]] = true;
                }
            }
        }
        for (int i = 1; i <= 16; i++) {
            if (!present[i]) {
                allCorrect = false;
                break;
            }
        }

        if (allCorrect) {
            System.out.println("Sudoku correcto.");
        } else {
            System.out.println("Sudoku incorrecto.");
        }
    }
}