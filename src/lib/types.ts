export enum VisionEvent {
    RACKET_A = 'RACKET_A',
    RACKET_B = 'RACKET_HIT_B',
    BOARD_A = 'BOARD_A',
    BOARD_B = 'BOARD_BOUNCE_B',
    EDGE_A = 'EDGE_A',
    EDGE_B = 'EDGE_B',
    CLIP_A = 'CLIP_A',
    CLIP_B = 'CLIP_B',
    NET = 'NET',
    OUT = 'OUT',
}

export type GameEvent = {
    type: VisionEvent | SoundEvent;
    timestamp: number;
};

export enum SoundEvent {
    RACKET_A = 'RACKET_A',
    RACKET_B = 'RACKET_HIT_B',
    BOARD_A = 'BOARD_A',
    BOARD_B = 'BOARD_BOUNCE_B',
    EDGE_A = 'EDGE_A',
    EDGE_B = 'EDGE_B',
    NET = 'NET',
    OUT = 'OUT',
}