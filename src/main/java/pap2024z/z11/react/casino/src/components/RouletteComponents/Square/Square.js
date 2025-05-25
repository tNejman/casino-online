import React from 'react';
import './Square.css';
import Auxillary from '../Auxillary.js';

/**
 * Square with following numbers in rotation
 * 
 * @param {object} props - (number, colour)
 * @return {JSX} - AUX
 */
const Square = (props) => {
    let assignedClasses = ["square"];
    if(props.colour === 'red') assignedClasses.push("redSquare");
    else if (props.colour === 'black') assignedClasses.push("blackSquare");
    else assignedClasses.push("greenSquare");

    return (
        <Auxillary>
            <p onClick={props.click} className={assignedClasses.join(" ")}>{props.number}</p>
        </Auxillary>
    )
}

export default Square