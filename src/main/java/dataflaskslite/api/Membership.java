package dataflaskslite.api;

import pt.minha.api.sim.Global;

import java.util.List;

/**
 * Created by mm on 10/10/2016.
 */
@Global
public interface Membership {

    List<Peer> getView();
}
