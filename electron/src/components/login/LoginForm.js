import '../../css/login/LoginForm.css';
import React from 'react';
import {
	Button,
	TextField
} from '@material-ui/core';

class LoginForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			password: ''
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();
		let strippedEmail = this.state.email.replace(/\s+/g, '');

		this.props.onSubmit(strippedEmail, this.state.password);
	};


	handleChange = (event) => {
		const input = event.target;
		const value = input.value;

		this.setState({
			[input.name]: value
		});
	}


	render() {
		return (
			<form id="createUserForm" onSubmit={this.onFormSubmit}>
				<TextField
					className="createUserField"
					name="email"
					label="Email" type="email"
					onChange={this.handleChange}/>
				<TextField
					className="createUserField"
					email="password"
					label="Password" type="password"
					onChange={this.handleChange}/>
				<Button
					id="loginSubmitButton"
					variant="contained"
					color="primary"
					type="submit"
				>Login
				</Button>
			</form>
		);
	}
}

export default LoginForm;
