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
		};
	}


	componentDidMount() {
		this.props.getSchedule();
	}


	handleEditClick = () => {
		this.setState((state) => ({
			editMode: !state.editMode
		}));
	};


	onEditCancel = () => {
		this.setState({
			editMode: false
		});
	}


	render() {
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
				<Card className="my-weekly-schedule-card" elevation={4}>
					<CardContent>
						<WeeklySchedule schedule={this.props.schedule}/>
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
						<EditScheduleForm onSubmit={this.props.onAddInterval} onCancel={this.onEditCancel} />
					</Collapse>
				</Card>
			</>
		);
	}
}

export default MyProfilePage;
