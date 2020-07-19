import '../css/MyProfilePage.css';
import React from 'react';
import {
	Card,
	CardContent,
	Typography
} from '@material-ui/core';
import DailySchedule from './DailySchedule';

class MyProfilePage extends React.Component {

	dayOfWeek = {
		0: 'Monday',
		1: 'Tuesday',
		2: 'Wednesday',
		3: 'Thursday',
		4: 'Friday',
		5: 'Saturday',
		6: 'Sunday'
	};


	componentDidMount() {
		this.props.getSchedule();
	}


	render() {
		let weeklySchedule = <span>Loading</span>
		if (this.props.schedule != null) {
			weeklySchedule = [];
			for (let i = 0; i < this.props.schedule.length; i++) {
				weeklySchedule.push(<div key={this.dayOfWeek[i]} className="weekly-schedule-names">{this.dayOfWeek[i]}</div>);
				weeklySchedule.push(
					<div key={this.dayOfWeek[i] + 'schedule'} className="weekly-schedule-schedules">
						<DailySchedule schedule={this.props.schedule[i]}/>
					</div>
				);
			}
		}

		return (
			<>
				<Card className="my-profile-card" elevation={4}>
					<CardContent>
						<div className="my-profile-user">
							<div className="my-profile-pic" />
							<div className="my-profile-info">
								<Typography className="my-profile-username" variant="h4">{this.props.user.username}</Typography>
								<Typography className="my-profile-email" variant="h6">{this.props.user.email}</Typography>
							</div>
						</div>
					</CardContent>
				</Card>
				<Card className="my-weekly-schedule-card" elevation={4}>
					<CardContent>
						<Typography className="Title" variant="h6">Weekly Schedule</Typography>
						<div className="weekly-schedule-grid">
							{weeklySchedule}
						</div>
					</CardContent>
				</Card>
			</>
		);
	}
}

export default MyProfilePage;
