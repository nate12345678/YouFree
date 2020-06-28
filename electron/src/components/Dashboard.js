import '../css/Dashboard.css';
import React from 'react';
import MyScheduleCard from './MyScheduleCard';
import FriendSchedulesCard from './FriendSchedulesCard';

class Dashboard extends React.Component {

	constructor(props) {
		super(props);
	}


	componentDidMount() {
		this.props.getDashboard();
	}


	render() {
		let mySchedule = this.props.schedule == null
			? "Loading..."
			: <MyScheduleCard schedule={this.props.schedule} onEdit={this.props.addInterval} />;

		let friendSchedules = this.props.friends == null
			? "Loading..."
			: <FriendSchedulesCard friends={this.props.friends} />;

		return (
			<>
				{mySchedule}
				{friendSchedules}
			</>
		);
	}

}

export default Dashboard;
