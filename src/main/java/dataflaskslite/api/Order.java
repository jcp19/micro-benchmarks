package dataflaskslite.api;

import pt.minha.api.sim.Global;

/**
 * Created by mm on 10/10/2016.
 */
@Global
public interface Order {

    void send(byte[] msg);
    byte[] receive();
}
