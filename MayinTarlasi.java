import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MayinTarlasi {
    private char[][] minefield;
    private char[][] hiddenField;
    private int rowCount;
    private int colCount;
    private int mineCount;
    private int uncoveredCount;

    public MayinTarlasi(int rowCount, int colCount, int mineCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.mineCount = mineCount;
        this.minefield = new char[rowCount][colCount];
        this.hiddenField = new char[rowCount][colCount];
        this.uncoveredCount = 0;
    }

    public void initialize() {
        for (char[] row : minefield) {
            Arrays.fill(row, '-');
        }
        for (char[] row : hiddenField) {
            Arrays.fill(row, '-');
        }
    }

    public void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < mineCount) {
            int row = random.nextInt(rowCount);
            int col = random.nextInt(colCount);

            if (minefield[row][col] != '*') {
                minefield[row][col] = '*';
                minesPlaced++;
            }
        }
    }

    public void printField() {
        System.out.println("Mayın Tarlası Oyuna Hoşgeldiniz !");
        for (char[] row : hiddenField) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < rowCount && col >= 0 && col < colCount;
    }

    public void revealCell(int row, int col) {
        if (!isWithinBounds(row, col) || hiddenField[row][col] != '-') {
            System.out.println("Geçersiz bir nokta girdiniz. Tekrar deneyin.");
            return;
        }

        if (minefield[row][col] == '*') {
            revealAllMines();
            printField();
            System.out.println("Game Over!!");
            return;
        }

        int mineCount = countAdjacentMines(row, col);
        hiddenField[row][col] = (char) (mineCount + '0');
        uncoveredCount++;

        if (uncoveredCount == (rowCount * colCount) - mineCount) {
            revealAllMines();
            printField();
            System.out.println("Oyunu Kazandınız!");
            return;
        }

        if (mineCount == 0) {
            revealEmptyAdjacentCells(row, col);
        }

        printField();
    }

    private void revealEmptyAdjacentCells(int row, int col) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (isWithinBounds(newRow, newCol) && hiddenField[newRow][newCol] == '-') {
                revealCell(newRow, newCol);
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int mineCount = 0;

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (isWithinBounds(newRow, newCol) && minefield[newRow][newCol] == '*') {
                mineCount++;
            }
        }

        return mineCount;
    }

    private void revealAllMines() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if (minefield[row][col] == '*') {
                    hiddenField[row][col] = '*';
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Mayın Tarlası Satır Sayısı: ");
        int rowCount = scanner.nextInt();

        System.out.print("Mayın Tarlası Sütun Sayısı: ");
        int colCount = scanner.nextInt();

        int mineCount = (rowCount * colCount) / 4;

        MayinTarlasi game = new MayinTarlasi(rowCount, colCount, mineCount);
        game.initialize();
        game.placeMines();
        game.printField();

        while (true) {
            System.out.print("Satır Giriniz: ");
            int row = scanner.nextInt();

            System.out.print("Sütun Giriniz: ");
            int col = scanner.nextInt();

            game.revealCell(row, col);
        }
    }
}