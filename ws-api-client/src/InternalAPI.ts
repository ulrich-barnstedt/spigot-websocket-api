import {CallType, Client} from "./Client";

export class InternalAPI {
    private readonly client: Client;

    public constructor (client: Client) {
        this.client = client;
    }


    // -------- java api method wrappers

    public setBlock (world: string, x: number, y: number, z: number, block: string) : InternalAPI {
        this.client.call(
            CallType.INTERNAL,
            "setBlock",
            [world, x, y, z, block]
        );

        return this;
    }

    public fill (world: string, x1: number, y1: number, z1: number, x2: number, y2: number, z2: number, block: string) : InternalAPI {
        this.client.call(
            CallType.INTERNAL,
            "fill",
            [world, x1, y1, z1, x2, y2, z2, block]
        );

        return this;
    }

    public setMultipleBlocks (world: string, actions: [number, number, number, string][]) : InternalAPI {
        this.client.call(
            CallType.INTERNAL,
            "setMultipleBlocks",
            [world, actions]
        );

        return this;
    }

    public async getWorlds () {
        return await this.client.callWithResponse(
            CallType.INTERNAL,
            "getWorlds"
        );
    }
}