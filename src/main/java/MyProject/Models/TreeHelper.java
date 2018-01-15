package myProject.models;

import java.util.Scanner;
import java.util.*;

public class TreeHelper
{
	private HashMap<String, Tree> hs = new HashMap<>();
	
	private int hits;
	private int hitSavedLeafs;
	private int leafCount;
	private int wins;
	private int loses;
	private int ties;
	
	private int imm;
	
	public void getTree(boolean computerStarts)
	{
		System.out.println("testing...");
		BlockStateEnum player = (computerStarts) ? BlockStateEnum.COMPUTER : BlockStateEnum.PERSON;
		Move bestMove = new Move();
		Tree tree = getMove(new GameState(0), new int[GameContainer.COLUMN], bestMove);
		System.out.println("done..." + tree.toString());
		
		System.out.println("hits:" + hits);
		System.out.println("hitSavedLeafs:" + hitSavedLeafs);
		System.out.println("leafVisited:" + leafCount);
		System.out.println("immediate found:" + imm);
		
		
		System.out.println("wins:" + wins);
		System.out.println("loses:" + loses);
		System.out.println("ties:" + ties);
		
		System.out.println("bestMove:" + bestMove.columnId);
	}
	
	public Tree getMove(GameState gameState, int[] colCount, Move lastMove)
	{
		//System.out.println("testing...");
		Tree tree = createTree(gameState, colCount, BlockStateEnum.COMPUTER, lastMove, 0);
		/*System.out.println("done..." + tree.toString());
		
		System.out.println("hits:" + hits);
		System.out.println("hitSavedLeafs:" + hitSavedLeafs);
		System.out.println("leafVisited:" + leafCount);
		System.out.println("immediate found:" + imm);
		
		
		System.out.println("wins:" + wins);
		System.out.println("loses:" + loses);
		System.out.println("ties:" + ties);
		*/
		
		int bestCol = tree.getBestMove();
		lastMove.set(colCount[bestCol], bestCol, BlockStateEnum.COMPUTER);
		return tree;
	}

	private Tree createTree(GameState gameState, int[] columnCount, BlockStateEnum player, Move lastMove, int movesMade)
	{
		if(movesMade >= 10)
		{
			return new Tree();
		}
		
		// Look if subproblem has been prev encoutered
		String code = gameState.toString();
		Tree tree  = hs.get(code);
		//Here CC
		if(tree != null)
		{
			hits++;
			hitSavedLeafs += (tree.wins + tree.loses + tree.ties);
			//System.out.println("match found for:" + code + "returning...");
			return tree;
		}
		
		// Look if subproblem has been prev encoutered in mirror way
		String revCode = gameState.toReverseString();
		tree = hs.get(revCode);
		//Here CC
		if(tree != null)
		{
			hits++;
			hitSavedLeafs += (tree.wins + tree.loses + tree.ties);
			//System.out.println("match found for:" + code + "returning...");
			return tree;
		}
		
		tree = new Tree();
		
		//System.out.println("Checking Win");
		GameHelper.CheckStatus(gameState, lastMove, columnCount);
				
		// Game won.. so return result
		if(gameState.winStatus != BlockStateEnum.EMPTY)
		{
			//System.out.println("leaf.."+ gameState.winStatus);
			Scanner s = new Scanner(System.in);
			//s.nextInt();
			
			if(gameState.winStatus == BlockStateEnum.COMPUTER)
			{
				tree.wins = 1;
				wins++;
			}
			//Here CC
			else if(gameState.winStatus == BlockStateEnum.PERSON)
			{
				tree.loses = 1;
				loses++;
			}
			else
			{
				tree.ties = 1;
				ties++;
			}
			
			//System.out.println("storing for:" + code);
			hs.put(code, tree);
			leafCount++;
			return tree;
		}
		//System.out.println("continue");
		
		// Game not won.. so play
		// look for immediate win technique
		int bestWay = -1;
		for(int columnId=0; columnId<GameContainer.COLUMN; columnId++)
		{
			//System.out.println("for columnId:"+ columnId);
			int rowId = columnCount[columnId];
			//System.out.println("for rowId:"+ rowId);
			
			if(rowId < GameContainer.ROW)
			{
				lastMove.set(rowId, columnId, player);
				gameState.set(lastMove);
				columnCount[columnId]++;
				
				GameHelper.CheckStatus(gameState, lastMove, columnCount);
				if(gameState.winStatus == BlockStateEnum.COMPUTER)
				{
					bestWay = columnId;
					imm++;
					
					Tree subTree = createTree(gameState, columnCount, changePlayer(player), lastMove, movesMade+1);
					tree.ways[columnId] = subTree;
					
					tree.wins += subTree.wins;
					tree.loses += subTree.loses;
					tree.ties += subTree.ties;
				}
				
				//BackTrack.. reset if game over
				gameState.unSet(rowId, columnId);
				columnCount[columnId]--;
				
				if(bestWay != -1)
				{
					break;
				}
			}
		}
		
		if(bestWay == -1)
		{
			double bestWinRate = 0;
			// Game not won.. so play
			int maxWays = 0;
			for(int columnId=0; columnId<GameContainer.COLUMN; columnId++)
			{
				//System.out.println("for columnId:"+ columnId);
				int rowId = columnCount[columnId];
				//System.out.println("for rowId:"+ rowId);
				
				if(rowId < GameContainer.ROW)
				{
					lastMove.set(rowId, columnId, player);
					gameState.set(lastMove);
					columnCount[columnId]++;
					
					Tree subTree = createTree(gameState, columnCount, changePlayer(player), lastMove, movesMade+1);
					tree.ways[columnId] = subTree;
					
					
					tree.wins += subTree.wins;
					tree.loses += subTree.loses;
					tree.ties += subTree.ties;
					
					if(maxWays < subTree.wins)
					{
						maxWays = subTree.wins;
						tree.maxWay = columnId;
					}
					
					//BackTrack.. reset if game over
					gameState.unSet(rowId, columnId);
					columnCount[columnId]--;
				}
			}
		}
		
		//System.out.println("storing for:" + code);
		hs.put(code, tree);
		return tree;
	}

	private BlockStateEnum changePlayer(BlockStateEnum player) 
	{
		return (player == BlockStateEnum.COMPUTER) ? BlockStateEnum.PERSON : BlockStateEnum.COMPUTER;
	}

	class Tree
	{
		public Tree[] ways = new Tree[GameContainer.COLUMN];
		public int maxWay = -1;
		public int wins = 0;
		public int loses = 0;
		public int ties = 0;
		
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<GameContainer.COLUMN; i++ )
			{
				if(ways[i] != null)
				{
					sb.append("( wins:" + ways[i].wins + " loses:" + ways[i].loses + " ties:" + ways[i].ties + " )");
					sb.append("\n");
				}
			}
			return sb.toString();
		}
		
		public int getBestMove()
		{
			int bestCol = -1;
			double bestWinRate = 0;
			
			for(int i=0; i<GameContainer.COLUMN; i++ )
			{
				if(ways[i] != null)
				{
					if((ways[i].wins + ways[i].loses) > 0)
					{
						double winRate = (ways[i].wins) / (ways[i].wins + ways[i].loses);
						if(winRate > bestWinRate)
						{
							bestWinRate = winRate;
							bestCol = i;
						}
					}
				}
			}
			if(bestCol != -1)
			{
				return bestCol;
			}
			
			int maxLoses = Integer.MAX_VALUE;
			for(int i=0; i<GameContainer.COLUMN; i++ )
			{
				if(ways[i] != null)
				{
					if(maxLoses > ways[i].loses)
					{
						maxLoses = ways[i].loses;
						bestCol = i;
					}
				}
			}
			if(bestCol != -1)
			{
				return bestCol;
			}
			
			int maxTies = -1;
			for(int i=0; i<GameContainer.COLUMN; i++ )
			{
				if(ways[i] != null)
				{
					if(maxTies < ways[i].ties)
					{
						maxTies = ways[i].ties;
						bestCol = i;
					}
				}
			}
			return bestCol;
		}
	}
}