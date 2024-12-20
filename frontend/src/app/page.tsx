import State from "@/app/components/State";
import Simulator from "@/app/components/Simulator";

export default function Home() {
    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Table Tennis Tracker</h1>
            <div className="space-y-4">
                <State />
                <Simulator />
            </div>
        </div>
    )
}