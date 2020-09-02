import '../css/MyProfilePage.css';
import React from 'react';
import {
	Card,
	CardActions,
	CardContent,
	Collapse,
	Icon,
	IconButton,
	Typography
} from '@material-ui/core';
import WeeklySchedule from './WeeklySchedule';
import EditScheduleForm from './common/EditScheduleForm';

class MyProfilePage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			editMode: false,
			selectedInterval: null
		};
	}


	componentDidMount() {
		this.props.getSchedule();
	}


	handleEditClick = () => {
		this.setState((state) => ({
			editMode: !state.editMode,
			selectedInterval: null
		}));
	};


	onEditCancel = () => {
		this.setState({
			editMode: false,
			selectedInterval: null
		});
	}


	onIntervalSelection = (interval) => {
		if (this.state.editMode === false) return;

		this.setState({
			selectedInterval: interval
		});
	}


	onIntervalDeselection = () => this.onIntervalSelection(null);


	updateInterval = (dayOfWeek, startMin, endMin) => {
		this.props.onUpdateInterval(this.state.selectedInterval.id, dayOfWeek, startMin, endMin);

		this.setState({
			selectedInterval: null
		});
	}


	deleteInterval = () => {
		this.props.onDeleteInterval(this.state.selectedInterval.id);

		this.setState({
			selectedInterval: null
		});
	}


	render() {
		const weeklyScheduleClasses = 'my-weekly-schedule-card' + (this.state.editMode ? ' editable' : '');

		return (
			<>
				<Card className="my-profile-card" elevation={4}>
					<CardContent>
						<div className="my-profile-user">
							<div className="my-profile-pic"/>
							<div className="my-profile-info">
								<Typography className="my-profile-username" variant="h4">{this.props.user.username}</Typography>
								<Typography className="my-profile-email" variant="h6">{this.props.user.email}</Typography>
							</div>
						</div>
					</CardContent>
				</Card>
				<Card className={weeklyScheduleClasses} elevation={4}>
					<CardContent>
						<WeeklySchedule schedule={this.props.schedule}
						                onIntervalSelection={this.onIntervalSelection}
						                selectedInterval={this.state.selectedInterval}
						/>
					</CardContent>
					<CardActions>
						<IconButton id="edit-button"
						            aria-expanded={this.state.editMode}
						            aria-label="edit"
						            onClick={this.handleEditClick}>
							<Icon id="edit-icon">{this.state.editMode ? 'cancel' : 'edit'}</Icon>
						</IconButton>
					</CardActions>
					<Collapse in={this.state.editMode} unmountOnExit>
						<EditScheduleForm interval={this.state.selectedInterval}
						                  onSubmit={this.props.onAddInterval}
						                  onUpdate={this.updateInterval}
						                  onDelete={this.deleteInterval}
						                  onCancel={this.onEditCancel}
						                  onIntervalDeselection={this.onIntervalDeselection}
						/>
					</Collapse>
				</Card>
			</>
		);
	}
}

export default MyProfilePage;
