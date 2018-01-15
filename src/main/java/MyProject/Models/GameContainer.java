package myProject.models;

import java.util.Scanner;
import java.util.*;
import java.lang.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Holds the data related to one stock commodity.
public class GameContainer 
{
	public static final int ROW = 6; 
	public static final int COLUMN = 7;
	
    private final int id;
	private int[] columnFilled = new int[COLUMN]; 
	private GameState gameState;
	private TreeHelper treeHelper = new TreeHelper();
	
    public GameContainer(int gameId) 
	{
		this.id = gameId;
		this.gameState = new GameState(gameId);
    }
	
    public int getId() 
	{
        return id;
    }
	
	private boolean checkMove(int columnId)
	{
		if(isEnded())
		{
			throw new InvalidMoveException("Game Ended! restart...");
		}
		
		if(columnId < 0 || columnId >= COLUMN)
		{
			throw new InvalidMoveException("ColumnId is wrong!");
		}
		
		if(columnFilled[columnId] >= ROW)
		{
			throw new InvalidMoveException("Column filled already!");
		}
		
		return true;
	}

	// This is User Move
	public void makeMove(int columnId) 
	{
		// Check if valid
		checkMove(columnId);
		
		// if valid, make the move
		Move move = new Move(columnFilled[columnId], columnId, BlockStateEnum.PERSON);
		makeMove(move);
		
		// if game not ended, make computer move
		if(!isEnded())
		{
			makeMove();
		}
	}
	
	// This is Computer move
	public void makeMove()
	{
		// Check is any move available
		int columnId = findEmptyColumn();
		Move move = new Move(columnFilled[columnId], columnId, BlockStateEnum.COMPUTER);
		makeMove(move);
	}
	
	private void makeMove(Move move)
	{
		gameState.set(move);
		columnFilled[move.columnId]++;
		GameHelper.CheckStatus(gameState, move, columnFilled);
		gameState.print();
		
		//System.out.println("Game ended: "+ isEnded());
		//System.out.println();
	}
	
	private int findEmptyColumn()
	{
		System.out.println("finding best move for lastMove..."+ gameState.latestMove.columnId);
		
		treeHelper.getMove(gameState, columnFilled, gameState.latestMove);
		if(gameState.latestMove.columnId != -1)
		{
			System.out.println("found move..."+ gameState.latestMove.columnId);
			return gameState.latestMove.columnId;
		}
		
		throw new InvalidStateFatalException();
	}
	
	public GameState getState()
	{
		return this.gameState;
	}
	
	private boolean isEnded()
	{
		return this.gameState.winStatus != BlockStateEnum.EMPTY;
	}
}

enum Status
{
	Waiting,
	Ended
}

@ResponseStatus(HttpStatus.CONFLICT)
class InvalidMoveException extends RuntimeException {

	public InvalidMoveException(String s) {
		super(s);
	}
}

@ResponseStatus(HttpStatus.CONFLICT)
class InvalidStateFatalException extends RuntimeException {

	public InvalidStateFatalException() {
		super("Invalid state reached! Computer cant make a move");
	}
}