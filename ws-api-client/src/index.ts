import {Client} from "./lib/Client";


(async () => {
    let c = await Client.connect("ws://localhost:25568", "wsapi");
    c.errorListener.on(console.log);


})();