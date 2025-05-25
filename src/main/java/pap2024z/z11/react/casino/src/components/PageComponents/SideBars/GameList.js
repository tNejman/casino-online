import React from "react"
import "./GameList.css"

/**
 * Component that gives choice between games
 * 
 * @param {function} setGame - changes currentGame in App
 * @return {JSX} - div
 */
const GameList = ({ setGame }) => {
    const games = ["Blackjack", "Roulette", "Poker", "Slots"];

    return (
        <div className="game-list">
            <h2>Games</h2>
            {games.map((game) => (
                <div
                    key={game}
                    className="game-item"
                    onClick={() => setGame(game)}
                >
                    {game}
                </div>
            ))}
        </div>
    );
};

export default GameList;
