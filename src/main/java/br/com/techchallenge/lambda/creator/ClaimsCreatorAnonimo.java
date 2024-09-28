package br.com.techchallenge.lambda.creator;

import java.util.HashMap;
import java.util.Map;

public class ClaimsCreatorAnonimo extends AbstractClaimsCreator {

    public Map<String,String> get() {
        Map<String,String> map = new HashMap<>();
        map.put("tipo", "anonimo");
        return map;
    }
}
