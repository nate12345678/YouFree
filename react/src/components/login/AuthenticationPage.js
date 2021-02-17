import '../../css/login/Authentication.css';
import React from 'react';
import CreateUserForm from './CreateUserForm';
import LoginForm from './LoginForm';
import {
	Paper,
	Tab,
	Tabs
} from '@material-ui/core';
import { connect } from 'react-redux';
import {
	createUser,
	login
} from '../../state/Effects';


function mapDispatchToProps(dispatch) {
	return {
		onCreateUserSubmit: (email, username, password, remember) => dispatch(createUser(email, username, password, remember)),
		onLoginSubmit: (email, password, remember) => dispatch(login(email, password, remember))
	};
}


class ConnectedAuthenticationPage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			currentTab: 0
		};
	}


	handleChange = (event, newValue) => {
		this.setState({
			currentTab: newValue
		});
	};


	render() {
		let content = this.state.currentTab === 1
			? <CreateUserForm onSubmit={this.props.onCreateUserSubmit}/>
			: <LoginForm onSubmit={this.props.onLoginSubmit}/>;

		return (
			<Paper className="auth-card">
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
			</Paper>
		);
	}
}

const AuthenticationPage = connect(null, mapDispatchToProps)(ConnectedAuthenticationPage);
export default AuthenticationPage;
