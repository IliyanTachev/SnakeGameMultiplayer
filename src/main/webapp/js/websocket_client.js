let websocket = new WebSocket("ws:localhost:8080/home");
websocket.onopen = function (){
    console.log("[Client] Connection established.");
}

websocket.onmessage = function (message){
    console.log("[Client] Message received: " + message.data);
}

websocket.onerror = function (event){
    console.error("WebSocket error observed:", event);
}

websocket.onclose = function (){
    console.log("[Client] Connection is closed.")
}