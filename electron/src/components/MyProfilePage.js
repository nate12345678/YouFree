import '../css/MyProfilePage.css';
import React from 'react';
import {
	Card,
	CardContent,
	Typography
} from '@material-ui/core';
import WeeklySchedule from './WeeklySchedule';

class MyProfilePage extends React.Component {

	componentDidMount() {
		this.props.getSchedule();
	}


	render() {
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
						<WeeklySchedule schedule={this.props.schedule} />
					</CardContent>
				</Card>
			</>
		);
	}
}

export default MyProfilePage;
