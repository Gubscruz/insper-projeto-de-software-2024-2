package br.insper.aposta.aposta;

import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApostaService {

   @Autowired
   private ApostaRepository apostaRepository;

   public void salvar(Aposta aposta) {
      aposta.setId(UUID.randomUUID().toString());

      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
                "http://3.81.8.254/partida/" + aposta.getIdPartida(),
                RetornarPartidaDTO.class);

      if (partida.getStatusCode().is2xxSuccessful())  {
         apostaRepository.save(aposta);
      }

   }

   public List<Aposta> listar(String status) {
      List<Aposta> apostas = apostaRepository.findAll();

      if (status != null){
         List<Aposta> lista = new ArrayList<>();

         for (Aposta aposta : apostas){
            if (aposta.getStatus().equals(status)){
					lista.add(aposta);

            }
         }
         return lista;
      }
      return apostas;
   }

   public Aposta getAposta(String id) {
      return apostaRepository.findById(id).orElse(null);
   }

   public void handleAposta(Aposta aposta){
      if (aposta.getStatus().equals("REALIZADA")) {
         RestTemplate restTemplate = new RestTemplate();

         ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
               "http://3.81.8.254/partida/" + aposta.getIdPartida(),
               RetornarPartidaDTO.class);

         if (partida.getStatusCode().is2xxSuccessful()){
            RetornarPartidaDTO partidaDTO = partida.getBody();
            String resultado;

            assert partidaDTO != null;
            if (partidaDTO.getStatus().equals("REALIZADA")){

               if (partidaDTO.getPlacarMandante().equals(partidaDTO.getPlacarVisitante())){
                  resultado = "EMPATE";
               }
               else if (partidaDTO.getPlacarMandante() > partidaDTO.getPlacarVisitante()) {
                  resultado = "VITORIA_MANDANTE";
               } else {
                  resultado = "VITORIA_VISITANTE";
               }

               if (aposta.getResultado().equals(resultado)){
                  aposta.setStatus("GANHOU");
               } else{
                  aposta.setStatus("PERDIDA");
               }

            } else {
					throw new IllegalStateException("A partida ainda não foi realizada");
				}
         } else{
            throw new IllegalStateException("Erro ao procurar dados da partida: " + partida.getStatusCode());
			}
      }
   }
}