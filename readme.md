# Spigot-WebSocket-API

This repository consists of two parts: the server (spigot plugin) and a example client implementation (typescript).   
The goal of this project is to allow a client written in any language to call java code on a minecraft server via a WebSocket connection.

### Server

The plugin provides a WebSocket server.   
After a client has connected and authenthicated, the server will listen for any incoming JSON messages.
These can be sent by any websocket-compliant client, and can call predefined code on the server.
(Theoretically the API for calling any arbitrary method from the Spigot API also exists, but has not been implemented yet).
These calls can have any desired effect, for example setting a block ingame (as in the example commands).

### Client

The typescript client is a simple example implementation wrapping around the websocket API and providing methods for the 
defined code on the server.
