<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
<%--    <script src="../js/websocket_client.js" type="application/javascript"></script>--%>
<%--    <link rel="stylesheet" href="../css/style.css" type="text/css"/>--%>
    <style>
        canvas{
            border: 1px solid purple;
            position: relative;
            margin: 0 auto;
            display: block;
        }
    </style>
</head>
<body>
    <canvas width="900" height="480">
    </canvas>
</body>
</html>

<script>
    let canvas = document.querySelector("canvas");
    let context = canvas.getContext("2d");
    let x_pos = 0, y_pos = 0;
    let grid_width = 60, grid_height = 40, unit_size = 15;

    for(let i=0;i<grid_height;i++, y_pos+=unit_size,x_pos=0){
        for(let k=0;k<grid_width;k++, x_pos+=unit_size){
            context.beginPath();
            context.strokeRect(x_pos,y_pos,unit_size,unit_size)
        }
    }
    let websocket = new WebSocket("ws:localhost:8080/home");
    websocket.onopen = function (){
        console.log("[Client] Connection established.");
        websocket.send(JSON.stringify({
            cmd: "initGrid",
            params: {
                grid_width,
                grid_height
            }
        }));
    }

    websocket.onmessage = function (message){
         console.log(JSON.parse(message.data));

        let jsonObj = JSON.parse(message.data); // to update (using reviver())
        context.fillRect(jsonObj.random_spot.random_x, jsonObj.random_spot.random_y,unit_size, unit_size);
        // console.log("Message received: " + message.data);
    }

    websocket.onerror = function (event){
        console.error("WebSocket error observed:", event);
    }

    websocket.onclose = function (){
        console.log("[Client] Connection is closed.")
    }
</script>
