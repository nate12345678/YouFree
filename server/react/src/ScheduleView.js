import React from 'react';
import './index.css';
import DayView from "./DayView";

function ScheduleView(props) {


	return (
		<div style={{
			border: 2 + 'px solid deeppink',
			borderRadius: 10 + 'px'
		}}>
			<DayView intervals={props.intervals[0]} day='Mon'/>
			<DayView intervals={props.intervals[1]} day='Tue'/>
			<DayView intervals={props.intervals[2]} day='Wed'/>
			<DayView intervals={props.intervals[3]} day='Thu'/>
			<DayView intervals={props.intervals[4]} day='Fri'/>
			<DayView intervals={props.intervals[5]} day='Sat'/>
			<DayView intervals={props.intervals[6]} day='Sun'/>
		</div>
	)
}

export default ScheduleView;