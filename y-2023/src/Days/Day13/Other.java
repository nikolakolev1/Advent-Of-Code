package Days.Day13;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Other {
    public static void main(String[] args) throws IOException {
        new Puzzle().solve();
    }
}

record Mirror(ArrayList<String> rows, Set<Long> solutions) {
    static Mirror from(ArrayList<String> rows) {
        return new Mirror(rows, new TreeSet<>());
    }

    private static boolean isReallyIt(final int i, List<String> strings) {
        int l = i - 1;
        int r = i;
        final int s = strings.size();
        while (l >= 0 && r < s) {
            if (!strings.get(l).equals(strings.get(r))) {
                return false;
            }
            --l;
            ++r;
        }
        return true;
    }

    private void findSolution() {
        List<String> columns = IntStream.range(0, rows.get(0).length()).mapToObj(i -> rows.stream().map(row -> row.charAt(i)).map(String::valueOf).collect(Collectors.joining())).toList();
        solutions.addAll(axes(columns, 1L));
        solutions.addAll(axes(rows, 100L));
    }

    long summary() {
        findSolution();
        return solutions.stream().findFirst().orElseThrow();
    }

    private Set<Long> axes(List<String> strings, long factor) {
        return IntStream.range(1, strings.size()).filter(i -> strings.get(i - 1).equals(strings.get(i))).filter(i -> isReallyIt(i, strings)).mapToObj(i -> factor * i).collect(Collectors.toSet());
    }

    private char toggle(char c) {
        return switch (c) {
            case '.' -> '#';
            case '#' -> '.';
            default -> throw new IllegalArgumentException();
        };
    }

    private Stream<String> smudge(String s) {
        return IntStream.range(0, s.length()).mapToObj(i -> s.substring(0, i) + toggle(s.charAt(i)) + s.substring(i + 1));
    }

    long smudge() {
        Long originalSolution = solutions.stream().findFirst().orElseThrow();
        IntStream.range(0, rows.size()).boxed().forEach(i -> {
            String save = rows.get(i);
            smudge(save).forEach(s -> {
                rows.set(i, s);
                findSolution();
            });
            rows.set(i, save);
        });
        Set<Long> copy = new TreeSet<>(solutions);
        copy.remove(originalSolution);
        return copy.stream().findFirst().orElseThrow();
    }
}

record Mirrors(List<Mirror> mirrors) {
    static Mirrors parse(Stream<String> lines) {
        List<ArrayList<String>> blocks = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        lines.forEach(line -> {
            if (line.isEmpty()) {
                counter.incrementAndGet();
            } else {
                if (counter.get() == blocks.size()) {
                    blocks.add(new ArrayList<>());
                }
                blocks.get(counter.get()).add(line);
            }
        });
        return new Mirrors(blocks.stream().map(Mirror::from).toList());
    }

    long summary() {
        return mirrors.stream().mapToLong(Mirror::summary).sum();
    }

    public long smudge() {
        return mirrors.stream().mapToLong(Mirror::smudge).sum();
    }
}

class Puzzle {
    void solve() {
        try {
            File initialFile = new File("data/day13/input.txt");
            InputStream input = new FileInputStream(initialFile);
            var reader = new BufferedReader(new InputStreamReader(input));
            var mirrors = Mirrors.parse(reader.lines());
            System.out.println(mirrors.summary());
            System.out.println(mirrors.smudge());
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

