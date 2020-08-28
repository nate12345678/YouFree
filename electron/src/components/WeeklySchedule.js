import '../css/WeeklySchedule.css';
import React from 'react';
import {
	Typography
} from '@material-ui/core';
import DailySchedule from './DailySchedule';

const dayOfWeek = {
	0: 'Monday',
	1: 'Tuesday',
	2: 'Wednesday',
	3: 'Thursday',
	4: 'Friday',
	5: 'Saturday',
	6: 'Sunday'
};


function WeeklySchedule({ schedule, onIntervalSelection, selectedInterval }) {

	let labeledSchedule = <span>Loading</span>
	if (schedule != null) {
		labeledSchedule = [];
		for (let i = 0; i < schedule.length; i++) {
			labeledSchedule.push(
				<div key={dayOfWeek[i]} className="weekly-schedule-names">
					{dayOfWeek[i]}
				</div>
			);

			labeledSchedule.push(
				<div key={dayOfWeek[i] + 'schedule'} className="weekly-schedule-schedules">
					<DailySchedule schedule={schedule[i]}
					               onIntervalSelection={onIntervalSelection}
					               selectedInterval={selectedInterval}
					/>
				</div>
			);
		}
	}

	return (
		<>
			<Typography className="weekly-schedule-title" variant="h6">Weekly Schedule</Typography>
			<div className="weekly-schedule-grid">{labeledSchedule}</div>
		</>
	);
}

export default WeeklySchedule;
