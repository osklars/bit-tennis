// Events
export enum EventType {
    Throw = 'Throw', ExitTable = 'ExitTable', Net = 'Net', // serves
    Racket = 'Racket', Board = 'Board', Out = 'Out',
}

export enum Player {
    Red = 'Red',
    Black = 'Black',
}

export type Event = {
    event: EventType,
    player?: Player,
};

export type NewMatch = {
    playerRed: string,
    playerBlack: string,
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
    Red: number,
    Black: number,
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