package myProject.models;

public class GameHelper
{
	public static void CheckStatus(GameState gameState, Move lastMove, int[] columnCount)
	{
		int rowId = lastMove.rowId, columnId = lastMove.columnId;
		BlockStateEnum player = lastMove.player;
		int winCount = 4;
		
		if(rowId == -1 || columnId == -1 || player == BlockStateEnum.EMPTY)
		{
			return;
		}
		
		boolean available = false;
		for(int i=0;i<GameContainer.COLUMN;i++)
		{
			if(columnCount[i] < GameContainer.ROW)
			{
				available = true;
				break;
			}
		}
		if(!available)
		{
			gameState.winStatus = BlockStateEnum.TIED;
			return;
		}
		
		// Check vertical
		int count = 1;
		for(int i = rowId+1; i < GameContainer.ROW && gameState.get(i, columnId) == player; i++, count++);
		for(int i = rowId-1; i >= 0 && gameState.get(i, columnId) == player; i--, count++);
		if(count >= winCount)
		{
			gameState.winStatus = player;
			return;
		}
		
		// Check horizontal
		count = 1;
		for(int j = columnId+1; j < GameContainer.COLUMN && gameState.get(rowId, j) == player; j++, count++);
		for(int j = columnId-1; j >= 0 && gameState.get(rowId, j) == player; j--, count++);
		if(count >= winCount)
		{
			gameState.winStatus = player;
			return;
		}
		
		// Check diagonal
		count = 1;
		for(int i = rowId+1, j = columnId+1; i < GameContainer.ROW && j < GameContainer.COLUMN && gameState.get(i, j) == player; i++,j++,count++);
		for(int i = rowId-1, j = columnId-1; i >= 0 && j >= 0 && gameState.get(i, j) == player; i--,j--,count++);
		if(count >= winCount)
		{
			gameState.winStatus = player;
			return;
		}
		
		// Check diagonal
		count = 1;
		for(int i = rowId+1, j = columnId-1; i < GameContainer.ROW && j >= 0 && gameState.get(i, j) == player; i++,j--,count++);
		for(int i = rowId-1, j = columnId+1; i >= 0 && j < GameContainer.COLUMN && gameState.get(i, j) == player; i--,j++,count++);
		if(count >= winCount)
		{
			gameState.winStatus = player;
			return;
		}
	}
}
