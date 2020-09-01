import '../css/FriendSchedulesCard.css';
import React from 'react';
import DailySchedule from './DailySchedule';
import {
	Card,
	CardContent,
	Typography
} from '@material-ui/core';
import Markers from './common/Markers';

function FriendSchedulesCard(props) {

	if (props.friends == null) {
		return "Loading friends' schedules...";
	}

	if (props.friends.length === 0) {
		return null;
	}

	const friends = props.friends.slice().sort((a, b) => a.user.username.localeCompare(b.user.username));

	let names = [];
	let schedules = [];
	for (let i = 0; i < friends.length; i++) {
		const name = (
			<div key={friends[i].user.id} className="friend-schedules-name">
				{friends[i].user.username}
			</div>
		);

		const schedule = (
			<React.Fragment key={friends[i].user.id}>
				<DailySchedule schedule={friends[i].schedule[props.day]} />
			</React.Fragment>
		);

		names.push(name);
		schedules.push(schedule);
	}

	return (
		<Card className="friend-schedules-card" elevation={4}>
			<CardContent>
				<Typography className="friend-schedules-title" variant="h6">Friends</Typography>
				<div className="friend-schedules-content">
					<div className="friend-schedules-names">
						{names}
					</div>
					<div className="friend-schedules-intervals">
						{schedules}
						<Markers variant="time" direction="vertical" />
					</div>
				</div>
			</CardContent>
		</Card>
	);

}

export default FriendSchedulesCard;
