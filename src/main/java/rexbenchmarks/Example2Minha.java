package rexbenchmarks;

import pt.minha.api.Entry;
import pt.minha.api.Host;
import pt.minha.api.Process;
import pt.minha.api.World;
import pt.minha.api.sim.Simulation;

public class Example2Minha implements IExample {
    public void mainMinha(String[] args) {

    }

    //TODO: Add signal and wait to Minha
    public static void main(String[] args) {
        try {
            World world = new Simulation();
            Host host = world.createHost();
            Process proc = host.createProcess();
            Entry<IExample> example = proc.createEntry(IExample.class, Example2.class.getName());
            example.call().main(new String[]{});
            world.run();
            world.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


