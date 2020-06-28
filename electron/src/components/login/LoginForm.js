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


	render() {
		return (
			<form id="createUserForm" onSubmit={this.onFormSubmit}>
				<TextField
					className="createUserField"
					label="Email" type="email"
					onChange={(event) => this.setState({ email: event.target.value })}/>
				<TextField
					className="createUserField"
					label="Password" type="password"
					onChange={(event) => this.setState({ password: event.target.value })}/>
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
