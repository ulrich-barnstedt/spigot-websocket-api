import {WebSocket} from "ws";
import {InternalAPI} from "./InternalAPI";
import {SingleEventEmitter} from "./SingleEventEmitter";

export enum CallType {
    INTERNAL = "i",
    EXTERNAL = "m"
}

type ServerResponse = {
    status: string,
    body: any
}

export class Client {
    private socket: WebSocket;
    private readonly internal_api: InternalAPI;
    private errorDistributor: SingleEventEmitter = new SingleEventEmitter(true);
    private dataDistributor: SingleEventEmitter = new SingleEventEmitter();

    private constructor (url: string) {
        this.socket = new WebSocket(url);
        this.internal_api = new InternalAPI(this);
    }

    private send (data: any) {
        this.socket.send(JSON.stringify(data));
    }

    private setupListeners () {
        this.socket.on("message", (data) => {
            // @ts-ignore
            let parsed: ServerResponse = JSON.parse(data);

            switch (parsed.status) {
                case "ERROR":
                    this.errorDistributor.emit(parsed.body);
                    break;
                case "CONTENT":
                    this.dataDistributor.emit(parsed.body);
                    break;
            }
        })
    }

    static async connect (url: string, password: string) : Promise<Client> {
        let client = new Client(url);
        client.setupListeners();

        await new Promise(resolve => client.socket.on("open", resolve));
        client.socket.send(password);

        return client;
    }

    public call (type: CallType, method: string, params?: any[]) {
        if (params) {
            this.send({
                type,
                method,
                params
            })
        } else {
            this.send({
                type,
                method
            })
        }
    }

    public async callWithResponse (type: CallType, method: string, params?: any[]) {
        this.call(type, method, params);
        return await this.dataListener.wait();
    }

    public get dataListener () {
        return this.dataDistributor;
    }

    public get errorListener () {
        return this.errorDistributor;
    }

    public get internal () {
        return this.internal_api;
    }
}
