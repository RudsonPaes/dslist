package com.devsuperior.dslist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;

@Service
public class GameListService {

    private final WebMvcConfigurer corsConfigurer;
	
	@Autowired
	private GameListRepository gameListRepository;
	
	@Autowired
	private GameRepository	gameRepository;

    GameListService(WebMvcConfigurer corsConfigurer) {
        this.corsConfigurer = corsConfigurer;
    }

	@Transactional(readOnly = true)
	public List<GameListDTO> findAll() {
		List<GameList> result = gameListRepository.findAll();
		return result.stream().map(x -> new GameListDTO(x)).toList();
	}
	
		@Transactional
		public void move(Long listId, int  sourceIbdex, int destinationIndex) {
		
			List<GameMinProjection> list = gameRepository.searchByList(listId);
			
			GameMinProjection obj = list.remove(sourceIbdex);
			list.add(destinationIndex, obj);
			
		int min = 	sourceIbdex < destinationIndex ? sourceIbdex : destinationIndex;
		int max = 	sourceIbdex < destinationIndex ? destinationIndex : sourceIbdex;
		
		for (int i = min; i <= max; i++) {
			gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
	}

}
}