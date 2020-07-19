import '../css/MyProfilePage.css';
import React from 'react';
import {
	Button,
	Card,
	CardActions,
	CardContent,
	Collapse,
	FormControl,
	Icon,
	IconButton,
	InputLabel,
	MenuItem,
	Select,
	TextField,
	Typography
} from '@material-ui/core';
import WeeklySchedule from './WeeklySchedule';

class MyProfilePage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			editMode: false,
			editForm: {
				dayOfWeek: 0,
				startMin: '00:00',
				endMin: '00:00'
			}
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


	handleEditFormChange = (event) => {
		const name = event.target.name;
		const value = event.target.value;

		this.setState((state) => ({
			editForm: {
				...state.editForm,
				[name]: value
			}
		}));
	};


	handleEditFormSubmit = (event) => {
		event.preventDefault();
		// TODO: this.props.addInterval(day, time, time)
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
						<div className="edit-weekly-schedule-wrapper">
							<form className="edit-weekly-schedule-form" onSubmit={this.handleEditFormSubmit}>
								<div className="edit-weekly-schedule-inputs">
									<FormControl variant="outlined" margin="normal">
										<InputLabel id="edit-weekly-schedule-day-label">Day of Week</InputLabel>
										<Select className="edit-weekly-schedule-day-select"
										        name="dayOfWeek"
										        value={this.state.editForm.dayOfWeek}
										        labelId={'edit-weekly-schedule-day-label'}
										        label="Day of Week"
										        onChange={this.handleEditFormChange}>
											<MenuItem value={0}>Monday</MenuItem>
											<MenuItem value={1}>Tuesday</MenuItem>
											<MenuItem value={2}>Wednesday</MenuItem>
											<MenuItem value={3}>Thursday</MenuItem>
											<MenuItem value={4}>Friday</MenuItem>
											<MenuItem value={5}>Saturday</MenuItem>
											<MenuItem value={6}>Sunday</MenuItem>
										</Select>
									</FormControl>
									<TextField className="edit-weekly-schedule-start-field"
									           name="startMin"
									           label="Start Time"
									           variant="outlined"
									           size="medium"
									           margin="normal"
									           type="time"
									           value={this.state.editForm.startMin}
									           onChange={this.handleEditFormChange}/>
									<TextField className="edit-weekly-schedule-end-field"
									           name="endMin"
									           label="End Time"
									           variant="outlined"
									           size="medium"
									           margin="normal"
									           type="time"
									           value={this.state.editForm.endMin}
									           onChange={this.handleEditFormChange}/>
								</div>
								<div className="edit-weekly-schedule-buttons">
									<Button className="edit-weekly-schedule-cancel"
									        variant="outlined"
									        color="primary"
									>Cancel</Button>
									<Button className="edit-weekly-schedule-submit"
									        variant="contained"
									        color="primary"
									        type="submit"
									        disableElevation
									>Save</Button>
								</div>
							</form>
						</div>
					</Collapse>
				</Card>
			</>
		);
	}
}

export default MyProfilePage;
