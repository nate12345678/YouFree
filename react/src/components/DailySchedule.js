import '../css/DailySchedule.css'
import React from 'react';
import {
	Tooltip,
	Typography
} from '@material-ui/core';

export default function DailySchedule({ schedule, onIntervalSelection = () => {}, selectedInterval }) {

	const LENGTH_OF_DAY = 1440;
	const onClick = (interval) => (event) => {
		onIntervalSelection(interval);
	}

	// Creating the timeline for day 0
	let boxes = [];
	for (let i = 0; i < schedule.length; i++) {
		const duration = schedule[i].endMin - schedule[i].startMin;
		const width = duration / LENGTH_OF_DAY;
		const leftMargin = (i === 0)
			? schedule[i].startMin / LENGTH_OF_DAY
			: (schedule[i].startMin - schedule[i - 1].endMin) / LENGTH_OF_DAY;

		let intervalClass = '';
		if (selectedInterval) {
			intervalClass = selectedInterval.id === schedule[i].id ? ' selected' : ' faded';
		}

		const tooltip = <TooltipContent startMin={schedule[i].startMin} endMin={schedule[i].endMin} editMode={true} />
		const box = (
			<Tooltip key={schedule[i].id} title={tooltip} arrow>
				<div className={'daily-schedule-bar' + intervalClass} onClick={onClick(schedule[i])} style={{
					marginLeft: 100 * leftMargin + '%',
					width: 100 * width + '%'
				}} />
			</Tooltip>
		);

		boxes.push(box);
	}

	return (
		<div className="daily-schedule">
			{boxes}
		</div>
	);
}


// TODO: Add user preference for 12h vs 24h time
function TooltipContent({ startMin, endMin, editMode }) {
	const startHour = Math.floor(startMin / 60) % 12;
	const startHalf = (Math.floor(startHour / 12) === 0) ? 'a' : 'p';
	const startTime = startHour + ':' + `${startMin % 60}`.padStart(2, '0') + startHalf;

	const endHour = Math.floor(endMin / 60) % 12;
	const endHalf = (Math.floor(endHour / 12) === 0) ? 'a' : 'p';
	const endTime = endHour + ':' + `${endMin % 60}`.padStart(2, '0') + endHalf;

	return (
		<React.Fragment>
			<Typography variant="body2">{startTime} - {endTime}</Typography>
		</React.Fragment>
	);
}
