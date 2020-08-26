import '../css/DailySchedule.css'
import React from 'react';
import Tooltip from '@material-ui/core/Tooltip';

function DailySchedule({ schedule }) {

	const LENGTH_OF_DAY = 1440;

	// Creating the timeline for day 0
	let boxes = [];
	for (let i = 0; i < schedule.length; i++) {
		const duration = schedule[i].endMin - schedule[i].startMin;
		const width = duration / LENGTH_OF_DAY;
		const leftMargin = (i === 0)
			? schedule[i].startMin / LENGTH_OF_DAY
			: (schedule[i].startMin - schedule[i - 1].endMin) / LENGTH_OF_DAY;

		const startTime = Math.floor(schedule[i].startMin / 60) + ':' + `${schedule[i].startMin % 60}`.padStart(2, '0');
		const endTime = Math.floor(schedule[i].endMin / 60) + ':' + `${schedule[i].endMin % 60}`.padStart(2, '0');

		let box = (
			<Tooltip key={schedule[i].startMin} arrow title={startTime + ' - ' + endTime}>
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

export default DailySchedule;
