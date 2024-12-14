// Events
export enum BallEvent {
    RACKET = 'RACKET',
    BOARD = 'BOARD',
    NET = 'NET',
    OUT = 'OUT',
}

export enum Player {
    A = 'A',
    B = 'B',
}

export type GameEvent = {
    type: BallEvent;
    player?: Player;
    timestamp: number;
};

// State: Latest ball event gives name
export enum RallyState {
    // Serve states
    TO_SERVE = 'TO_SERVE',
    SERVE_RACKET = 'SERVE_RACKET',
    SERVE_OWN_BOARD = 'SERVE_OWN_BOARD',
    SERVE_NET = 'SERVE_NET',
    SERVE_BOARD = 'SERVE_BOARD',
    // Return states
    TO_STRIKE = 'TO_STRIKE',
    RACKET = 'RACKET',
    NET = 'NET',
    BOARD = 'BOARD',
}

export type GameState = {
    points: Record<Player, number>,
    rallyState: RallyState,
    player: Player, // a player owns the ball until the next player should hit it
    firstServer: Player,
}