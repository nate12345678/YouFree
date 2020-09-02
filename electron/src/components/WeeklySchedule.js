import '../css/WeeklySchedule.css';
import React from 'react';
import {
	Typography
} from '@material-ui/core';
import { dayOfWeek } from '../constants/DayOfWeek'
import DailySchedule from './DailySchedule';
import Markers from './common/Markers';


function WeeklySchedule({ schedule, onIntervalSelection, selectedInterval }) {

	// TODO: display something different when content is loading
	let dayLabels = [];
	let dailySchedules = [];
	if (schedule != null) {

		for (let i = 0; i < schedule.length; i++) {
			const dayLabel = <div key={dayOfWeek[i]} className="weekly-schedule-label">{dayOfWeek[i]}</div>;
			const dailySchedule = (
				<React.Fragment key={dayOfWeek[i]}>
					<DailySchedule schedule={schedule[i]} onIntervalSelection={onIntervalSelection} selectedInterval={selectedInterval} />
				</React.Fragment>
			);

			dayLabels.push(dayLabel);
			dailySchedules.push(dailySchedule);
		}
	}

	return (
		<>
			<Typography className="weekly-schedule-title" variant="h6">Weekly Schedule</Typography>
			<div className="weekly-schedule-content">
				<div className="weekly-schedule-day-labels">
					{dayLabels}
				</div>
				<div className="weekly-schedule-day-intervals">
					{dailySchedules}
					<Markers variant="time" direction="vertical" />
				</div>
			</div>
		</>
	);
}

export default WeeklySchedule;
