import '../css/Dashboard.css';
import React from 'react';
import Schedule from './Schedule';

function Dashboard(props) {
	let content = props.schedule
		? <Schedule schedule={props.schedule} />
		: "Loading...";

	return (
		<div>
			{content}
		</div>
	);
}

export default Dashboard;
