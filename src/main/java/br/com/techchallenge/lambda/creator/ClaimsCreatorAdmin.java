package br.com.techchallenge.lambda.creator;

import java.util.HashMap;
import java.util.Map;

public class ClaimsCreatorAdmin extends AbstractClaimsCreator {

    private final Long id;
    private final String nome;

    public ClaimsCreatorAdmin(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Map<String,String> get() {
        Map<String,String> map = new HashMap<>();
        map.put("tipo", "admin");
        map.put("id", id.toString());
        map.put("nome", nome);
        return map;
    }

}
