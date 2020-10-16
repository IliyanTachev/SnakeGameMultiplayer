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
    let last_position_move = null;
    let settings = {
        map_color: "#323232",
        snake_color: "#ffffff",
        food_color: "#ff0000"
    }

    for(let i=0;i<grid_height;i++, y_pos+=unit_size,x_pos=0){
        for(let k=0;k<grid_width;k++, x_pos+=unit_size){
            context.beginPath();
            context.fillStyle = settings.map_color;
            context.fillRect(x_pos,y_pos,unit_size,unit_size)
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
        if(jsonData.random_spots !== undefined) {
            let snakeHead = jsonData.random_spots.snakeHead;
            context.fillStyle = settings.snake_color;
            context.fillRect((snakeHead.x)*unit_size, (snakeHead.y)*unit_size,unit_size, unit_size);
            for(let foodPoint of jsonData.random_spots.food){
                context.fillStyle = settings.food_color;
                context.fillRect((foodPoint.x)*unit_size, (foodPoint.y)*unit_size,unit_size, unit_size);
            }
        }
        else if(jsonData.renderSnake !== undefined){
            let snake = jsonData.renderSnake;
            if(last_position_move != null) {
                context.fillStyle = "#323232";
                context.fillRect((last_position_move.point.x)*unit_size, (last_position_move.point.y)*unit_size, unit_size, unit_size);
            }
            for(let snakePoint of snake){
                context.fillStyle = "#ffffff";
                context.fillRect((snakePoint.point.x)*unit_size, (snakePoint.point.y)*unit_size, unit_size, unit_size);
                last_position_move = snakePoint;
            }
            // context.fillStyle = "#ffffff";
            // context.fillRect((snake[0].point.x)*unit_size, (snake[0].point.y)*unit_size, unit_size, unit_size);
            //last_position_move = snake[0];
        } else if(jsonData.error_on_server){
            alert("There is an ERROR on server.");
        }
    }

    websocket.onerror = function (event){
        console.error("WebSocket error observed:", event);
    }

    websocket.onclose = function (){
        console.log("[Client] Connection is closed.")
    }

    var changePositionCounter = 0;
    var lastTimeoutId = null;
    document.addEventListener('keyup', function (e){
        if(e.key == "ArrowUp" || e.key == "ArrowDown" || e.key == "ArrowRight" || e.key == "ArrowLeft") {
            if(changePositionCounter> 0) clearTimeout(lastTimeoutId);
            var framesPerSecond = 10;
            let requestAnimation = () => {
                lastTimeoutId = setTimeout(function() {
                    websocket.send(JSON.stringify({
                        cmd: "snakeMove",
                        params: {
                            direction: e.key.replace("Arrow", "move")
                        }
                    }));
                    requestAnimationFrame(requestAnimation);
                }, 1000 / framesPerSecond);
            };

            requestAnimation();
            changePositionCounter++;
        }
    });
</script>
