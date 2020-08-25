import React from 'react';
import {
	Button,
	TextField
} from '@material-ui/core';
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';

class LoginForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			password: '',
			remember: false
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();
		let strippedEmail = this.state.email.replace(/\s+/g, '');

		this.props.onSubmit(strippedEmail, this.state.password, this.state.remember);
	};


	handleChange = (event) => {
		const input = event.target;
		const value = (input.type === 'checkbox') ? input.checked : input.value;

		this.setState({
			[input.name]: value
		});
	}


	render() {
		return (
			<form id="auth-form" onSubmit={this.onFormSubmit}>
				<TextField className="auth-field"
				           name="email"
				           label="Email"
				           type="email"
				           onChange={this.handleChange}
				/>
				<TextField className="auth-field"
				           name="password"
				           label="Password"
				           type="password"
				           onChange={this.handleChange}/>
				<FormControlLabel name="remember"
				                  control={<Checkbox checked={this.state.remember} onChange={this.handleChange} />}
				                  label="Remember me"/>
				<Button id="auth-submit-button"
				        variant="contained"
				        color="primary"
				        type="submit"
				>
					Login
				</Button>
			</form>
		);
	}
}

export default LoginForm;
