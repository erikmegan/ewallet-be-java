package com.flip.assignment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    public static void main(String[] args) {
        List<String> words = Arrays.asList(
                "UGHCVTBQZSALWQO",
                "DAQSSXWQHCKWOVM",
                "VAHIOCMLAQMZION",
                "LMVRADWROTHFMBT",
                "XSVQKREBKDFGCBE",
                "MBJDLRQOTKGOHLQ",
                "VLBRGWSGNBLDAQW",
                "QOELDPLIKINVKVD",
                "VHSKFORLOGLLBJD",
                "HFIEKODLHPDMWEK",
                "LFWNAJEDPOAYTDD",
                "KDGPESNFYFLMAKE",
                "FKFOGEWRKGLHADK",
                "FMXDPWLQPFJGLJG",
                "GLDPRLWQLMNVJFK",
                "TKPLSGLDOSMGOHM"
        ); // inputan soal
        List<String> wordSearch = Arrays.asList("ADA", "TOKO", "SIAPA", "DENI");
        List<String> results = new ArrayList<>();

        for(String word : wordSearch) {
            boolean found = false;
            StringBuilder result = new StringBuilder();

            //===================== find by column ======================
            for (int i = 0; i < words.get(0).length(); i++) {
                int indexWordArr = 0;
                for(int j = 0; j < words.size(); j++) {
                    if(indexWordArr >= word.length()) {
                        break;
                    }
                    if(word.charAt(indexWordArr) == words.get(j).charAt(i)) {
                        indexWordArr++;
                        result.append(words.get(j).charAt(i));
                    }
                }
                if (word.equals(result.toString())) {
                    found = true;
                    results.add("YA");
                    break;
                }
                result = new StringBuilder();
            }

            //klo ketemu, ga perlu cek ke bawah
            if(found) {
                continue;
            }

            //===================== find by row ======================
            for (int i = 0; i < words.size(); i++) {
                //search for column
                int indexWordArr = 0;
                for(int j = 0; j < words.get(i).length(); j++) {
                    if(indexWordArr >= word.length()) {
                        break;
                    }
                    if(word.charAt(indexWordArr) == words.get(i).charAt(j)) {
                        indexWordArr++;
                        result.append(words.get(i).charAt(j));
                    }
                }
                if (word.equals(result.toString())) {
                    found = true;
                    results.add("YA");
                    break;
                }
                result = new StringBuilder();
            }

            //klo ketemu, ga perlu cek ke bawah
            if(found) {
                continue;
            }

            //===================== find by diagonal ======================
            for (int i = 0; i < words.size(); i++) {
                //search for column
                int indexWordArr = 0;
                for(int j = 0; j < words.get(i).length(); j++) {
                    int column = j;
                    int row = i;
                    while (column < words.get(i).length() && row < words.size()) {
                        if(indexWordArr >= word.length()) {
                            break;
                        }
                        if(word.charAt(indexWordArr) == words.get(row).charAt(column)) {
                            indexWordArr++;
                            result.append(words.get(row).charAt(column));
                        }
                        column++;
                        row++;

                    }

                }
                if (word.equals(result.toString())) {
                    found = true;
                    results.add("YA");
                    break;
                }
                result = new StringBuilder();
            }

            //klo ga ketemu, tambahin tidak
            if(!found) {
                results.add("TIDAK");
            }

        }
        System.out.println(results);
    }
}