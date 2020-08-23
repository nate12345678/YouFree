import '../css/EditScheduleForm.css';
import React from 'react';
import {
	Button,
	MenuItem,
	TextField
} from '@material-ui/core';

class EditScheduleForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			dayOfWeek: '',
			startMin: '00:00',
			endMin: '00:00'
		};
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


	render() {
		return (
			<form id="EditScheduleForm">
				<TextField
					id="dayOfWeekSelect"
					label="Day"
					name="dayOfWeek"
					value={this.state.dayOfWeek}
					variant="outlined"
					onChange={this.handleInputChange}
					select
				>
					<MenuItem value={''}/>
					<MenuItem value={0}>Monday</MenuItem>
					<MenuItem value={1}>Tuesday</MenuItem>
					<MenuItem value={2}>Wednesday</MenuItem>
					<MenuItem value={3}>Thursday</MenuItem>
					<MenuItem value={4}>Friday</MenuItem>
					<MenuItem value={5}>Saturday</MenuItem>
					<MenuItem value={6}>Sunday</MenuItem>
				</TextField>

				<div id="timesDiv" className="col">
					<TextField id="startMinField"
					           name="startMin"
					           label="Start Time"
					           variant="outlined"
					           size="small"
					           margin="dense"
					           type="time"
					           value={this.state.startMin}
					           onChange={this.handleInputChange}/>
					<TextField id="endMinField"
					           name="endMin"
					           label="End Time"
					           variant="outlined"
					           size="small"
					           margin="dense"
					           type="time"
					           value={this.state.endMin}
					           onChange={this.handleInputChange}/>
				</div>

				<Button id="submitButton" variant="contained" color="primary" onClick={this.onFormSubmit}>Add Interval</Button>
			</form>
		);
	}

}

export default EditScheduleForm;
