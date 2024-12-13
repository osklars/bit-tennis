// app/api/events/stream/route.ts
import { memoryStore } from '@/lib/memoryStore';
import { GameEvent } from '@/lib/types';

export async function GET() {
    const encoder = new TextEncoder();
    const stream = new ReadableStream({
        async start(controller) {
            // Send initial state
            const events = await memoryStore.lrange('tt:events', 0, 4);
            controller.enqueue(
                encoder.encode(`data: ${JSON.stringify(events.map(e => JSON.parse(e)))}\n\n`)
            );

            // Subscribe to updates
            const unsubscribe = await memoryStore.subscribe('game_updates', async () => {
                const events = await memoryStore.lrange('tt:events', 0, 4);
                controller.enqueue(
                    encoder.encode(`data: ${JSON.stringify(events.map(e => JSON.parse(e)))}\n\n`)
                );
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

// import { redis } from '@/lib/redis';
// import { GameEvent } from '@/lib/types';
//
// export async function GET() {
//     const encoder = new TextEncoder();
//     const stream = new ReadableStream({
//         async start(controller) {
//             // Send initial state
//             const events = await redis.lrange('tt:events', 0, 4);
//             controller.enqueue(
//                 encoder.encode(`data: ${JSON.stringify(events.map(e => JSON.parse(e)))}\n\n`)
//             );
//
//             // Subscribe to updates
//             const subscriber = redis.duplicate();
//             await subscriber.subscribe('game_updates');
//
//             subscriber.on('message', async (channel, message) => {
//                 const events = await redis.lrange('tt:events', 0, 4);
//                 controller.enqueue(
//                     encoder.encode(`data: ${JSON.stringify(events.map(e => JSON.parse(e)))}\n\n`)
//                 );
//             });
//         }
//     });
//
//     return new Response(stream, {
//         headers: {
//             'Content-Type': 'text/event-stream',
//             'Cache-Control': 'no-cache',
//             'Connection': 'keep-alive',
//         },
//     });
// }