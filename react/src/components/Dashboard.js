import '../css/Dashboard.css';
import React from 'react';
import DailySchedule from './DailySchedule';
import {
	Card,
	CardContent,
	Icon,
	IconButton,
	Typography
} from '@material-ui/core';
import { dayOfWeek } from '../constants/DayOfWeek';
import Markers from './common/Markers';
import { connect } from 'react-redux';
import {
	addInterval,
	fetchMySchedule,
	getFriendSchedules
} from '../state/Effects';


function select(state) {
	return {
		friends: state.friendSchedules,
		mySchedule: state.mySchedule
	};
}


function mapDispatchToProps(dispatch) {
	return {
		getDashboard: () => {
			dispatch(getFriendSchedules());
			dispatch(fetchMySchedule());
		},
		addInterval: (dayOfWeek, startMin, endMin) => dispatch(addInterval(dayOfWeek, startMin, endMin))
	};
}


class ConnectedDashboard extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			day: (new Date().getDay() + 6) % 7
		};
	}


	componentDidMount() {
		this.props.getDashboard();
		// We add 6 so that Monday = 0
		this.setState({
			day: (new Date().getDay() + 6) % 7
		});
	}


	onGoNextDay = () => {
		this.setState(prevState => ({
			day: (prevState.day + 1) % 7
		}));
	}


	onGoPrevDay = () => {
		this.setState(prevState => ({
			day: (prevState.day + 6) % 7
		}))
	}


	render() {
		if (this.props.friends == null) {
			return "Loading friends' schedules...";
		}

		if (this.props.friends.length === 0) {
			// TODO: Display a card to recommend adding friends
		}

		const friends = this.props.friends.slice().sort((a, b) => a.user.username.localeCompare(b.user.username));

		let names = [];
		let schedules = [];

		// TODO: When my schedule is empty, display card to recommend editing my schedule
		if (this.props.mySchedule) {
			const name = <div key={0} className="friend-schedules-name">Me</div>;
			const schedule = (
				<React.Fragment key={0}>
					<DailySchedule schedule={this.props.mySchedule[this.state.day]} />
				</React.Fragment>
			);

			names.push(name);
			schedules.push(schedule);
		}

		for (let i = 0; i < friends.length; i++) {
			const name = (
				<div key={friends[i].user.id} className="friend-schedules-name">
					{friends[i].user.username}
				</div>
			);

			const schedule = (
				<React.Fragment key={friends[i].user.id}>
					<DailySchedule schedule={friends[i].schedule[this.state.day]} />
				</React.Fragment>
			);

			names.push(name);
			schedules.push(schedule);
		}

		return (
			<Card className="friend-schedules-card" elevation={4}>
				<CardContent>
					<div className="friend-schedules-title-area">
						<IconButton onClick={this.onGoPrevDay}>
							<Icon>navigate_before</Icon>
						</IconButton>
						<Typography className="friend-schedules-title" variant="h5">{dayOfWeek[this.state.day]}</Typography>
						<IconButton onClick={this.onGoNextDay}>
							<Icon>navigate_next</Icon>
						</IconButton>
					</div>
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
}

const Dashboard = connect(select, mapDispatchToProps)(ConnectedDashboard);
export default Dashboard;
