// Events
export enum BallEvent {
    Racket = 'Racket',
    Board = 'Board',
    Net = 'Net',
    Out = 'Out',
}

export enum Player {
    A = 'A',
    B = 'B',
}

export type GameEvent = {
    ballEvent: BallEvent;
    player?: Player;
    timestamp: number;
};

export type GameHistory = {
    event: GameEvent;
    state: GameState;
}

// State: Latest ball event gives name
export enum RallyState {
    // Serve states
    ToServe = 'ToServe',
    ServeRacket = 'ServeRacket',
    ServeOwnBoard = 'ServeOwnBoard',
    ServeNet = 'ServeNet',
    ServeBoard = 'ServeBoard',
    // Return states
    ToStrike = 'ToStrike',
    Racket = 'Racket',
    Net = 'Net',
    Board = 'Board',
}

export type GameState = {
    points: Record<Player, number>,
    rallyState: RallyState,
    possession: Player, // a player possesses the ball until the next player should hit it
    firstServer: Player,
}