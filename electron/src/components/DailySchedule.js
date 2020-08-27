import '../css/DailySchedule.css'
import React from 'react';
import {
	Button,
	Tooltip
} from '@material-ui/core';

export default function DailySchedule({ schedule }) {

	const LENGTH_OF_DAY = 1440;

	// Creating the timeline for day 0
	let boxes = [];
	for (let i = 0; i < schedule.length; i++) {
		const duration = schedule[i].endMin - schedule[i].startMin;
		const width = duration / LENGTH_OF_DAY;
		const leftMargin = (i === 0)
			? schedule[i].startMin / LENGTH_OF_DAY
			: (schedule[i].startMin - schedule[i - 1].endMin) / LENGTH_OF_DAY;

		const tooltip = <TooltipContent startMin={schedule[i].startMin} endMin={schedule[i].endMin} editMode={true} />
		const box = (
			<Tooltip key={schedule[i].startMin} title={tooltip} arrow interactive>
				<div className="bar" style={{
					marginLeft: 100 * leftMargin + '%',
					width: 100 * width + '%'
				}} />
			</Tooltip>
		);

		boxes.push(box);
	}

	return (
		<div className="intervals">
			{boxes}
		</div>
	);
}


function TooltipContent({ startMin, endMin, editMode }) {
	const startTime = Math.floor(startMin / 60) + ':' + `${startMin % 60}`.padStart(2, '0');
	const endTime = Math.floor(endMin / 60) + ':' + `${endMin % 60}`.padStart(2, '0');

	return (
		<React.Fragment>
			<div>{startTime} - {endTime}</div>
			<Button>Edit</Button>
			<Button>Delete</Button>
		</React.Fragment>
	);
}
