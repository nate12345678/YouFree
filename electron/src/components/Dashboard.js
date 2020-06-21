import '../css/Dashboard.css';
import React from 'react';
import Schedule from './Schedule';
import MyScheduleCard from './MyScheduleCard';
import FriendSchedulesCard from './FriendSchedulesCard';

function Dashboard(props) {
	let content = props.schedule
		? <Schedule schedule={props.schedule} />
		: "Loading...";

	let friendSchedule = props.friendSchedules
		? <FriendSchedulesCard friends={props.friendSchedules} />
		: "Loading...";

	return (
		<div>
			<MyScheduleCard schedule={props.schedule} onEdit={this.addInterval}/>
			<FriendSchedulesCard friends={props.friendSchedules} />
		</div>
	);
}

export default Dashboard;
