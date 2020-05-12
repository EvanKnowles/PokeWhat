package za.co.knowles.pokewhat.tie;

import za.co.knowles.pokewhat.domain.HandResult;

import java.util.List;

public interface ITieResolver {

    List<HandResult> resolve(List<HandResult> inputs);

}
