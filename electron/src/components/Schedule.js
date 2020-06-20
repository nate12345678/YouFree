import '../css/Schedule.css'
import React from 'react';

function Schedule(props) {

	const LENGTH_OF_DAY = 1440;

	// Creating the timeline for day 0
	let boxes = [];
	for (let i = 0; i < props.schedule[0].length; i++) {
		let duration = props.schedule[0][i].endMin - props.schedule[0][i].startMin;
		let width = duration / LENGTH_OF_DAY;
		let leftMargin = (i === 0)
			? props.schedule[0][i].startMin / LENGTH_OF_DAY
			: (props.schedule[0][i].startMin - props.schedule[0][i - 1].endMin) / LENGTH_OF_DAY;

		let box = (
			<div key={props.schedule[0][i].startMin} className="Bar" style={{
				marginLeft: 100 * leftMargin + '%',
				width: 100 * width + '%'
			}} />
		);

		boxes.push(box);
	}

	return (
		<div className="Intervals">
			{boxes}
		</div>
	);
}

export default Schedule;
