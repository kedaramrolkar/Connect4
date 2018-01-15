package myProject.engine;

import myProject.models.GameContainer;

import java.util.HashMap;
import java.lang.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public class GameRepository
{
	private HashMap<Integer, GameContainer> _gameHashMap;
	
	public GameRepository()
	{
		_gameHashMap = new HashMap<>();
	}
	
	// Gets the GameContainer by id if present
	public GameContainer getGameContainer(int gameId)
	{
		GameContainer gameContainer = _gameHashMap.get(gameId);
		if(gameContainer == null)
		{
			throw new InvalidGameIdException(gameId);
		}
		
		return gameContainer;
	}
	
	// Create the GameContainer by id
	public GameContainer createGameContainer(int gameId)
	{
		GameContainer gameContainer = new GameContainer(gameId);
		System.out.println("New game started with Id:"+gameId+ "...");
		System.out.println();
		
		_gameHashMap.put(gameId, gameContainer);
		return gameContainer;
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class InvalidGameIdException extends RuntimeException {

	public InvalidGameIdException(int gameId) {
		super("Could not find game with Id:'" + gameId + "'.");
	}
}