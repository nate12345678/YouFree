import '../css/CreateUser.css';
import React from 'react';
import {
	Button,
	InputBase
} from '@material-ui/core';

class CreateUser extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			username: '',
			password: ''
		}
	}


	onFormSubmit = (event) => {
		event.preventDefault();
		let strippedEmail = this.state.email.replace(/\s+/g, '');
		let strippedUsername = this.state.username.replace(/\s+/g, '');

		this.props.onSubmit(strippedEmail, strippedUsername, this.state.password);
	}


	render() {

		const emailInput = (
			<InputBase
				id="emailInput"
				placeholder="Email..."
				value={this.state.email}
				onChange={(event) => this.setState({ email: event.target.value })}
				inputProps={{'aria-label': 'Email'}}
			/>
		);

		const usernameInput = (
			<InputBase
				id="usernameInput"
				placeholder="Username..."
				value={this.state.username}
				onChange={(event) => this.setState({ username: event.target.value })}
				inputProps={{'aria-label': 'Username'}}
			/>
		);

		const passwordInput = (
			<InputBase
				id="passwordInput"
				placeholder="Password..."
				value={this.state.password}
				onChange={(event) => this.setState({ password: event.target.value })}
				inputProps={{'aria-label': 'Password'}}
			/>
		);

		return (
			<form id="createUserForm">
				{emailInput}
				{usernameInput}
				{passwordInput}
				<Button onClick={this.onFormSubmit}>Submit</Button>
			</form>
		);
	}
}

export default CreateUser;
