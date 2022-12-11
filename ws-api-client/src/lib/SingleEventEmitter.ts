type EventListener = (data?: any) => void;

export class SingleEventEmitter {
    private listeners: EventListener[] = [];
    private onceListeners: EventListener[] = [];
    private readonly throwIfNone: boolean;

    constructor (throwIfNone: boolean = false) {
        this.throwIfNone = throwIfNone;
    }

    public on (listener: EventListener) {
        this.listeners.push(listener);
    }

    public once (listener: EventListener) {
        this.onceListeners.push(listener);
    }

    public async wait () : Promise<any> {
        return await new Promise(resolve => this.once(resolve));
    }

    public emit (data: any) {
        if (this.throwIfNone && (this.listeners.length + this.onceListeners.length) === 0) {
            throw data;
        }

        this.listeners.forEach(l => l(data));
        this.onceListeners.forEach(l => l(data));
        this.onceListeners = [];
    }
}