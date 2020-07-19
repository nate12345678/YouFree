import '../css/Dashboard.css';
import React from 'react';
import MyScheduleCard from './MyScheduleCard';
import FriendSchedulesCard from './FriendSchedulesCard';

class Dashboard extends React.Component {

	componentDidMount() {
		this.props.getDashboard();
	}


	render() {
		let mySchedule = this.props.schedule == null
			? "Loading..."
			: <MyScheduleCard schedule={this.props.schedule} onAddInterval={this.props.onAddInterval} />;

		let friendSchedules = this.props.friends == null
			? "Loading..."
			: <FriendSchedulesCard friends={this.props.friends} day={this.props.day} />;

		return (
			<>
				{mySchedule}
				{friendSchedules}
			</>
		);
	}

}

export default Dashboard;
