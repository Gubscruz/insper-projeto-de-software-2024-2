package br.insper.loja.time.service;


import br.insper.loja.time.model.Time;
import br.insper.loja.time.repository.TimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTests {

	@InjectMocks
	private TimeService timeService;

	@Mock
	private TimeRepository timeRepository;


	@Test
	public void listarTimes(){

		// preparacao:
		Mockito.when(timeRepository.findAll()).thenReturn(new ArrayList<>());


		// chamada do codigo testado
		List<Time> times = timeService.listarTimes(null);


		// verificacao dos resultados
		Assertions.assertTrue(times.isEmpty());


	}




}
