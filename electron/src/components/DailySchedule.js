import '../css/DailySchedule.css'
import React from 'react';

function DailySchedule(props) {

	const LENGTH_OF_DAY = 1440;

	// Creating the timeline for day 0
	let boxes = [];
	for (let i = 0; i < props.schedule.length; i++) {
		let duration = props.schedule[i].endMin - props.schedule[i].startMin;
		let width = duration / LENGTH_OF_DAY;
		let leftMargin = (i === 0)
			? props.schedule[i].startMin / LENGTH_OF_DAY
			: (props.schedule[i].startMin - props.schedule[i - 1].endMin) / LENGTH_OF_DAY;

		let box = (
			<div key={props.schedule[i].startMin} className="Bar" style={{
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

export default DailySchedule;
