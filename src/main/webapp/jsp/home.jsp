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
        let jsonData = JSON.parse(message.data); // to update (using reviver())
        if(jsonData.random_spot != undefined) {
            context.fillRect((jsonData.random_spot.x)*unit_size, (jsonData.random_spot.y)*unit_size,unit_size, unit_size);
            console.log(jsonData.random_spot.x)
            console.log(jsonData.random_spot.y)
        }
        else if(jsonData.renderSnake != undefined){
            let snake = jsonData.renderSnake;
            for(const element of snake){
                context.fillRect((element.point.x)*unit_size, (element.point.y)*unit_size, unit_size, unit_size);
            }
        }
    }

    websocket.onerror = function (event){
        console.error("WebSocket error observed:", event);
    }

    websocket.onclose = function (){
        console.log("[Client] Connection is closed.")
    }

    document.addEventListener('keyup', function (e){
        if(e.key == "ArrowUp" || e.key == "ArrowDown" || e.key == "ArrowRight" || e.key == "ArrowLeft") {
            websocket.send(JSON.stringify({
                cmd: "snakeMove",
                params: {
                    direction: e.key.replace("Arrow", "move")
                }
            }));
        }
    });
</script>
