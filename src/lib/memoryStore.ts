// lib/memoryStore.ts
type Subscriber = (message: string) => void;

class MemoryStore {
    private static instance: MemoryStore;
    private events: string[] = [];
    private subscribers: Subscriber[] = [];

    private constructor() {}

    static getInstance(): MemoryStore {
        if (!MemoryStore.instance) {
            MemoryStore.instance = new MemoryStore();
        }
        return MemoryStore.instance;
    }

    // Mimics Redis LPUSH + LTRIM
    async lpush(key: string, value: string) {
        this.events.unshift(value);
        this.events = this.events.slice(0, 5); // Keep only latest 5
        return this.events.length;
    }

    // Mimics Redis LRANGE
    async lrange(key: string, start: number, end: number) {
        return this.events.slice(start, end + 1);
    }

    // Mimics Redis PUBLISH
    async publish(channel: string, message: string) {
        this.subscribers.forEach(subscriber => subscriber(message));
        return this.subscribers.length;
    }

    // Mimics Redis SUBSCRIBE
    async subscribe(channel: string, callback: Subscriber) {
        this.subscribers.push(callback);
        return () => {
            this.subscribers = this.subscribers.filter(sub => sub !== callback);
        };
    }
}

export const memoryStore = MemoryStore.getInstance();