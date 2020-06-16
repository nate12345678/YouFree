import '../css/IntervalManagement.css';
import React from 'react';
import {
	Button
} from '@material-ui/core';

class IntervalManagement extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			dayofWeek: -1,
			startMin: -1,
			endMin: -1
		}
	}

	onFormSubmit = (event) => {
		event.preventDefault();
		this.props.onSubmit(this.state.dayOfWeek, this.state.startMin, this.state.endMin);
	}

	render() {
		return (
			<form id="AddIntervalForm">
				<input id="dayOfWeekInput" type="number" onChange={(event) => this.setState({dayOfWeek: event.target.value})} />
				<input id="startMin" type="number" onChange={(event) => this.setState({startMin: event.target.value})} />
				<input id="endMin" type="number" onChange={(event) => this.setState({endMin: event.target.value})} />
				<Button variant="contained" color="primary" onClick={this.onFormSubmit}>Add Interval</Button>
			</form>
		);
	}

}

export default IntervalManagement;
