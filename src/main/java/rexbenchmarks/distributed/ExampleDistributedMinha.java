package rexbenchmarks.distributed;

import pt.minha.api.*;
import pt.minha.api.Process;
import pt.minha.api.sim.Simulation;

public class ExampleDistributedMinha {

    public static void main(String args[]){
        int SENDERS = 4;

        try {
            World world = new Simulation();

            //setup receiver node
            Host hostR = world.createHost();
            Process procR = hostR.createProcess();
            Entry<Main> nodeR = procR.createEntry();

            nodeR.queue().main(Receiver.class.getName(),"NR", hostR.getAddress().getHostAddress(), String.valueOf(2180), String.valueOf(SENDERS));


            //setup sender nodes
            for(int i = 0; i < SENDERS; i++){
                Host hostS = world.createHost();
                Process procS = hostS.createProcess();
                Entry<Main> nodeS = procS.createEntry();

                nodeS.call().main(Sender.class.getName(),"N"+i,hostS.getAddress().getHostAddress(),String.valueOf(2181+i), String.valueOf(i), hostR.getAddress().getHostAddress(),"2180" );
            }//*/

            world.run();
            world.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
