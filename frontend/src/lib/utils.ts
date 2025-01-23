import {Player} from "@/lib/types";

export function Opponent(player: Player) {
    return player === Player.Red ? Player.Black : Player.Red;
}