package br.com.techchallenge.lambda.creator;

import java.util.HashMap;
import java.util.Map;

public class ClaimsCreatorCliente extends AbstractClaimsCreator {

    private final String cpf;
    private final String nome;

    public ClaimsCreatorCliente(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public Map<String,String> get() {
        Map<String,String> map = new HashMap<>();
        map.put("tipo", "cliente");
        map.put("id", cpf);
        map.put("nome", nome);
        return map;
    }
}
