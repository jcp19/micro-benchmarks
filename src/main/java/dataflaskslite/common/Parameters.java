package dataflaskslite.common;

/**
 * Created by nunomachado on 10/05/17.
 */
public class Parameters {

    //ATTENTION: these are hardcoded parameters that must be tuned according to the deployment.
    public static int NUMPEERS = 5;
    public static long NUMCYCLES = 5;
    public static long INTERVAL = 1000;
    public static int VIEWSIZE = 3;//(int)Math.ceil(NUMPEERS/2);
    public static int GOSSIPSIZE = VIEWSIZE-1;
}
