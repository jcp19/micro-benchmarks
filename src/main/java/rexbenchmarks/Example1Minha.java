package rexbenchmarks;

import pt.minha.api.*;
import pt.minha.api.Process;
import pt.minha.api.sim.Simulation;

public class Example1Minha {
    public static void main(String[] args) {
        try {
            World world = new Simulation();
            Host host = world.createHost();
            Process proc = host.createProcess();

            Entry<Main> example = proc.createEntry();
            example.call().main(Example1.class.getName(), new String[]{});

            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}


