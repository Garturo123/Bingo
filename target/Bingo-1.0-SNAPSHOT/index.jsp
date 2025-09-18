<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Bingo - Inicio</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <h1>¡Bienvenido al Bingo!</h1>
        <div class="card">
            <h2>Crear una nueva partida</h2>
            <form action="partida" method="post">
                <input type="hidden" name="action" value="crear">
                <input type="text" name="nombre" placeholder="Tu nombre" required>
                <select name="tipoJuego">
                    <option value="90">Bingo 90</option>
                </select>
                <button type="submit">Crear Partida</button>
            </form>
        </div>
        <div class="card">
            <h2>Unirse a una partida</h2>
            <form action="partida" method="post">
                <input type="hidden" name="action" value="unirse">
                <input type="text" name="nombre" placeholder="Tu nombre" required>
                <button type="submit">Unirse a Partida</button>
            </form>
        </div>
    </div>
</body>
</html>