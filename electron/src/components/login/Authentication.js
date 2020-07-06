import '../../css/login/Authentication.css';
import React from 'react';
import CreateUserForm from './CreateUserForm';
import LoginForm from './LoginForm';
import {
	Card,
	CardContent,
	Paper,
	Tab,
	Tabs
} from '@material-ui/core';

class Authentication extends React.Component {

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
			<Card id="authCard" elevation={4}>
				<Paper id="tabs" square elevation={1}>
					<Tabs value={this.state.currentTab}
					      variant="fullWidth"
					      indicatorColor="primary"
					      textColor="primary"
					      onChange={this.handleChange}>

						<Tab label="Login"/>
						<Tab label="Create User"/>
					</Tabs>
				</Paper>

				<CardContent id="authCardContent">
					{content}
				</CardContent>
			</Card>
		);
	}
}

export default Authentication;
