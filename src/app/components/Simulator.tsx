'use client';

import {EventType, StateSummary, Player, ErrorResponse} from "@/lib/types";
import {useState} from "react";

export default function Simulator() {
    const [state, setState] = useState<StateSummary | ErrorResponse>();

    const sendEvent = async (event: EventType, player?: Player) => {
        const body = JSON.stringify({
            event,
            player: player || null
        })
        console.log("oskar post event", body);
        try {
            const response = await fetch('http://localhost:8080/event', {
                method: 'POST',
                body,
            });
            const newState = await response.json()
            console.log("oskar posted event", newState);
            setState(newState);
        } catch (e) {
            console.log("oskar post event", e)
        }
    };
    
    return (
        <div className="flex items-center justify-center gap-8 p-4">
            {/* Left side */}
            <div className="flex items-center gap-4">
                <span className="text-2xl font-bold">A</span>
                <div className="flex flex-col gap-2">
                    <button
                        onClick={() => sendEvent(EventType.Throw, Player.A)}
                        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                        Throw
                    </button>
                    <button
                        onClick={() => sendEvent(EventType.Racket, Player.A)}
                        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                        Racket
                    </button>
                </div>
            </div>

            {/* Table */}
            <div className="border-2 border-gray-400 rounded">
                <div className="flex">
                    <button
                        onClick={() => sendEvent(EventType.Board, Player.A)}
                        className="w-48 h-32 bg-green-100 hover:bg-green-200 border-r-4 border-gray-400"
                    >
                        Side A
                    </button>
                    <div
                        onClick={() => sendEvent(EventType.Net)}
                        className="w-4 h-32 bg-gray-300 hover:bg-gray-400 cursor-pointer"
                    />
                    <button
                        onClick={() => sendEvent(EventType.Board, Player.B)}
                        className="w-48 h-32 bg-green-100 hover:bg-green-200 border-l-4 border-gray-400"
                    >
                        Side B
                    </button>
                </div>
            </div>

            {/* Right side */}
            <div className="flex items-center gap-4">
                <div className="flex flex-col gap-2">
                    <button
                        onClick={() => sendEvent(EventType.Throw, Player.B)}
                        className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    >
                        Throw
                    </button>
                    <button
                        onClick={() => sendEvent(EventType.Racket, Player.B)}
                        className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    >
                        Racket
                    </button>
                </div>
                <span className="text-2xl font-bold">B</span>
            </div>
        </div>
    );
}

// export default function Simulator() {
//
//     const newMatch = async (input: NewMatch) => {
//         console.log("oskar post new", input);
//         try {
//             const response = await fetch('http://localhost:8080/new', {
//                 method: 'POST',
//                 body: JSON.stringify(input),
//             })
//         } catch (e) {
//             console.log("oskar post new", e)
//         }
//     };
//
//     return (
//         <div className="p-8">
//             <h1 className="text-2xl font-bold mb-4">Vision Event Simulator</h1>
//             <div className="grid grid-cols-2 gap-4">
//                 <div>
//                     <h2 className="font-bold mb-2">Player A</h2>
//                     <button
//                         onClick={() => sendEvent(EventType.Racket, Player.A)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Racket A
//                     </button>
//                     <button
//                         onClick={() => sendEvent(EventType.Board, Player.A)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Board A
//                     </button>
//                 </div>
//                 <div>
//                     <button
//                         onClick={() => sendEvent(EventType.Net)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Net
//                     </button>
//                     <button
//                         onClick={() => sendEvent(EventType.Out)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Out
//                     </button>
//                 </div>
//                 <div>
//                     <h2 className="font-bold mb-2">Player B</h2>
//                     <button
//                         onClick={() => sendEvent(EventType.Racket, Player.B)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Racket B
//                     </button>
//                     <button
//                         onClick={() => sendEvent(EventType.Board, Player.B)}
//                         className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
//                     >
//                         Board B
//                     </button>
//                 </div>
//
//                 <div><h2 className="font-bold mb-2">Response: {JSON.stringify(state)}</h2></div>
//             </div>
//         </div>
//     );
// }