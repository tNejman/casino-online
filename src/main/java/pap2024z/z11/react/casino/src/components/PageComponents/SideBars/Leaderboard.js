import React, { useEffect, useState } from "react";
import "./Leaderboard.css";

/**
 * Component for player rating table
 * 
 * @return {JSX} - div
 */
const Leaderboard = () => {
    const [players, setPlayers] = useState([]);
    
    useEffect(() => {
        // Funkcja do aktualizacji danych
        const fetchPlayers = async () => {
            try {
                const response = await fetch(`http://localhost:8081/api/player/getAllPlayers`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                if (!response.ok) {
                    const errorMessage = await response.json();
                    console.error(`Error getting players: ${errorMessage}`);
                    return;
                }

                const data = await response.json();
                setPlayers(data);
            } catch (error) {
                console.error("Error with fetching API:", error);
            }
        };

        // Wywołanie funkcji fetchPlayers w regularnych odstępach
        const intervalId = setInterval(() => {
            fetchPlayers();
        }, 5000); // 5000 ms = 5 sekund

        // Natychmiastowe pobranie danych przy montowaniu komponentu
        fetchPlayers();

        // Czyszczenie interwału po odmontowaniu komponentu
        return () => clearInterval(intervalId);
    }, []); // Pusta lista zależności, aby uruchomić tylko raz po zamontowaniu

    return (
        <div className="leaderboard">
            <h2>Leaderboard</h2>
            {players.map((player, index) => (
                <div key={index} className="player">
                    <span>{player[0]}</span> {/* Nazwa gracza */}
                    <span>{player[1]}</span> {/* Wynik gracza */}
                </div>
            ))}
        </div>
    );
};

export default Leaderboard;
