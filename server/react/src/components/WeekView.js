import React from 'react';
import DayView from './DayView';

function WeekView(props) {
    return (
        <div className='WeekView'>
            {props.intervals.map((day, i) =>
                <DayView key={i} dayNum={i} intervals={day} cellAction={props.cellAction} />
            )}
            {/* <DayView intervals={props.intervals[0]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[1]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[2]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[3]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[4]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[5]} cellFunction={props.cellFunction} />
            <DayView intervals={props.intervals[6]} cellFunction={props.cellFunction} /> */}
        </div>
    );
}

export default WeekView;