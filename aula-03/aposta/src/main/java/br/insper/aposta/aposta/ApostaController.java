package br.insper.aposta.aposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    @Autowired
    private ApostaService apostaService;

    @GetMapping
    public List<Aposta> listar(@RequestParam(required = false) String status)  {
        return apostaService.listar(status);
    }

    @PostMapping
    public void salvar(@RequestBody Aposta aposta) {
        apostaService.salvar(aposta);
    }

    @GetMapping("/{idAposta}")
    @ResponseStatus(HttpStatus.OK)
    public void resultado(@PathVariable String idAposta) {
        Aposta aposta = apostaService.getAposta(idAposta);
        apostaService.handleAposta(aposta);
    }
}
