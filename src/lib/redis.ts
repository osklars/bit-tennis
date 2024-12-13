// // lib/redis.ts
// import { Redis } from 'ioredis';
//
// const globalForRedis = globalThis as unknown as {
//     redis: Redis | undefined;
// };
//
// export const redis = globalForRedis.redis ??
//     new Redis(process.env.REDIS_URL || 'redis://localhost:6379', {
//         maxRetriesPerRequest: 1,
//         retryStrategy: (times) => {
//             console.log('Redis connection lost, retrying...');
//             return Math.min(times * 50, 2000);
//         },
//         reconnectOnError: (err) => {
//             console.log('Redis reconnect on error:', err);
//             return true;
//         }
//     });
//
// redis.on('error', (error) => {
//     console.log('Redis connection error:', error);
// });
//
// redis.on('connect', () => {
//     console.log('Successfully connected to Redis');
// });
//
// if (process.env.NODE_ENV !== 'production') globalForRedis.redis = redis;