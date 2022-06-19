import java.util.*;

public class Main {
    static Map<Character, List<String>> grammar;
    static Map<Character, Integer> map;
    static Integer[][] matrix;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        System.out.println(isDerivable(line));
    }

    public static boolean isDerivable(String line) {
        init();
        line += "#";
        char[] chain = line.toCharArray();
        int cursor = 0;

        Stack<Character> stack = new Stack<>();
        stack.add('#');
        boolean failure = false;

        if (chain.length <2) {
            return false;
        }
        while (cursor <= chain.length) {
            char c = chain[cursor];
            if (!map.containsKey(c)) {
                failure = true;
                break;
            }
            if (c == '#' && stack.size() == 2 && stack.peek() == 'S')
                break;
            Integer relation = getRelation(stack.peek(), chain[cursor]);
            if (relation == null) {
                failure = true;
                break;
            }
            if (relation <= 0) {
                stack.push(c);
                cursor++;
            } else {
                StringBuilder sb = new StringBuilder();
                char last;
                do {
                    last = stack.pop();
                    sb.insert(0, last);
                } while (getRelation(stack.peek(), last) >= 0);
                Iterator<Map.Entry<Character, List<String>>> iterator = grammar.entrySet().iterator();
                boolean found = false;
                char leftPart = 0;
                while (iterator.hasNext() && !found) {
                    Map.Entry<Character, List<String>> entry = iterator.next();
                    if (entry.getValue().contains(sb.toString())) {
                        found = true;
                        leftPart = entry.getKey();
                    }
                }
                if (!found) {
                    failure = true;
                    break;
                }
                stack.add(leftPart);
            }
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.insert(0, stack.pop());
            sb.insert(0, stack.pop());
        } catch (EmptyStackException e) {
            return false;
        }
        return !failure && sb.toString().equals("#S");
    }


    private static void init() {
        grammar = new HashMap<>();
        List<String> S = new ArrayList<>();
        S.add("D");
        grammar.put('S', S);
        List<String> D = new ArrayList<>();
        D.add("B");
        grammar.put('D', D);
        List<String> A = new ArrayList<>();
        A.add("A+T");
        A.add("T");
        grammar.put('A', A);
        List<String> T = new ArrayList<>();
        T.add("F*T");
        T.add("F");
        grammar.put('T', T);
        List<String> F = new ArrayList<>();
        F.add("a");
        F.add("(B)");
        grammar.put('F', F);
        List<String> B = new ArrayList<>();
        B.add("A");
        grammar.put('B', B);

        map = new HashMap<>();
        map.put('S', 0);
        map.put('D', 1);
        map.put('A', 2);
        map.put('T', 3);
        map.put('F', 4);
        map.put('B', 5);
        map.put('+', 6);
        map.put('*', 7);
        map.put('a', 8);
        map.put('(', 9);
        map.put(')', 10);
        map.put('#', 11);

        matrix = new Integer[][]{
                {null, null, null, null, null, null, null, null, null, null, null, 1},
                {null, null, null, null, null, null, null, null, null, null, null, 1},
                {null, null, null, null, null, null,    0, null, null, null,    1, 1},
                {null, null, null, null, null, null,    1, null, null, null,    1, 1},
                {null, null, null, null, null, null,    1,    0, null, null,    1, 1},
                {null, null, null, null, null, null, null, null, null, null,    0, 1},
                {null, null, null,    0,   -1, null, null, null,   -1,   -1, null, 1},
                {null, null, null,    0,   -1, null, null, null,   -1,   -1, null, 1},
                {null, null, null, null, null, null,    1,    1, null, null,    1, 1},
                {null, null,   -1,   -1,   -1,    0, null, null,   -1,   -1, null, 1},
                {null, null, null, null, null, null,    1,    1, null, null,    1, 1},
                {  -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1, -1,null},
        };
    }

    private static Integer getRelation(char left, char right) {
        return matrix[map.get(left)][map.get(right)];
    }
}
