import React from 'react';
import {
	Button,
	Checkbox,
	FormControlLabel,
	TextField
} from '@material-ui/core';

class CreateUserForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			username: '',
			password: '',
			remember: false
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();
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
	}


	render() {
		return (
			<form id="authForm" onSubmit={this.onFormSubmit}>
				<TextField className="AuthField"
				           name="email"
				           label="Email"
				           type="email"
				           onChange={this.handleChange}/>
				<TextField className="AuthField"
				           name="username"
				           label="Username"
				           type="text"
				           onChange={this.handleChange}/>
				<TextField className="AuthField"
				           name="password"
				           label="Password"
				           type="password"
				           onChange={this.handleChange}/>
				<FormControlLabel name="remember"
				                  control={<Checkbox checked={this.state.remember} onChange={this.handleChange} />}
				                  label="Remember me"/>
				<Button id="authSubmitButton"
				        variant="contained"
				        color="primary"
				        type="submit"
				>
					Submit
				</Button>
			</form>
		);
	}
}

export default CreateUserForm;
