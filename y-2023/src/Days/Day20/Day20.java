package Days.Day20;

import General.Day;
import General.Helper;
import Utils.LCM;

import java.io.File;
import java.util.*;

public class Day20 implements Day {
    private final List<Module> modules = new java.util.ArrayList<>();
    private final HashMap<String, Module> moduleMap = new HashMap<>();
    private final Queue<PulseInfo> pulseQueue = new java.util.LinkedList<>();
    private static final boolean LOW_PULSE = false;
    private int csHighPulse = 0; // For part 2

    private record PulseInfo(boolean pulse, Module sourceModule, Module destinationModule) {
        @Override
        public String toString() {
            String name = sourceModule == null ? "button" : sourceModule.name;
            String pulse = this.pulse ? "high" : "low";
            String destinationModule = this.destinationModule == null ? "output" : this.destinationModule.name;

            return name + " -" + pulse + "-> " + destinationModule;
        }
    }

    /*
     * Modules communicate using pulses. Each pulse is either a high pulse or a low pulse.
     * When a module sends a pulse, it sends that type of pulse to each module in its list of destination modules.
     */
    class Module {
        String name;
        List<Module> destinationModules = new java.util.ArrayList<>();

        // Send a pulse to the queue to be sent to all destination modules later
        void sendPulse(boolean pulse) {
            for (Module destinationModule : destinationModules) {
                pulseQueue.add(new PulseInfo(pulse, this, destinationModule));
            }
        }

        // Receive a pulse from a source module
        void receivePulse(PulseInfo pulseInfo) {
            throw new UnsupportedOperationException("receivePulse() not implemented");
        }
    }

    /*
     * Flip-flop modules are either on or off; they are initially off.
     * -> If a ff module receives a high pulse, it is ignored (nothing happens).
     * -> If a ff module receives a low pulse, it flips between on and off.
     * ----> If it was off, it turns on and sends a high pulse.
     * ----> If it was on, it turns off and sends a low pulse.
     */
    class FlipFlop extends Module {
        boolean isOn;

        @Override
        void receivePulse(PulseInfo pulseInfo) {
            boolean pulse = pulseInfo.pulse();

            if (pulse == LOW_PULSE) {
                flip();
                sendPulse(isOn); // ON -> HIGH pulse; OFF -> LOW pulse
            }
        }

        // Flip the state of the flip-flop module
        void flip() {
            isOn = !isOn;
        }
    }

    /*
     * Conjunction modules remember the type of the most recent pulse received from each of their connected input modules;
     * They initially default to remembering a low pulse for each input. When a pulse is received, the conjunction module
     * first updates its memory for that input. Then, if it remembers high pulses for all inputs, it sends a low pulse;
     * otherwise, it sends a high pulse.
     */
    class Conjunction extends Module {
        List<Module> inputModules = new java.util.ArrayList<>();
        boolean[] inputPulses;

        @Override
        void receivePulse(PulseInfo pulseInfo) {
            if (inputPulses == null) init();

            // Update the pulse memory for the input module that sent the pulse
            int i = inputModules.indexOf(pulseInfo.sourceModule);
            inputPulses[i] = pulseInfo.pulse;

            // Check if all input pulses are high
            boolean allHigh = true;
            for (boolean inputPulse : inputPulses) {
                if (inputPulse == LOW_PULSE) {
                    allHigh = false;
                    break;
                }
            }

            // Send a pulse to all destination modules
            sendPulse(!allHigh); // allHigh -> LOW pulse; !allHigh -> HIGH pulse
        }

        void init() {
            inputPulses = new boolean[inputModules.size()];
        }
    }

    /*
     * When a broadcast module receives a pulse, it sends the same pulse to all of its destination modules.
     */
    class Broadcast extends Module {
        @Override
        void receivePulse(PulseInfo pulseInfo) {
            sendPulse(pulseInfo.pulse);
        }
    }

    public static void main(String[] args) {
        Day day20 = new Day20();
        day20.loadData(Helper.filename(20));
        System.out.println(day20.part1());
        System.out.println(day20.part2());
    }

    @Override
    public void loadData(String filename) {
        try {
            File input = new File(filename);

            // Parse the input twice

            // First, create all modules
            Scanner sc1 = new Scanner(input);

            while (sc1.hasNextLine()) {
                String module = sc1.nextLine();
                module = module.substring(0, module.indexOf(" "));

                if (module.equals("broadcaster")) {
                    Broadcast broadcast = new Broadcast();
                    broadcast.name = module;
                    modules.add(broadcast);
                    moduleMap.put(module, broadcast);
                } else if (module.startsWith("%")) {
                    module = module.substring(1);
                    FlipFlop flipFlop = new FlipFlop();
                    flipFlop.name = module;
                    modules.add(flipFlop);
                    moduleMap.put(module, flipFlop);
                } else if (module.startsWith("&")) {
                    module = module.substring(1);
                    Conjunction conjunction = new Conjunction();
                    conjunction.name = module;
                    modules.add(conjunction);
                    moduleMap.put(module, conjunction);
                } else {
                    throw new Exception("Invalid module type: " + module);
                }
            }

            // Second, connect all modules (build the graph)
            Scanner sc2 = new Scanner(input);

            int i = 0;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                line = line.substring(line.indexOf(" -> ") + 4);

                Module sourceModule = modules.get(i);
                String[] destinations = line.trim().split(", ");

                for (String destination : destinations) {
                    Module destinationModule = moduleMap.get(destination);

                    sourceModule.destinationModules.add(destinationModule);

                    if (destinationModule instanceof Conjunction) {
                        ((Conjunction) destinationModule).inputModules.add(sourceModule);
                    }
                }

                i++;
            }

            sc1.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    @Override
    public String part1() {
        int lowPulse = 0, highPulse = 0;

        for (int i = 0; i < 1000; i++) {
            int[] pulses = pushButton();

            lowPulse += pulses[0];
            highPulse += pulses[1];
        }

        return String.valueOf(lowPulse * highPulse);
    }

    @Override
    public String part2() {
        reset();

        int buttonPresses = 0;
        List<Integer> denominators = new ArrayList<>();

        do {
            csHighPulse = 0;

            buttonPresses++;
            pushButton();

            if (csHighPulse != 0) denominators.add(buttonPresses);
        } while (denominators.size() < 4);

        return String.valueOf(LCM.findLCM(denominators));
    }

    // For part 2
    public void reset() {
        modules.clear();
        moduleMap.clear();

        loadData(Helper.filename(20));
    }

    // When you push the button, a single low pulse is sent directly to the broadcaster module
    private int[] pushButton() {
        pulseQueue.add(new PulseInfo(LOW_PULSE, null, moduleMap.get("broadcaster")));

        int lowPulse = 0, highPulse = 0;
        while (!pulseQueue.isEmpty()) {
            PulseInfo pulseInfo = pulseQueue.poll();

            // For part 1 (keep track of the number of low and high pulses)
            if (pulseInfo.pulse) highPulse++;
            else lowPulse++;
            // System.out.println(pulseInfo);

            if (pulseInfo.destinationModule != null) {
                pulseInfo.destinationModule.receivePulse(pulseInfo);

                // For part 2
                if (pulseInfo.destinationModule.name.equals("cs") && pulseInfo.pulse) {
                    csHighPulse++;
                }
            }
        }

        return new int[]{lowPulse, highPulse};
    }
}