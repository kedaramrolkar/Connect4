# Connect4

Enter cmd into the base project directory containing the README.md file

Compile the project:
> mvn compile

Test the project:
> mvn test

Package the project:
> mvn package

Run the packaged jar:
> java -jar target\Connect4-0.1.0.jar


# NOTE: 
The project is madeup of web api, hence to use the services you will need to use proper REST Client.
e.g. Advanced REST Client for Chrome.
Also you can see the game prints on console..

# start game

To start/restart game:

> GET http://localhost:8080/game/start?computerStarts={boolean}

Note: {boolean} is 'true' or 'false'. E.g. /start?computerStarts=false

RESPONSE:

> 200 OK: Accept with Id for the game with the latest move, if computer was first.

# Make Move:

To make a move for columnId '0':

> GET http://localhost:8080/game/1/move?columnId=0

RESPONSE:
> 200 OK: if valid game wit

> 404 CONFLICT: invalid move columnId. or the game has ended already.

> 404 NOT_FOUND: invalid game 'Id'

# Get current status

> GET http://localhost:8080/game/1/state

RESPONSE:

> 200 OK: current state of game, and latest move

> 404 NOT_FOUND: invalid game 'Id'


AI for the game:

I used dynamic programming approach for the game, i.e, figuring all possible combinations of a move and its in its depth. The technique is to maximize the end win conditions of a move and make the best move possible.
But the solution is NP hence need to use prunning and approximation techniques.

Techniques used:
1) Same smaller subproblems:
Store the results of a current game in hashmap and use the result if the same condition is seen later in other subproblems. Huge advantage in avoiding recomputation.

2) Mirror pruning:
A state of a game is same if looked from the other side(mirror). Hence we can reduce the whole computation by 2 factor using mirrioring subproblems.

3) Immediate win termination:
If its computers move and a immediate move is a win, make the move and do not solve the other moves. Reduces unnecessary computation.

4) 10 moves depth:
Even after these techniques, the solution is NP. Hence I pruned the depth height to 10 moves. So the AI with compute all combinations uptill 10 moves and return the best move to maximize the end win combinations.

I have tested the AI, it works well. I am further adding few move hueristic techniques to increase win rate.