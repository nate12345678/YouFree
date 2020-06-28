import '../../css/login/CreateUserForm.css';
import React from 'react';
import {
	Button,
	TextField
} from '@material-ui/core';

class CreateUserForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			email: '',
			username: '',
			password: ''
		};
	}


	onFormSubmit = (event) => {
		event.preventDefault();
		let strippedEmail = this.state.email.replace(/\s+/g, '');
		let strippedUsername = this.state.username.replace(/\s+/g, '');

		this.props.onSubmit(strippedEmail, strippedUsername, this.state.password);
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
				<TextField className="createUserField"
				           name="email"
				           label="Email"
				           type="email"
				           onChange={this.handleChange}/>
				<TextField className="createUserField"
				           name="username"
				           label="Username"
				           type="text"
				           onChange={this.handleChange}/>
				<TextField className="createUserField"
				           name="password"
				           label="Password"
				           type="password"
				           onChange={this.handleChange}/>
				<Button id="createUserSubmitButton"
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
