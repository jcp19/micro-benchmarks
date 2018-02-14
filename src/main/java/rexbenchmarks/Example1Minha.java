package rexbenchmarks;

import pt.minha.api.Entry;
import pt.minha.api.Host;
import pt.minha.api.Process;
import pt.minha.api.World;
import pt.minha.api.sim.Simulation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example1Minha {
    public static void main(String[] args) {
        try {
            World world = new Simulation();
            Host host = world.createHost();
            Process proc = host.createProcess();
            Entry<IExample> example = proc.createEntry(IExample.class, Example1.class.getName());
            example.call().main(new String[]{});
            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


