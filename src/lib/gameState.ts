// lib/redisGameState.ts
import { Redis } from 'ioredis';

type GameState = {
    player1Score: number;
    player2Score: number;
    serving: 1 | 2;
    gameActive: boolean;
};

class RedisGameStateManager {
    private static instance: RedisGameStateManager;
    private redis: Redis;
    private subscribers: ((state: GameState) => void)[] = [];
    private readonly gameStateKey = 'table_tennis:gameState';

    private constructor() {
        this.redis = new Redis(process.env.REDIS_URL || 'redis://localhost:6379');

        // Subscribe to Redis updates
        const subscriber = this.redis.duplicate();
        subscriber.subscribe('game_updates', (err) => {
            if (err) console.error('Failed to subscribe:', err);
        });

        subscriber.on('message', async (channel, message) => {
            if (channel === 'game_updates') {
                const state = JSON.parse(message);
                this.notifySubscribers(state);
            }
        });
    }

    static getInstance() {
        if (!RedisGameStateManager.instance) {
            RedisGameStateManager.instance = new RedisGameStateManager();
        }
        return RedisGameStateManager.instance;
    }

    async getCurrentState(): Promise<GameState> {
        const state = await this.redis.get(this.gameStateKey);
        if (!state) {
            const initialState: GameState = {
                player1Score: 0,
                player2Score: 0,
                serving: 1,
                gameActive: false,
            };
            await this.redis.set(this.gameStateKey, JSON.stringify(initialState));
            return initialState;
        }
        return JSON.parse(state);
    }

    subscribe(callback: (state: GameState) => void) {
        this.subscribers.push(callback);
        return () => {
            this.subscribers = this.subscribers.filter(cb => cb !== callback);
        };
    }

    private notifySubscribers(state: GameState) {
        this.subscribers.forEach(callback => callback(state));
    }

    async updateScore(player: 1 | 2) {
        const currentState = await this.getCurrentState();
        const newState = {
            ...currentState,
            [`player${player}Score`]: currentState[`player${player}Score`] + 1
        };

        await this.redis.set(this.gameStateKey, JSON.stringify(newState));
        await this.redis.publish('game_updates', JSON.stringify(newState));
    }

    async startGame() {
        const currentState = await this.getCurrentState();
        const newState = { ...currentState, gameActive: true };

        await this.redis.set(this.gameStateKey, JSON.stringify(newState));
        await this.redis.publish('game_updates', JSON.stringify(newState));
    }

    async resetGame() {
        const newState: GameState = {
            player1Score: 0,
            player2Score: 0,
            serving: 1,
            gameActive: false,
        };

        await this.redis.set(this.gameStateKey, JSON.stringify(newState));
        await this.redis.publish('game_updates', JSON.stringify(newState));
    }
}

// app/api/score/route.ts
// Only the GET handler needs to change slightly to handle async getCurrentState
export async function GET() {
    const encoder = new TextEncoder();
    const stream = new ReadableStream({
        async start(controller) {
            const gameState = RedisGameStateManager.getInstance();

            // Send initial state
            const initialState = await gameState.getCurrentState();
            controller.enqueue(encoder.encode(`data: ${JSON.stringify(initialState)}\n\n`));

            // Subscribe to updates
            gameState.subscribe((state) => {
                controller.enqueue(encoder.encode(`data: ${JSON.stringify(state)}\n\n`));
            });
        }
    });

    return new Response(stream, {
        headers: {
            'Content-Type': 'text/event-stream',
            'Cache-Control': 'no-cache',
            'Connection': 'keep-alive',
        },
    });
}