package rexbenchmarks;

import pt.minha.api.*;
import pt.minha.api.Process;
import pt.minha.api.sim.Simulation;

public class Example2Minha {
    public static void main(String[] args) {
        try {
            World world = new Simulation();
            Host host = world.createHost();
            Process proc = host.createProcess();

            Entry<Main> example = proc.createEntry();
            example.call().main(Example2.class.getName(), new String[]{});

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}


