import React from 'react';
import {
	Button,
	Checkbox,
	FormControlLabel,
	TextField
} from '@material-ui/core';

const EMAIL_REGEX = /^.+@.+\..+/;

class CreateUserForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			username: '',
			password: '',
			remember: false,
			emailError: false,
			passwordError: false
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();

		// TODO: This should probably pop up some sort of message
		if (this.state.emailError || this.state.passwordError) {
			return;
		}

		let trimmedEmail = this.state.email.trim();
		let trimmedUsername = this.state.username.trim();

		this.props.onSubmit(trimmedEmail, trimmedUsername, this.state.password, this.state.remember);
	};


	handleChange = (event) => {
		const input = event.target;
		const value = (input.type === 'checkbox') ? input.checked : input.value;

		this.setState({
			[input.name]: value
		});

		// Perform form validation
		if (this.state.emailError && input.name === 'email') {
			this.validateEmail(event);
			return;
		}

		if (this.state.passwordError && input.name === 'password') {
			this.validatePassword(event);
		}
	}


	validateEmail = (event) => {
		let value = event.target.value;

		this.setState({
			emailError: !EMAIL_REGEX.test(value)
		});
	}


	validatePassword = (event) => {
		let value = event.target.value;
		this.setState({
			passwordError: value.length < 8
		});
	}


	render() {
		const rememberMe = <Checkbox checked={this.state.remember} onChange={this.handleChange} />;

		return (
			<form id="auth-form" onSubmit={this.onFormSubmit}>
				<TextField className="auth-field"
				           name="email"
				           label="Email"
				           type="email"
				           error={this.state.emailError}
				           helperText={this.state.emailError ? 'Must be a valid email' : null}
				           onChange={this.handleChange}
				           onBlur={this.validateEmail}
				/>
				<TextField className="auth-field"
				           name="username"
				           label="Username"
				           type="text"
				           onChange={this.handleChange}/>
				<TextField className="auth-field"
				           name="password"
				           label="Password"
				           type="password"
				           error={this.state.passwordError}
				           helperText={this.state.passwordError ? 'Must be at least 8 characters' : null}
				           onChange={this.handleChange}
				           onBlur={this.validatePassword}
				/>
				<FormControlLabel name="remember"
				                  control={rememberMe}
				                  label="Remember me"
				/>
				<Button id="auth-submit-button"
				        variant="contained"
				        color="primary"
				        type="submit"
				        disabled={this.state.emailError || this.state.passwordError}
				>
					Submit
				</Button>
			</form>
		);
	}
}

export default CreateUserForm;
