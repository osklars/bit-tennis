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
    side?: Side,
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
    latestInput?: Input,
    firstServer: Player,
    gamePoints: Points,
    setPoints: Points,
    playerRed: string,
    playerBlack: string,
    bestOf: number,
}

export type ErrorResponse = {
    error: string,
}

// Input
export type Input = {
    action: InputAction,
    player: Player,
}

export enum InputAction {
    Increase = 'Increase',
    Decrease = 'Decrease',
}

export enum Side {
    Left = 'Left',
    Right = 'Right',
}