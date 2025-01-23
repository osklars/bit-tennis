'use client';

import {useState} from 'react';
import {redirect, useRouter} from 'next/navigation';
import {NewMatch, Player} from '@/lib/types';

async function submitForm(formData: FormData) {
    await fetch(`${process.env.NEXT_PUBLIC_API_URL}/new`, {
        method: 'POST',
        body: JSON.stringify({
            playerRed: formData.get('playerRed'),
            playerBlack: formData.get('playerBlack'),
            bestOf: Number(formData.get('bestOf')),
            firstServer: formData.get('firstServer')
        })
    });
    redirect('/score');
}

export default function SetupForm() {
    const router = useRouter();
    const [formData, setFormData] = useState<NewMatch>({
        playerRed: '',
        playerBlack: '',
        bestOf: 3,
        firstServer: Player.Red
    });

    return (
        <form action={submitForm}>
            <div className="flex w-screen h-screen">
                <div className="bg-red-600 w-1/2 h-full flex flex-col items-center justify-center text-white">
                    <input
                        type="text"
                        value={formData.playerRed}
                        onChange={e => setFormData({...formData, playerRed: e.target.value})}
                        className="w-64 p-2 bg-transparent text-white text-4xl border-b-2 border-white placeholder-white/50 focus:outline-none text-center"
                        placeholder="Red"
                    />
                </div>

                <div
                    className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 text-4xl font-bold text-white">
                    VS
                </div>

                <div className="bg-black w-1/2 h-full flex flex-col items-center justify-center text-white">
                    <input
                        type="text"
                        value={formData.playerBlack}
                        onChange={e => setFormData({...formData, playerBlack: e.target.value})}
                        className="w-64 p-2 bg-transparent text-white text-4xl border-b-2 border-white placeholder-white/50 focus:outline-none text-center"
                        placeholder="Black"
                    />
                </div>

                <div className="absolute bottom-0 left-0 right-0 flex justify-center p-8 gap-4">
                    <select
                        value={formData.bestOf}
                        onChange={e => setFormData({...formData, bestOf: parseInt(e.target.value)})}
                        className="p-2 border rounded"
                    >
                        <option value={1}>Best of 1</option>
                        <option value={3}>Best of 3</option>
                        <option value={5}>Best of 5</option>
                    </select>
                    <select
                        value={formData.firstServer}
                        onChange={e => setFormData({...formData, firstServer: e.target.value as Player})}
                        className="p-2 border rounded"
                    >
                        <option value={Player.Red}>Red serves first</option>
                        <option value={Player.Black}>Black serves first</option>
                    </select>
                    <button type="submit" className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                        Start Game
                    </button>
                </div>
            </div>
        </form>
    );
}