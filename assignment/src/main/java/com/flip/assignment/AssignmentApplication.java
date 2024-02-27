package com.flip.assignment;

//import com.sun.tools.javac.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


@SpringBootApplication
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);

//		List<String> wordsF = new ArrayList<>(Arrays.asList("ADA", "TOKO", "SIAPA"));
//		List<String> boardF =new ArrayList<>(Arrays.asList("UGHCVTBQZSALWQO",
//				"DAQSSXWQHCKWOVM",
//				"VAHIOCMLAQMZION",
//				"LMVRAQWROTHFMBT",
//				"XSVQKRFBKDFGCBE",
//				"MBJDLRQETKGOHLQ",
//				"VLBRGWSGLBLDAQW",
//				"QOELDPLIKDNVKVD",
//				"VHSKFORLOGLLBJD",
//				"HFIEKODLHPDMWEK",
//				"LFWNAJEDPOAYTDD",
//				"KDGPESNFYFLMAKE",
//				"FKFOGEWRKGLHADK",
//				"FMXDPWLQPFJGLJG",
//				"GLDPRLWQLMNVJFK",
//				"TKPLSGLDOSMGOHM"));
//		String[] words = wordsF.toArray(new String[0]);
//
//		char[][] board = boardF.stream()
//				.map(String::toCharArray)
//				.toArray(char[][]::new);
//
//		Result solver = new Result();
//		List<String> result = solver.findWords(board, words);
//
//		for (String str : result) {
//			System.out.println(str);
//		}

		// Printing the char[][] for demonstration
//		for (char[] row : board) {
//			for (char c : row) {
//				System.out.print(c + " ");
//			}
//			System.out.println();
//		}
	}

}
class Result {

	/*
	 * Complete the 'cari' function below.
	 *
	 * The function is expected to return a STRING_ARRAY.
	 * The function accepts following parameters:
	 *  1. STRING_ARRAY huruf
	 *  2. STRING_ARRAY kata
	 */

	private static int[][] mover = {{1, 0}, {0, 1}, {-1, 0},
			{0, -1}, {1, 1}, {-1, -1},
			{1, -1}, {-1, 1}};

	public static boolean dfs(int x, int y, String word, boolean[][] visited,
							  char[][] board) {

		// If word length becomes 0, the string is found
		if (word.length() == 0) {
			return true;
		}

		visited[x][y] = true;
		boolean sol = false;

		// Making possible moves
		for (int[] move : mover) {
			int curr_x = move[0] + x;
			int curr_y = move[1] + y;

			// Checking for out of bound areas
			if (0 <= curr_x && curr_x < board.length && 0 <= curr_y && curr_y < board[0].length) {

				// Checking for similarity in the first letter and the visited array
				if (board[curr_x][curr_y] == word.charAt(0) && !visited[curr_x][curr_y]) {
					String s = word.substring(1);
					sol |= dfs(curr_x, curr_y, s, visited, board);
				}
			}
		}

		visited[x][y] = false;
		return sol;
	}

	public static List<String> findWords(char[][] board, String[] words) {
		List<String> ans = new ArrayList<>();
		boolean[][] visited = new boolean[board.length][board[0].length];

		for (String word : words) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] == word.charAt(0)) {
						String s = word.substring(1);
						if (dfs(i, j, s, visited, board)) {
							ans.add(word + " -> {" + i + "," + j + "}");
						}
					}
				}
			}
		}

		return ans;
	}

	public static List<String> cari(List<String> huruf, List<String> kata) {
		// Write your code here
		char[][] board = huruf.stream()
				.map(String::toCharArray)
				.toArray(char[][]::new);
		String[] words = kata.toArray(new String[0]);
		List<String> result = findWords(board, words);
		return result;
	}

}

 class Testing {
	// Rows and columns in the given grid
	static int R, C;

	// For searching in all 8 direction
	static int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };
	static int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };

	static boolean search2D(char[][] grid, int row,
							int col, String word)
	{
		// If first character of word
		// doesn't match with
		// given starting point in grid.
		if (grid[row][col] != word.charAt(0))
			return false;

		int len = word.length();

		// Search word in all 8 directions
		// starting from (row, col)
		for (int dir = 0; dir < 8; dir++) {
			// Initialize starting point
			// for current direction
			int k, rd = row + x[dir], cd = col + y[dir];

			// First character is already checked,
			// match remaining characters
			for (k = 1; k < len; k++) {
				// If out of bound break
				if (rd >= R || rd < 0 || cd >= C || cd < 0)
					break;

				// If not matched, break
				if (grid[rd][cd] != word.charAt(k))
					break;

				// Moving in particular direction
				rd += x[dir];
				cd += y[dir];
			}

			// If all character matched,
			// then value of must
			// be equal to length of word
			if (k == len)
				return true;
		}
		return false;
	}

	// Searches given word in a given
	// matrix in all 8 directions
	static void patternSearch(
			char[][] grid,
			String word, List<String> result)
	{
		// Consider every point as starting
		// point and search given word
		for (int row = 0; row < R; row++) {
			for (int col = 0; col < C; col++) {
				if (grid[row][col]==word.charAt(0)  &&
						search2D(grid, row, col, word)){
//					System.out.println(
//							"pattern found at " + row + ", " + col);
					result.add("ADA");
				}else{
					result.add("TIDAK");
				}

			}
		}
	}

	public static void main(String[] args) {
		List<String> words = Arrays.asList("GEEKSFORGEEKS", "GEEKSQUIZGEEK", "IDEQAPRACTICE"); // inputan soal
		List<String> wordSearch = Arrays.asList("GEEKS"); //inputan soal
		//Assumes all lines are the same length
		char[][] charCharArray = new char[words.size()][words.get(0).length()];

		for(int i = 0; i < words.size(); i++)  {
			String line = words.get(i);
			charCharArray[i] = line.toCharArray();
		}
		R = charCharArray.length;
		C = charCharArray[0].length;
		List<String> result = new ArrayList<>();
		for(String search : wordSearch) {
			patternSearch(charCharArray, search, result);
		}


		List<Integer> res = new ArrayList<>();
		Integer smallest = Integer.MAX_VALUE;
		for (int i = 0; i < res.size(); i++){
			if (smallest > res.get(i)) {
				smallest = res.get(i);
			}
		}
	}

	 public static int langkahTercepat(List<List<Integer>> ladders, List<List<Integer>> snakes) {
		 // Write your code here
		 List<Integer> res = new ArrayList<>();
		 for (int i = 0; i < snakes.size(); i++){
//			 res.add(minThrow(N, arr));
			 snakes.get(i).get(i);
		 }

		 Integer smallest = Integer.MAX_VALUE;
		 for (int i = 0; i < res.size(); i++){
			 if (smallest > res.get(i)) {
				 smallest = res.get(i);
			 }
		 }
		 return smallest;
	 }

}


