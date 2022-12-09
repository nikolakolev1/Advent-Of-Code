import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        mainMethod();
    }

    public static void mainMethod() {
        Set<String> tailPositionsPart1 = new HashSet<>();
        Head head = new Head();
        Tail tail = new Tail();

        Set<String> tailPositionsPart2 = new HashSet<>();
        Head headP2 = new Head();
        Tail t1 = new Tail(), t2 = new Tail(), t3 = new Tail(), t4 = new Tail(), t5 = new Tail(), t6 = new Tail(), t7 = new Tail(), t8 = new Tail(), t9 = new Tail();

        try {
            File input = new File("input.txt");
            Scanner myScanner = new Scanner(input);

            while (myScanner.hasNextLine()) {
                String thisRow = myScanner.nextLine();

                int steps = Integer.parseInt(thisRow.substring(2));
                for (int i = 0; i < steps; i++) {
                    int[] tailMoveP1 = tail.moveIf(head.move(thisRow.substring(0, 1)));
                    tailPositionsPart1.add(tailMoveP1[0] + " " + tailMoveP1[1]);

                    int[] tailMoveP2 = t9.moveIf(t8.moveIf(t7.moveIf(t6.moveIf(t5.moveIf(t4.moveIf(t3.moveIf(t2.moveIf(t1.moveIf(headP2.move(thisRow.substring(0, 1)))))))))));
                    tailPositionsPart2.add(tailMoveP2[0] + " " + tailMoveP2[1]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Part 1: " + tailPositionsPart1.size());
        System.out.println("Part 2: " + tailPositionsPart2.size());
    }

    static class Head {
        public Map<String, int[]> directionsMap;
        public int xCoordinate;
        public int yCoordinate;

        public Head() {
            directionsMap = new HashMap<>();
            directionsMap.put("R", new int[]{1, 0});
            directionsMap.put("L", new int[]{-1, 0});
            directionsMap.put("U", new int[]{0, 1});
            directionsMap.put("D", new int[]{0, -1});

            xCoordinate = 0;
            yCoordinate = 0;
        }

        public int[] move(String direction) {
            int[] changeCoordinates = directionsMap.get(direction);
            xCoordinate += changeCoordinates[0];
            yCoordinate += changeCoordinates[1];
            return new int[]{xCoordinate, yCoordinate};
        }
    }

    static class Tail {
        public int xCoordinate;
        public int yCoordinate;

        public Tail() {
            xCoordinate = 0;
            yCoordinate = 0;
        }

        public int[] moveIf(int[] headNewCoordinates) {
            int differenceInX = headNewCoordinates[0] - xCoordinate;
            int differenceInY = headNewCoordinates[1] - yCoordinate;

            if (differenceInY == 0) {
                if (differenceInX == 2) {
                    xCoordinate++;
                } else if (differenceInX == -2) {
                    xCoordinate--;
                }
            } else if (differenceInX == 0) {
                if (differenceInY == 2) {
                    yCoordinate++;
                } else if (differenceInY == -2) {
                    yCoordinate--;
                }
            } else if (differenceInY == 1 || differenceInY == -1) {
                if (differenceInX == 2) {
                    xCoordinate++;
                    if (differenceInY == 1) {
                        yCoordinate++;
                    } else {
                        yCoordinate--;
                    }
                } else if (differenceInX == -2) {
                    xCoordinate--;
                    if (differenceInY == 1) {
                        yCoordinate++;
                    } else {
                        yCoordinate--;
                    }
                }
            } else if (differenceInX == 1 || differenceInX == -1) {
                if (differenceInY == 2) {
                    if (differenceInX == 1) {
                        xCoordinate++;
                    } else {
                        xCoordinate--;
                    }
                    yCoordinate++;
                } else if (differenceInY == -2) {
                    if (differenceInX == 1) {
                        xCoordinate++;
                    } else {
                        xCoordinate--;
                    }
                    yCoordinate--;
                }
            } else {
                if (differenceInX == 2) {
                    xCoordinate++;
                    if (differenceInY == 2) {
                        yCoordinate++;
                    } else if (differenceInY == -2) {
                        yCoordinate--;
                    }
                } else if (differenceInX == -2) {
                    xCoordinate--;
                    if (differenceInY == 2) {
                        yCoordinate++;
                    } else if (differenceInY == -2) {
                        yCoordinate--;
                    }
                }
            }

            return new int[]{xCoordinate, yCoordinate};
        }
    }
}