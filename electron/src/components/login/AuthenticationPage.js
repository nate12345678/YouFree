import '../../css/login/Authentication.css';
import React from 'react';
import CreateUserForm from './CreateUserForm';
import LoginForm from './LoginForm';
import {
	Tab,
	Tabs
} from '@material-ui/core';

class AuthenticationPage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			currentTab: 0
		};
	}


	handleChange = (event, newValue) => {
		this.setState(() => {
			return {
				currentTab: newValue
			};
		});
	};


	render() {
		let content = this.state.currentTab === 1
			? <CreateUserForm onSubmit={this.props.onCreateUserSubmit}/>
			: <LoginForm onSubmit={this.props.onLoginSubmit}/>;

		return (
			<div className="card-elevation4 auth-card">
				<div className="card-elevation1" id="tabs">
					<Tabs value={this.state.currentTab}
					      variant="fullWidth"
					      indicatorColor="primary"
					      textColor="primary"
					      onChange={this.handleChange}>

						<Tab label="Login"/>
						<Tab label="Create User"/>
					</Tabs>
				</div>

				<div className="card-content auth-card-content">
					{content}
				</div>
			</div>
		);
	}
}

export default AuthenticationPage;
