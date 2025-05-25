import React, { Component } from 'react';
import './Board.css';
import Square from '../Square/Square';
import Auxillary from '../Auxillary';

class Board extends Component {
    render(){
        let numbers = [...Array(37).keys()]
        let output = []
        for(var i = 0; i < 10; i++){
            output.push(...numbers);
        }
        const black_numbers = this.props.black_numbers;
        const squares = (
            <Auxillary>
                {output.map((number, index) => {
                    let colour = null;
                    if (black_numbers.includes(number)) colour = 'black';
                    else if (number === 0) colour = 'green';
                    else colour = 'red';

                    return <Square
                        key={index}
                        number={number}
                        colour={colour}
                        />
                })}
            </Auxillary>
        )
        let style = {
            transition: "all 7999ms cubic-bezier(0, 0, 0.28, 1) 0s",
            transform: "matrix(1, 0, 0, 1, " + this.props.chosen_number * -1 + ", 0)"
        }
        let containerClasses = ["spinItemsContainer"];
        let spinner = null;
        if(this.props.spin === true){
            spinner = (
                <div style={style} className={containerClasses.join(" ")}>
                    {squares}
                </div>
            )
        } else{
            spinner = (
                <div className={containerClasses.join(" ")}>
                    {squares}
                </div>
            )
        }
        return (
            <Auxillary>
                <div className={"outer"}>
                    <div className={"spinContainer"}>
                        {spinner}
                    </div>
                </div>
            </Auxillary>

        )
    }

}

export default Board