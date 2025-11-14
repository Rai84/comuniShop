package com.pi.comuniShop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeoService {

    private RestTemplate rest = new RestTemplate();

    public Map<String, Double> buscarLatLongPorCep(String cep) {
        try {
            // 1. Buscar endereço no ViaCEP
            String viaCepUrl = "https://viacep.com.br/ws/" + cep + "/json/";
            Map<String, Object> viaCep = rest.getForObject(viaCepUrl, Map.class);

            if (viaCep == null || viaCep.get("erro") != null) {
                return null;
            }

            String logradouro = viaCep.get("logradouro").toString();
            String bairro = viaCep.get("bairro").toString();
            String cidade = viaCep.get("localidade").toString();
            String uf = viaCep.get("uf").toString();

            // 2. Montar endereço completo
            String endereco = logradouro + ", " + bairro + ", " + cidade + ", " + uf + ", Brasil";

            // 3. Buscar lat/long no Nominatim
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + endereco;

            List<Map<String, Object>> response = rest.getForObject(url, List.class);

            if (response != null && !response.isEmpty()) {
                Map<String, Object> dados = response.get(0);

                Map<String, Double> resultado = new HashMap<>();
                resultado.put("lat", Double.parseDouble(dados.get("lat").toString()));
                resultado.put("lon", Double.parseDouble(dados.get("lon").toString()));

                return resultado;
            }

        } catch (Exception e) {
            System.out.println("Erro ao consultar coordenadas: " + e.getMessage());
        }

        return null;
    }
}
