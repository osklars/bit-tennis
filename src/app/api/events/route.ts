// app/api/events/route.ts
import { memoryStore } from '@/lib/memoryStore';
import { GameEvent } from '@/lib/types';

export async function POST(request: Request) {
    const event = await request.json() as GameEvent;
    await memoryStore.lpush('tt:events', JSON.stringify(event));
    await memoryStore.publish('game_updates', JSON.stringify(event));
    return Response.json({ success: true });
}

// import { redis } from '@/lib/redis';
// import { GameEvent } from '@/lib/types';
//
// export async function POST(request: Request) {
//     const event = await request.json() as GameEvent;
//     await redis.lpush('tt:events', JSON.stringify(event));
//     await redis.ltrim('tt:events', 0, 4); // Keep only latest 5 events
//     await redis.publish('game_updates', JSON.stringify(event));
//     return Response.json({ success: true });
// }