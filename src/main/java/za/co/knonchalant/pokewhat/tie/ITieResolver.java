package za.co.knonchalant.pokewhat.tie;

import za.co.knonchalant.pokewhat.domain.HandResult;

import java.util.List;

public interface ITieResolver {

    List<HandResult> resolve(List<HandResult> inputs);

}
