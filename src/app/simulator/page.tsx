'use client';

export default function SimulatorPage() {
    const sendEvent = async (type: string) => {
        await fetch('/api/events', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ type, timestamp: Date.now() }),
        });
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Event Simulator</h1>
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <h2 className="font-bold mb-2">Player A</h2>
                    <button
                        onClick={() => sendEvent('RACKET_HIT_A')}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket Hit A
                    </button>
                    <button
                        onClick={() => sendEvent('BOARD_BOUNCE_A')}
                        className="w-full bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board Bounce A
                    </button>
                </div>
                <div>
                    <h2 className="font-bold mb-2">Player B</h2>
                    <button
                        onClick={() => sendEvent('RACKET_HIT_B')}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket Hit B
                    </button>
                    <button
                        onClick={() => sendEvent('BOARD_BOUNCE_B')}
                        className="w-full bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board Bounce B
                    </button>
                </div>
            </div>
        </div>
    );
}