export type GameEvent = {
    type: 'RACKET_HIT_A' | 'RACKET_HIT_B' | 'BOARD_BOUNCE_A' | 'BOARD_BOUNCE_B' | 'NET' | 'OUT';
    timestamp: number;
};