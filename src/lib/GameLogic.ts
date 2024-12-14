import {BallEvent, GameEvent, GameState, Player, RallyState} from "@/lib/types";

export function newGame(firstServer: Player): GameState {
    return {
        points: {[Player.A]: 0, [Player.B]: 0},
        rallyState: RallyState.TO_SERVE,
        player: Player.A,
        firstServer
    }
}

export function opponent(player: Player): Player {
    return player === Player.A ? Player.B : Player.A;
}

export function addPoint(current: GameState, player: Player): GameState {
    const newPoints = current.points[player] + 1;
    return {
        points: {...current.points, [player]: newPoints},
        rallyState: RallyState.TO_SERVE,
        player: (newPoints + current.points[opponent(player)] % 4) < 2 ? 
            current.firstServer : 
            opponent(current.firstServer),
        firstServer: current.firstServer,
    }
}