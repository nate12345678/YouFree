import '../css/FriendSchedulesCard.css';
import React from 'react';
import Schedule from './Schedule';
import {
	Card,
	CardContent
} from '@material-ui/core';

function FriendSchedulesCard(props) {

	if (props.friends == null) {
		return "Loading friends' schedules...";
	}

	if (props.friends.length === 0) {
		return null;
	}

	props.friends.sort((a, b) => a.user.username.localeCompare(b.user.username));

	let names = [];
	let schedules = [];
	for (let i = 0; i < props.friends.length; i++) {
		let nameDiv = <div className="NameListing" key={i}>{props.friends[i].user.username}</div>;
		let scheduleDiv = <Schedule key={i} schedule={props.friends[i].schedule}/>;

		names.push(nameDiv);
		schedules.push(scheduleDiv);
	}

	return (
		<Card className="FriendSchedulesCard" elevation={4}>
			<CardContent>
				<div className="FriendSchedulesDiv">
					<div className="Names">
						{names}
					</div>
					<div className="Schedules">
						{schedules}
					</div>
				</div>
			</CardContent>
		</Card>
	);

}

export default FriendSchedulesCard;
