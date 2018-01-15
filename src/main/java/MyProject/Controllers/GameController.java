package myProject.controllers;

import java.util.concurrent.atomic.AtomicInteger;
import myProject.engine.*;
import myProject.models.GameContainer;
import myProject.models.GameState;
import myProject.models.TreeHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/game")
public class GameController 
{
	private final AtomicInteger counter = new AtomicInteger();
	private GameRepository gameRepository = new GameRepository();
	
	//http://localhost:8080/game/1/move
	@RequestMapping(method = RequestMethod.GET, value = "/{gameId}/move")
	@ResponseBody
    public GameState makeMove(@PathVariable("gameId") int gameId, @RequestParam int columnId) 
	{
		GameContainer gameContainer = gameRepository.getGameContainer(gameId);
		gameContainer.makeMove(columnId);
		return gameContainer.getState();
    }
	
	//http://localhost:8080/game/start
	@RequestMapping(method = RequestMethod.GET, value = "/start")
	@ResponseBody
    public GameState restartGame(@RequestParam boolean computerStarts) 
	{
		GameContainer gameContainer = gameRepository.createGameContainer(counter.incrementAndGet());
		if(computerStarts)
		{
			gameContainer.makeMove();
		}
		return gameContainer.getState();
    }
	
	//http://localhost:8080/game/1/state
	@RequestMapping(method = RequestMethod.GET, value = "/{gameId}/state")
	@ResponseBody
    public GameState getState(@PathVariable("gameId") int gameId) 
	{
		GameContainer gameContainer = gameRepository.getGameContainer(gameId);
		return gameContainer.getState();
    }	
	
	//http://localhost:8080/game/test
	@RequestMapping(method = RequestMethod.GET, value = "/test")
	@ResponseBody
    public void test(@RequestParam boolean computerStarts) 
	{
		GameContainer gameContainer = gameRepository.createGameContainer(counter.incrementAndGet());
		
		new TreeHelper().getTree(computerStarts);
    }
}