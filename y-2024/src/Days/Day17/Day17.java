package Days.Day17;

import General.Day;

import java.io.File;
import java.util.Scanner;

public class Day17 implements Day {
    private long registerA, registerB, registerC;
    private long registerB_initial, registerC_initial = -1;
    private int[] program;
    private int instructionPointer = 0;
    private String output = "";

    public static void main(String[] args) {
        Day day17 = new Day17();
        day17.loadData("data/day17/input.txt");
        System.out.println(day17.part1());
        System.out.println(day17.part2());
    }

    @Override
    public void loadData(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                // TODO: Load data
                String line = scanner.nextLine();

                if (line.isEmpty()) continue;

                if (line.startsWith("Register")) {
                    if (line.contains("A")) {
                        registerA = Long.parseLong(line.split(": ")[1]);
                    } else if (line.contains("B")) {
                        registerB = Long.parseLong(line.split(": ")[1]);
                        registerB_initial = registerB;
                    } else if (line.contains("C")) {
                        registerC = Long.parseLong(line.split(": ")[1]);
                        registerC_initial = registerC;
                    }
                } else {
                    String[] split = line.substring(9).split(",");
                    program = new int[split.length];

                    for (int i = 0; i < split.length; i++) {
                        program[i] = Integer.parseInt(split[i]);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        while (instructionPointer < program.length) {
            int opcode = program[instructionPointer];
            int operand = program[instructionPointer + 1];

            switch (opcode) {
                case 0 -> instruction0(operand);
                case 1 -> instruction1(operand);
                case 2 -> instruction2(operand);
                case 3 -> instruction3(operand);
                case 4 -> instruction4();
                case 5 -> instruction5(operand);
                case 6 -> instruction6(operand);
                case 7 -> instruction7(operand);
                default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
            }

            instructionPointer += 2;
        }

        // Remove trailing comma
        if (output.endsWith(",")) output = output.substring(0, output.length() - 1);
        return output;
    }

    @Override
    public String part2() {
        for (int i = 0; true; i++) {
            resetAll();
            registerA = i;

            while (instructionPointer < program.length) {
                int opcode = program[instructionPointer];
                int operand = program[instructionPointer + 1];

                switch (opcode) {
                    case 0 -> instruction0(operand);
                    case 1 -> instruction1(operand);
                    case 2 -> instruction2(operand);
                    case 3 -> instruction3(operand);
                    case 4 -> instruction4();
                    case 5 -> instruction5(operand);
                    case 6 -> instruction6(operand);
                    case 7 -> instruction7(operand);
                    default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
                }

                instructionPointer += 2;

                if (!output.isEmpty() && !programStartsWithOutput(output)) break;
            }

            if (output.endsWith(",")) {
                output = output.substring(0, output.length() - 1);
                if (output.equals(arrayToString(program))) {
                    return String.valueOf(i);
                }
            }

            System.out.println(i);
        }
    }

    /**
     * The value of a combo operand can be found as follows:
     * <p>
     * Combo operands 0 through 3 represent literal values 0 through 3.
     * Combo operand 4 represents the value of register A.
     * Combo operand 5 represents the value of register B.
     * Combo operand 6 represents the value of register C.
     * Combo operand 7 is reserved and will not appear in valid programs.
     *
     * @param comboOperand The combo operand to evaluate.
     * @return The value of the combo operand.
     */
    private long comboOperandValue(int comboOperand) {
        return switch (comboOperand) {
            case 0, 1, 2, 3 -> comboOperand;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            default -> throw new IllegalArgumentException("Invalid value: " + comboOperand);
        };
    }

    /**
     * The adv instruction (opcode 0) performs division. The numerator is the
     * value in the A register. The denominator is found by raising 2 to the power
     * of the instruction's combo operand. (So, an operand of 2 would divide A by
     * 4 (2^2); an operand of 5 would divide A by 2^B.) The result of the division
     * operation is truncated to an integer and then written to the A register.
     */
    private void instruction0(int comboOperand) {
        long numerator = registerA;
        long denominator = (long) Math.floor(Math.pow(2, comboOperandValue(comboOperand)));

        registerA = numerator / denominator;
    }

    /**
     * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and
     * the instruction's literal operand, then stores the result in register B.
     */
    private void instruction1(int literalOperand) {
        registerB = registerB ^ literalOperand;
    }

    /**
     * The bst instruction (opcode 2) calculates the value of its combo operand
     * modulo 8 (thereby keeping only its lowest 3 bits), then writes that value
     * to the B register.
     */
    private void instruction2(int comboOperand) {
        registerB = comboOperandValue(comboOperand) % 8;
    }

    /**
     * The jnz instruction (opcode 3) does nothing if the A register is 0.
     * However, if the A register is not zero, it jumps by setting the instruction
     * pointer to the value of its literal operand; if this instruction jumps, the
     * instruction pointer is not increased by 2 after this instruction.
     */
    private void instruction3(int literalOperand) {
        if (registerA != 0) {
            instructionPointer = literalOperand;
            instructionPointer -= 2; // Decrement by 2 to counteract the increment in the main loop
        }
    }

    /**
     * The bxc instruction (opcode 4) calculates the bitwise XOR of register B and
     * register C, then stores the result in register B. (For legacy reasons, this
     * instruction reads an operand but ignores it.)
     */
    private void instruction4() {
        registerB = registerB ^ registerC;
    }

    /**
     * The out instruction (opcode 5) calculates the value of its combo operand
     * modulo 8, then outputs that value. (If a program outputs multiple values,
     * they are separated by commas.)
     */
    private void instruction5(int comboOperand) {
        output += (comboOperandValue(comboOperand) % 8) + ",";
    }

    /**
     * The bdv instruction (opcode 6) works exactly like the adv instruction
     * except that the result is stored in the B register. (The numerator is still
     * read from the A register.)
     */
    private void instruction6(int comboOperand) {
        long numerator = registerA;
        long denominator = (long) Math.floor(Math.pow(2, comboOperandValue(comboOperand)));

        registerB = numerator / denominator;
    }

    /**
     * The cdv instruction (opcode 7) works exactly like the adv instruction
     * except that the result is stored in the C register. (The numerator is still
     * read from the A register.)
     */
    private void instruction7(int comboOperand) {
        long numerator = registerA;
        long denominator = (long) Math.floor(Math.pow(2, comboOperandValue(comboOperand)));

        registerC = numerator / denominator;
    }

    private boolean programStartsWithOutput(String output) {
        return arrayToString(program).startsWith(output.substring(0, output.length() - 1));
    }

    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]).append(",");
        }
        sb.append(array[array.length - 1]);

        return sb.toString();
    }

    private void resetAll() {
        registerA = 0;
        registerB = registerB_initial;
        registerC = registerC_initial;

        instructionPointer = 0;

        output = "";
    }

    private void printRegistersAndProgram() {
        printRegisters();
        System.out.println();
        printProgram();
    }

    private void printRegisters() {
        System.out.println("Register A: " + registerA + "\nRegister B: " + registerB + "\nRegister C: " + registerC);
    }

    private void printProgram() {
        System.out.print("Program: ");
        for (int i = 0; i < program.length - 1; i++) {
            System.out.print(program[i] + ",");
        }
        System.out.println(program[program.length - 1]);
    }
}