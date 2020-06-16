import '../css/Dashboard.css';
import React from 'react';
import Schedule from './Schedule';

class Dashboard extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			initSchedule: props.initSchedule,
			schedule: props.schedule
		}
	}

	componentDidMount() {
		this.state.initSchedule();
	}

	render() {
		console.log("Dashboard re-rendered");
		if (this.state.aschedule == null) {
			return <div>Loading</div>;
		}

		return (
			<div>
				<Schedule schedule={this.state.aschedule} />
			</div>
		);
	}
}

export default Dashboard;
