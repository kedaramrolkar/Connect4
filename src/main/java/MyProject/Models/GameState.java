package myProject.models;


import java.util.Scanner;
import java.util.*;

public class GameState
{
	public int gameId;
	private BlockStateEnum[][] space;
	public BlockStateEnum winStatus;
	public Move latestMove = new Move();
	
	public GameState(int gameId) 
	{
		this.gameId = gameId;
		winStatus = BlockStateEnum.EMPTY;
		
		space = new BlockStateEnum[GameContainer.ROW][GameContainer.COLUMN];
		for(int i=0;i< GameContainer.ROW;i++)
		{
			for(int j=0;j< GameContainer.COLUMN;j++)
			{
				space[i][j] = BlockStateEnum.EMPTY;
			}
		}
	}
	
	public void set(Move move)
	{
		//System.out.println("setting "+ move.rowId + " " + move.columnId + " to " + move.player);
		this.space[move.rowId][move.columnId] = move.player;
		
		latestMove.set(move);
	}
	
	public void unSet(int rowId, int columnId)
	{
		//System.out.println("unsetting "+ rowId + " " + columnId);
		this.space[rowId][columnId] = BlockStateEnum.EMPTY;
		winStatus = BlockStateEnum.EMPTY;
	}
	
	public BlockStateEnum get(int rowId, int columnId)
	{
		return this.space[rowId][columnId];
	}
	
	public void print()
	{
		System.out.println("printing Game...");
		
		for(int i=GameContainer.ROW-1 ;i>=0; i--)
		{
			for(int j=0;j< GameContainer.COLUMN;j++)
			{
				if(space[i][j] == BlockStateEnum.EMPTY)
				{
					System.out.print(" ");
				}
				else if(space[i][j] == BlockStateEnum.PERSON)
				{
					System.out.print("X");
				}
				else
				{
					System.out.print("O");
				}
				
				if(j < GameContainer.COLUMN-1)
				{
					System.out.print(" | ");
				}
			}
			System.out.println();
		}
		//System.out.println("continue...press key");
		//Scanner s = new Scanner(System.in);
		//s.nextLine();
	}
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=GameContainer.ROW-1 ;i>=0; i--)
		{
			for(int j=0;j< GameContainer.COLUMN;j++)
			{
				if(space[i][j] == BlockStateEnum.EMPTY)
				{
					sb.append(" ");
				}
				else if(space[i][j] == BlockStateEnum.PERSON)
				{
					sb.append("X");
				}
				else
				{
					sb.append("O");
				}
			}
		}
		return sb.toString();
	}
	
	public String toReverseString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=GameContainer.ROW-1 ;i>=0; i--)
		{
			for(int j=GameContainer.COLUMN-1 ;j>=0; j--)
			{
				if(space[i][j] == BlockStateEnum.EMPTY)
				{
					sb.append(" ");
				}
				else if(space[i][j] == BlockStateEnum.PERSON)
				{
					sb.append("X");
				}
				else
				{
					sb.append("O");
				}
			}
		}
		return sb.toString();
	}
}

class Move
{
	public int rowId = -1;
	public int columnId = -1;
	public BlockStateEnum player = BlockStateEnum.EMPTY;
	
	public Move()
	{
	}
	
	public Move(int rowId, int columnId, BlockStateEnum player)
	{
		this.rowId = rowId;
		this.columnId = columnId;
		this.player = player;
	}
	
	public void set(int rowId, int columnId, BlockStateEnum player)
	{
		this.rowId = rowId;
		this.columnId = columnId;
		this.player = player;
	}
	
	public void set(Move move)
	{
		this.rowId = move.rowId;
		this.columnId = move.columnId;
		this.player = move.player;
	}
}