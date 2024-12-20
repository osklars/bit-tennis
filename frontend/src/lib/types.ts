// Events
export enum EventType {
    Throw = 'Throw', ExitTable = 'ExitTable', Net = 'Net', // serves
    Racket = 'Racket', Board = 'Board', Out = 'Out',
}

export enum Player {
    A = 'A',
    B = 'B',
}

export type Event = {
    event: EventType,
    player?: Player,
};

export type NewMatch = {
    playerA: string,
    playerB: string,
    bestOf: number,
    firstServer: Player,
}


// State: Latest ball event gives name
export enum RallyState {
    // Serve states
    Idle = 'Idle',
    ToServe = 'ToServe',
    ServeRacket = 'ToBounce1',
    ServeOwnBoard = 'ToBounce2',
    ServeNet = 'NetServe',
    // Return states
    ToStrike = 'ToStrike',
    Board = 'ToBounce',
}

export type Points = {
    A: number,
    B: number,
}

export type StateSummary = {
    latestEvent?: Event,
    rallyState: RallyState,
    possession: Player,
    gamePoints: Points,
    setPoints: Points,
}

export type ErrorResponse = {
    error: string,
}