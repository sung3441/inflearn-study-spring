package study.jpa;

import java.util.HashMap;

public class Solution {

    public static void main(String[] args) {

    }

    static String[] solution(String[] players, String[] callings) {

        HashMap<String, Integer> map = new HashMap<>();
        for (int i  = 0; i < players.length; i++) {
            map.put(players[i], i);
        }

        for (String calling : callings) {
            Integer rank = map.get(calling);
            String target = players[rank - 1];

            players[rank - 1] = calling;
            players[rank] = target;

            map.put(calling, rank - 1);
            map.put(target, rank);
        }

        return players;
    }
}
