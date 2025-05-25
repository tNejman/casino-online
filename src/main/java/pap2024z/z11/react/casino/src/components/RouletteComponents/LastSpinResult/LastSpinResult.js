import React from 'react';
import './LastSpinResult.css';
import LastNumber from './LastNumber.js'

/**
 * Component showing last rolled number
 *
 * @param {object} props - (last_win, color)
 * @return {JSX} - div
 */
const LastSpinResult = (props) => {
    return (
        <div className={"container"}>
            <div className={"last_win_cont"}>
                <h1>Last winning number</h1>
                <LastNumber number={props.last_win} color={props.color} />
            </div>
        </div>
    )
}

export default LastSpinResult