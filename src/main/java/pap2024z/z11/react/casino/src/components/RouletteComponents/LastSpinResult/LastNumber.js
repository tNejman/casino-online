import React from 'react';
import './LastNumber.css';

/**
 * Placeholder for last rolled number
 * 
 * @param {object} props - (number, color)
 * @return {JSX} - div
 */
const LastNumber = (props) => {
    let assignedClasses = ["number"];
    if (props.color === "red" || props.color === "Red") assignedClasses.push("red");
    else if (props.color === "black" || props.color === "Black") assignedClasses.push("black");
    else if (props.color === "green" || props.color === "Green")assignedClasses.push("green");

    return (
        <div className={assignedClasses.join(" ")}>{props.number}</div>
    )
}

export default LastNumber