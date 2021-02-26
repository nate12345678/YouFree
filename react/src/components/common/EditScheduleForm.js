import './EditScheduleForm.css';
import React from 'react';
import {
	Button,
	FormControl,
	InputLabel,
	MenuItem,
	Select,
	TextField,
	Typography
} from '@material-ui/core';

class EditScheduleForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = (props.interval)
			? {
				dayOfWeek: props.interval.dayOfWeek,
				startMin: this.convertTime(props.interval.startMin),
				endMin: this.convertTime(props.interval.endMin),
				timeError: false
			}
			: {
				dayOfWeek: 0,
				startMin: '00:00',
				endMin: '12:00',
				timeError: false
			}
		;
	}


	convertTime = (minutes) => {
		const hours = `${Math.floor(minutes / 60)}`.padStart(2, '0');
		const min = `${minutes % 60}`.padStart(2, '0');
		return hours + ':' + min;
	}


	onFormSubmit = (event) => {
		event.preventDefault();

		if (this.state.timeError) {
			return;
		}

		const startMinSplit = this.state.startMin.split(':');
		const startMin = +startMinSplit[0] * 60 + +startMinSplit[1];

		const endMinSplit = this.state.endMin.split(':');
		const endMin = +endMinSplit[0] * 60 + +endMinSplit[1];

		// If we're updating an interval...
		if (this.props.interval) {
			this.props.onUpdate(this.state.dayOfWeek, startMin, endMin);
			return;
		}

		this.props.onSubmit(this.state.dayOfWeek, startMin, endMin);
	};


	onCancel = () => {
		if (this.props.interval) {
			this.props.onIntervalDeselection();
			return;
		}

		this.props.onCancel();
	}


	handleInputChange = (event) => {
		const input = event.target;

		this.setState({
			[input.name]: input.value
		});

		if (input.name === 'startMin') {
			this.validateStartTime(event);
		}

		if (input.name === 'endMin') {
			this.validateEndTime(event);
		}
	}


	validateStartTime = (event) => {
		const value = event.target.value;
		this.setState(prevState => ({
			timeError: value >= prevState.endMin
		}));
	}


	validateEndTime = (event) => {
		const value = event.target.value;
		this.setState(prevState => ({
			timeError: value <= prevState.startMin
		}));
	}


	componentDidUpdate(prevProps, prevState, snapshot) {
		// If a new interval has been selected, populate form with its values
		if (this.props.interval && (prevProps.interval == null || this.props.interval.id !== prevProps.interval.id)) {
			this.setState({
				dayOfWeek: this.props.interval.dayOfWeek,
				startMin: this.convertTime(this.props.interval.startMin),
				endMin: this.convertTime(this.props.interval.endMin),
				timeError: false
			});
		}
	}


	render() {
		const deleteButton = (
			<Button className="edit-schedule-delete-button"
			        variant="outlined"
			        color="secondary"
			        onClick={this.props.onDelete}
			        disableElevation
			>Delete</Button>
		);

		const errorMessage = (
			<div className="edit-schedule-error-message">Start time must be earlier than end time</div>
		);

		const title = this.props.interval
			? 'Edit time interval'
			: 'Add free time'
		;

		const subtitle = this.props.interval
			? 'Or click "cancel" to create a new one'
			: 'Or click an interval to edit'
		;

		return (
			<form className="edit-schedule-form" onSubmit={this.onFormSubmit}>
				<Typography variant="h5">{title}</Typography>
				<Typography variant="body2">{subtitle}</Typography>
				<div>
					<FormControl variant="outlined" margin="normal">
						<InputLabel id="edit-schedule-day-label">Day of Week</InputLabel>
						<Select className="edit-schedule-day-select"
								name="dayOfWeek"
								value={this.state.dayOfWeek}
								label="Day of Week"
						        labelId="edit-schedule-day-label"
								onChange={this.handleInputChange}
						>
							<MenuItem value={0}>Monday</MenuItem>
							<MenuItem value={1}>Tuesday</MenuItem>
							<MenuItem value={2}>Wednesday</MenuItem>
							<MenuItem value={3}>Thursday</MenuItem>
							<MenuItem value={4}>Friday</MenuItem>
							<MenuItem value={5}>Saturday</MenuItem>
							<MenuItem value={6}>Sunday</MenuItem>
						</Select>
					</FormControl>
					<TextField className="edit-schedule-start-field"
					           name="startMin"
					           label="Start Time"
					           variant="outlined"
					           size="medium"
					           margin="normal"
					           type="time"
					           value={this.state.startMin}
					           onChange={this.handleInputChange}
					           error={this.state.timeError}
					/>
					<TextField className="edit-schedule-end-field"
					           name="endMin"
					           label="End Time"
					           variant="outlined"
					           size="medium"
					           margin="normal"
					           type="time"
					           value={this.state.endMin}
					           onChange={this.handleInputChange}
					           error={this.state.timeError}
					/>
				</div>
				{ this.state.timeError ? errorMessage : null }
				<div>
					<Button className="edit-schedule-cancel-button"
					        variant="outlined"
					        color="primary"
					        onClick={this.onCancel}
					        disableElevation
					>Cancel</Button>
					{ this.props.interval ? deleteButton : null }
					<Button className="edit-schedule-submit-button"
					        variant="contained"
					        color="primary"
					        type="submit"
					        disabled={this.state.timeError}
					        disableElevation
					>{ this.props.interval ? 'Save' : 'Add Interval' }</Button>
				</div>
			</form>
		);
	}

}

export default EditScheduleForm;
