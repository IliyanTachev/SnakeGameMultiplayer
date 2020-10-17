<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
<%--    <script src="../js/websocket_client.js" type="application/javascript"></script>--%>
<%--    <link rel="stylesheet" href="../css/style.css" type="text/css"/>--%>
    <style>
        body{
            background-color: #1a0000;
        }
        canvas{
            border: 1px solid purple;
            position: relative;
            margin: 0 auto;
            display: block;
        }

        .eaten-food{
            color: white;
            font-weight: bolder;
            font-family: "Lucida Console", Courier, monospace;
            font-size: 30px;
            text-align: center;
        }

        .eaten-food > .score{
            display: inline;
        }

        .modal{
            position: absolute;
            top: 50%;
            left: 50%;
            width: 80%;
            height: 70%;
            background-color: rgba(255,255,255,0.7);
            transform:translate(-50%,-50%);
            font-family: "Lucida Console", Courier, monospace;
            font-size: 50px;
            text-align: center;
        }

        .modal.win{

        }

        .modal.lose{

        }

        #try-again-btn{
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            display: block;
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <canvas width="900" height="480">
    </canvas>
    <div class="eaten-food">
        Eaten food: <p class="score">0</p>
    </div>
</body>
</html>

<script>
    let websocket = null;
    let gameLoader = (cmd) => {
        console.log("websocket = " + websocket);
        if(cmd != "restart") websocket = new WebSocket("ws:localhost:8080/home"); // restart game after clicked on try again button

        if(cmd == "restart"){
            // reset stuff
            document.querySelector(".eaten-food > .score").innerHTML="0";
            document.querySelector(".modal").remove();

            websocket.send(JSON.stringify({
                cmd: "resetSnake"
            }));
        }
        let canvas = document.querySelector("canvas");
        let context = canvas.getContext("2d");
        let x_pos = 0, y_pos = 0;
        let grid_width = 60, grid_height = 40, unit_size = 15;
        let last_position_move = null;
        let settings = {
            map_color: "#323232",
            snake_color: "#ffffff",
            food_color: "#ff0000",
            map_border_color: "#ffffff"
        }

        for (let i = 0; i < grid_height; i++, y_pos += unit_size, x_pos = 0) {
            for (let k = 0; k < grid_width; k++, x_pos += unit_size) {
                context.beginPath();
                context.fillStyle = settings.map_color; // fillStyle
                context.fillRect(x_pos, y_pos, unit_size, unit_size); // fillRect
                context.strokeStyle = settings.map_border_color;
                context.strokeRect(x_pos, y_pos, unit_size + 1, unit_size + 1); // border
            }
        }

        websocket.onopen = function () {
            console.log("[Client] Connection established.");
            websocket.send(JSON.stringify({
                cmd: "initGrid",
                params: {
                    grid_width,
                    grid_height
                }
            }));
        }

        // globals
        let snakeHead = null;
        let random_spots_food = null;
        let snake = null;

        websocket.onmessage = function (message) {
            let jsonData = JSON.parse(message.data); // to update (using reviver())
            if (jsonData.random_spots !== undefined) {
                snakeHead = jsonData.random_spots.snakeHead;
                random_spots_food = jsonData.random_spots.food;

                context.beginPath();
                context.fillStyle = settings.snake_color;
                context.fillRect((snakeHead.x) * unit_size, (snakeHead.y) * unit_size, unit_size - 1, unit_size - 1); // snake inner color
                context.strokeStyle = settings.map_color;
                context.strokeRect((snakeHead.x) * unit_size, (snakeHead.y) * unit_size, unit_size, unit_size); // snake border color

                for (let foodPoint of random_spots_food) {
                    context.beginPath();
                    context.fillStyle = settings.food_color;
                    context.fillRect((foodPoint.x) * unit_size, (foodPoint.y) * unit_size, unit_size, unit_size);
                }

                console.log("food number: " + random_spots_food.length);
            } else if (jsonData.renderSnake !== undefined) {
                // Delete initial random spot
                context.beginPath();
                context.fillStyle = settings.map_color; // fillStyle
                context.fillRect((snakeHead.x) * unit_size, (snakeHead.y) * unit_size, unit_size - 1, unit_size - 1);
                context.strokeStyle = settings.map_border_color;
                context.strokeRect((snakeHead.x) * unit_size, (snakeHead.y) * unit_size, unit_size, unit_size);
                // Delete initial random spot

                snake = jsonData.renderSnake;
                let eaten_food_score = document.querySelector(".eaten-food > .score");
                eaten_food_score.innerHTML = snake.length - 1;

                if (snake.length-1 == random_spots_food.length) {
                    displayModal("win", random_spots_food.length);
                }

                if (last_position_move != null) { // Delete body after snake update
                    context.beginPath();
                    context.fillStyle = settings.map_color; // fillStyle
                    context.fillRect((last_position_move.point.x) * unit_size, (last_position_move.point.y) * unit_size, unit_size - 1, unit_size - 1);
                    context.strokeStyle = settings.map_border_color;
                    context.strokeRect((last_position_move.point.x) * unit_size, (last_position_move.point.y) * unit_size, unit_size, unit_size);
                }
                for (let snakePoint of snake) {
                    context.beginPath();
                    context.fillStyle = settings.snake_color; // fillStyle
                    context.fillRect((snakePoint.point.x) * unit_size, (snakePoint.point.y) * unit_size, unit_size - 1, unit_size - 1);
                    context.strokeStyle = settings.map_color;
                    context.strokeRect((snakePoint.point.x) * unit_size, (snakePoint.point.y) * unit_size, unit_size, unit_size);
                    last_position_move = snakePoint;
                }
            } else if (jsonData.error_on_server) {
                alert("There is an ERROR on server.");
            } else if(jsonData.gameover != undefined){
                displayModal("lose", snake.length-1); // snake.length-1 = food number
            }
        }

        websocket.onerror = function (event) {
            console.error("WebSocket error observed:", event);
        }

        websocket.onclose = function () {
            console.log("[Client] Connection is closed.")
        }

        var changePositionCounter = 0;
        var lastTimeoutId = null;
        document.addEventListener('keyup', function (e) {
            if (e.key == "ArrowUp" || e.key == "ArrowDown" || e.key == "ArrowRight" || e.key == "ArrowLeft") {
                if (changePositionCounter > 0) clearTimeout(lastTimeoutId);
                var framesPerSecond = 10;
                let requestAnimation = () => {
                    lastTimeoutId = setTimeout(function () {
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

        function displayModal(type, score) {
            if (document.querySelector(".modal") === null) {
                let document_body = document.querySelector("body");
                let div = document.createElement("div");
                div.classList.add("modal");
                if (type == "win") {
                    div.classList.add("win");
                    div.innerHTML = "<p style=\"background-color: #4CAF50\">You won. Congratiolations.</p>" + "<br/>";
                    div.innerHTML += "Your score is " + score;
                } else {
                    div.classList.add("lose");
                    div.innerHTML = "<p style=\"background-color: darkred;color: black;\">GAME OVER" + "<br/>";
                    div.innerHTML += "Your score is " + score;
                }
                div.innerHTML += "<button id=\"try-again-btn\" onclick=\"gameLoader('restart');\">Try Again</button>";
                document_body.appendChild(div);
            }
        }
    }

    gameLoader();
</script>
