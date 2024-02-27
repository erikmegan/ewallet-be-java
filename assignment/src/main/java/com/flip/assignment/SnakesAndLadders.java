package com.flip.assignment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnakesAndLadders {
    public static void main(String[] args) {
        List<List<Integer>> ladders = new ArrayList<>();
//    ladders.add(Arrays.asList(8, 52));
//    ladders.add(Arrays.asList(6, 80));
//    ladders.add(Arrays.asList(26, 42));
//    ladders.add(Arrays.asList(2, 72));

        ladders.add(Arrays.asList(32, 62));
        ladders.add(Arrays.asList(42, 68));
        ladders.add(Arrays.asList(12, 98));

        int result = Integer.MAX_VALUE;
        for(List<Integer> ladder : ladders) {
            int start = ladder.get(0);
            int end = ladder.get(1);
            int goal = 100 - (end - start);
            int totalStep = 0;
            int dice = 6;
            while (goal > 0) {
                if(goal >= dice) {
                    goal = goal - dice;
                    totalStep++;
                } else {
                    dice--;
                }
            }
            if(result > totalStep) {
                result = totalStep;
            }
        }
        System.out.println(result);
    }
}
