import java.io.*;
import java.util.*;

public class Main {
    private static int N, M, P;
    private static String S;
    private static ArrayList<String> puzzleShapes = new ArrayList<>();
    private static char[][] board;
    private static boolean solutionFound = false;
    private static long iterationCount = 0; // Menyimpan jumlah percobaan

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file test case (contoh: test_case.txt): ");
        String fileName = scanner.nextLine();

        try {
            readInputFile(fileName);
            board = new char[N][M];
            initializeBoard();

            long startTime = System.currentTimeMillis();
            solvePuzzle(0);
            long endTime = System.currentTimeMillis();

            if (!solutionFound) {
                System.out.println("Tidak ada solusi yang ditemukan.");
            }

            System.out.println("Waktu pencarian: " + (endTime - startTime) + " ms");
            System.out.println("Banyak kasus yang ditinjau: " + iterationCount);
            System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
            String saveOption = scanner.nextLine();
            if (saveOption.equalsIgnoreCase("ya")) {
                saveSolution();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File tidak ditemukan di folder 'test'.");
            System.out.println("Pastikan:");
            System.out.println("1. File " + fileName + " ada di dalam folder 'test'");
            System.out.println("2. Folder 'test' ada di lokasi yang sama dengan program");
            String projectPath = new File(System.getProperty("user.dir")).getParent();
            System.out.println("Path yang dicari: " + new File(projectPath + File.separator + "test").getAbsolutePath());
        }
    }

    private static void readInputFile(String fileName) throws FileNotFoundException {
        String projectPath = new File(System.getProperty("user.dir")).getParent();
        File testDir = new File(projectPath + File.separator + "test");
        File file = new File(testDir, fileName);
        Scanner fileScanner = new Scanner(file);
        puzzleShapes.clear();

        N = fileScanner.nextInt();
        M = fileScanner.nextInt();
        P = fileScanner.nextInt();
        fileScanner.nextLine();
        S = fileScanner.nextLine();
        
        Character currentLetter = null;
        StringBuilder currentShape = new StringBuilder();
        
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (!line.isEmpty()) {
                // Get first character of each line (the letter identifier)
                char firstChar = line.charAt(0);
                
                // If this is a new letter or first shape
                if (currentLetter == null || firstChar != currentLetter) {
                    // Save previous shape if exists
                    if (currentShape.length() > 0) {
                        puzzleShapes.add(currentShape.toString());
                    }
                    // Start new shape
                    currentShape = new StringBuilder();
                    currentLetter = firstChar;
                }
                
                // Add line to current shape
                if (currentShape.length() > 0) {
                    currentShape.append("\n");
                }
                // Add full line including the letter
                currentShape.append(line);
            }
        }
        
        // Add final shape
        if (currentShape.length() > 0) {
            puzzleShapes.add(currentShape.toString());
        }
        
        fileScanner.close();    }

    private static void initializeBoard() {
        board = new char[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = '.';
            }
        }
    }

    private static boolean solvePuzzle(int pieceIndex) {
        if (pieceIndex == P) {
            if (isBoardFull()) {
                solutionFound = true;
                System.out.println("\nSolution found!");
                printBoard();
                return true;
            }
            return false;
        }

        String piece = puzzleShapes.get(pieceIndex);
        ArrayList<String> orientations = getAllOrientations(piece);

        for (String rotatedPiece : orientations) {
            int height = rotatedPiece.split("\n").length;
            int width = rotatedPiece.split("\n")[0].length(); // Ambil panjang baris pertama

            for (int i = 0; i <= N - height; i++) { // Hanya cek posisi yang bisa menampung blok
                for (int j = 0; j <= M - width; j++) {
                    iterationCount++; // Hitung setiap percobaan menempatkan blok
                    
                    if (canPlacePiece(rotatedPiece, i, j)) {
                        placePiece(rotatedPiece, i, j, (char) ('A' + pieceIndex));
                        if (solvePuzzle(pieceIndex + 1)) {
                            return true;
                        }
                        removePiece(rotatedPiece, i, j);
                    }
                }
            }
        }

        return false;
    }

    private static ArrayList<String> getAllOrientations(String piece) {
        HashSet<String> uniqueOrientations = new HashSet<>();
        ArrayList<String> orientations = new ArrayList<>();
        
        // Tambahkan bentuk asli
        uniqueOrientations.add(piece);
        orientations.add(piece);

        // Tambahkan pencerminan horizontal dan vertikal
        String mirroredHoriz = mirror(piece);
        String mirroredVert = mirrorVertically(piece);
        
        if (uniqueOrientations.add(mirroredHoriz)) {
            orientations.add(mirroredHoriz);
        }
        if (uniqueOrientations.add(mirroredVert)) {
            orientations.add(mirroredVert);
        }

        // Tambahkan rotasi 90°, 180°, 270° untuk setiap bentuk
        String current = piece;
        for (int i = 0; i < 3; i++) {
            current = rotateRight(current);
            if (uniqueOrientations.add(current)) {
                orientations.add(current);
            }
        }

        // Ulangi rotasi untuk bentuk-bentuk yang dipantulkan
        current = mirroredHoriz;
        for (int i = 0; i < 3; i++) {
            current = rotateRight(current);
            if (uniqueOrientations.add(current)) {
                orientations.add(current);
            }
        }

        return orientations;
    }

    private static String mirrorVertically(String piece) {
        String[] lines = piece.split("\n");
        List<String> flipped = new ArrayList<>(Arrays.asList(lines));
        Collections.reverse(flipped);
        return String.join("\n", flipped);
    }

    private static String rotateRight(String piece) {
        if (piece == null || piece.isEmpty()) {
            return piece;
        }

        String[] lines = piece.split("\n");
        if (lines.length == 0 || lines[0].isEmpty()) {
            return piece;
        }

        int height = lines.length;
        int width = lines[0].length();
        char[][] rotated = new char[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j < lines[i].length()) {
                    rotated[j][height - 1 - i] = lines[i].charAt(j);
                } else {
                    rotated[j][height - 1 - i] = ' ';
                }
            }
        }
        return charArrayToString(rotated);
    }

    private static String mirror(String piece) {
        String[] lines = piece.split("\n");
        StringBuilder mirrored = new StringBuilder();

        for (String line : lines) {
            mirrored.append(new StringBuilder(line).reverse()).append("\n");
        }

        return mirrored.toString().trim();
    }

    private static String charArrayToString(char[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sb.append(arr[i][j]);
            }
            if (i < arr.length - 1) sb.append("\n");
        }
        return sb.toString();
    }

    private static boolean canPlacePiece(String piece, int row, int col) {
        if (row < 0 || col < 0) return false;
        
        String[] lines = piece.split("\n");
        int height = lines.length;
        int width = 0;

        // Hitung lebar maksimum blok
        for (String line : lines) {
            width = Math.max(width, line.length());
        }

        // Cek apakah blok keluar dari batas papan
        if (row + height > N || col + width > M) return false;

        boolean hasAdjacent = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) != ' ') {
                    if (board[row + i][col + j] != '.') {
                        return false; // Bentrok dengan blok lain
                    }
                    if (checkAdjacent(row + i, col + j)) {
                        hasAdjacent = true;
                    }
                }
            }
        }

        return (row == 0 && col == 0) || hasAdjacent; // Blok pertama bebas, lainnya harus adjacent
    }

    private static boolean checkAdjacent(int row, int col) {
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < M) {
                if (board[newRow][newCol] != '.') {
                    return true;
                }
            }
        }
        return false;
    }

    private static void placePiece(String piece, int row, int col, char c) {
        String[] lines = piece.split("\n");
        for (int i = 0; i < lines.length && row + i < N; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length() && col + j < M; j++) {
                if (Character.isLetter(line.charAt(j))) {
                    board[row + i][col + j] = c;
                }
            }
        }
    }

    private static void removePiece(String piece, int row, int col) {
        String[] lines = piece.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                if (Character.isLetter(lines[i].charAt(j))) {
                    board[row + i][col + j] = '.';
                }
            }
        }
    }


    private static boolean isBoardFull() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void saveSolution() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file untuk menyimpan solusi (contoh: solusi1.txt): ");
        String fileName = scanner.nextLine().trim();

        try {
            String projectPath = new File(System.getProperty("user.dir")).getParent();
            File testDir = new File(projectPath + File.separator + "test");
            File solutionFile = new File(testDir, fileName);

            java.io.PrintWriter writer = new java.io.PrintWriter(solutionFile);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    writer.print(board[i][j]);
                }
                writer.println();
            }
            writer.close();
            System.out.println("Solusi telah disimpan dalam file: " + solutionFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("Gagal menyimpan solusi: " + e.getMessage());
        }
    }
}