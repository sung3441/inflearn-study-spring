import java.util.HashMap;

public class Main {

    public static void main(String[] args) { // 65 ~ 91
        int[] solution = solution(new String[]{"ABACD", "BCEFD"}, new String[]{"ABDC", "AABB"});
        for (int i : solution) {
            System.out.println(i);
        }
    }

    static int[] solution(String[] keymap, String[] targets) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] answer = new int[targets.length];
        for (int i = 65; i <= 91; i++) {
            int min = 100;
            for (String key : keymap) {
                int idx = key.indexOf(i);
                if (idx == -1) continue;
                if (idx < min) {
                    min = idx;
                    map.put(i, idx + 1);
                }
            }
        }

        for (int i = 0; i < targets.length; i++) {
            String target = targets[i];
            int sum = 0;
            for (int j = 0; j < target.length(); j++) {
                int alpha = target.charAt(j);
                if (!map.containsKey(alpha)) {
                    sum = -1;
                    break;
                }
                sum += map.get(alpha);
            }
            answer[i] = sum;
        }
        return answer;
    }
}