package dataflaskslite.api;

import pt.minha.api.sim.Global;

import java.util.List;

/**
 * Created by mm on 10/10/2016.
 */
@Global
public interface Storage {

    void put(byte[] key, byte[] value);
    void put(byte[] key, byte[] value, long version);

    byte[] get(byte[] key);
    byte[] get(byte[] key, long version);

    byte[] delete(byte[] key);
    byte[] delete(byte[] key, long version);

    List<byte[]> scan(byte[] start, byte[] stop);
}
