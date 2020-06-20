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
			startMin: '',
			endMin: ''
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();
		this.props.onSubmit(this.state.dayOfWeek, this.state.startMin, this.state.endMin);
	};


	render() {
		return (
			<form id="addIntervalForm">
				<div className="row centered">
					<TextField
						id="dayOfWeekSelect"
						label="Day"
						value={this.state.dayOfWeek}
						variant="outlined"
						onChange={(event) => this.setState({ dayOfWeek: event.target.value })}
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
						<TextField
							id="startMinField"
							label="Start Time"
							variant="outlined"
							size="small"
							margin="dense"
							type="number"
							value={this.state.startMin}
							onChange={(event) => this.setState({ startMin: +event.target.value })}/>
						<TextField
							id="endMinField"
							label="End Time"
							variant="outlined"
							size="small"
							margin="dense"
							type="number"
							value={this.state.endMin}
							onChange={(event) => this.setState({ endMin: +event.target.value })}/>
					</div>

					<Button id="submitButton" variant="contained" color="primary" onClick={this.onFormSubmit}>Add Interval</Button>
				</div>
			</form>
		);
	}

}

export default EditScheduleForm;
