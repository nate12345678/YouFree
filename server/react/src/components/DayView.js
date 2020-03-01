import React from 'react';
import Cell from './Cell';

function DayView(props) {
    return (
        <div className="DayView">
            <div style={{position:'absolute',left:'1.5%'}}>{props.dayNum}</div>
            {props.intervals.map(interval =>
                <Cell key={interval.id} interval={interval} cellAction={props.cellAction} />
            )}
        </div>
    );
}

export default DayView;