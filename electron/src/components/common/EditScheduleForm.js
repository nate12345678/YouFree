import './EditScheduleForm.css';
import React from 'react';
import {
	Button,
	FormControl,
	InputLabel,
	MenuItem,
	Select,
	TextField
} from '@material-ui/core';

class EditScheduleForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			dayOfWeek: 0,
			startMin: '00:00',
			endMin: '00:00',
			intervalId: null
		};
	}


	convertTime = (minutes) => {
		const hours = `${Math.floor(minutes / 60)}`.padStart(2, '0');
		const min = `${minutes % 60}`.padStart(2, '0');
		return hours + ':' + min;
	}


	onFormSubmit = (event) => {
		event.preventDefault();

		const startMinSplit = this.state.startMin.split(':');
		const startMin = +startMinSplit[0] * 60 + +startMinSplit[1];

		const endMinSplit = this.state.endMin.split(':');
		const endMin = +endMinSplit[0] * 60 + +endMinSplit[1];

		this.props.onSubmit(this.state.dayOfWeek, startMin, endMin);
	};


	handleInputChange = (event) => {
		this.setState({
			[event.target.name]: event.target.value
		});
	}


	componentDidUpdate(prevProps, prevState, snapshot) {
		if (this.props.interval && this.props.interval.id !== this.state.intervalId) {
			this.setState((state, props) => ({
				dayOfWeek: props.interval.dayOfWeek,
				startMin: this.convertTime(props.interval.startMin),
				endMin: this.convertTime(props.interval.endMin),
				intervalId: props.interval.id
			}));
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

		return (
			<form className="edit-schedule-form" onSubmit={this.onFormSubmit}>
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
					           onChange={this.handleInputChange}/>
					<TextField className="edit-schedule-end-field"
					           name="endMin"
					           label="End Time"
					           variant="outlined"
					           size="medium"
					           margin="normal"
					           type="time"
					           value={this.state.endMin}
					           onChange={this.handleInputChange}/>
				</div>
				<div>
					<Button className="edit-schedule-cancel-button"
					        variant="outlined"
					        color="primary"
					        onClick={this.props.onCancel}
					        disableElevation
					>Cancel</Button>
					{ this.state.intervalId ? deleteButton : null }
					<Button className="edit-schedule-submit-button"
					        variant="contained"
					        color="primary"
					        type="submit"
					        disableElevation
					>{ this.state.intervalId ? 'Save' : 'Add Interval' }</Button>
				</div>
			</form>
		);
	}

}

export default EditScheduleForm;
